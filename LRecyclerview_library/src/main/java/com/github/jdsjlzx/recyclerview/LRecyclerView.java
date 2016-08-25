package com.github.jdsjlzx.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.github.jdsjlzx.BuildConfig;
import com.github.jdsjlzx.R;
import com.github.jdsjlzx.interfaces.Closeable;
import com.github.jdsjlzx.interfaces.OnSwipeMenuItemClickListener;
import com.github.jdsjlzx.interfaces.SwipeMenuCreator;
import com.github.jdsjlzx.swipe.SwipeMenu;
import com.github.jdsjlzx.swipe.SwipeMenuAdapter;
import com.github.jdsjlzx.swipe.SwipeMenuLayout;
import com.github.jdsjlzx.swipe.touch.DefaultItemTouchHelper;
import com.github.jdsjlzx.swipe.touch.OnItemMoveListener;
import com.github.jdsjlzx.swipe.touch.OnItemMovementListener;
import com.github.jdsjlzx.util.ViewUtils;
import com.github.jdsjlzx.view.ArrowRefreshHeader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhixian on 16/1/4.
 */
public class LRecyclerView extends RecyclerView {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    private boolean pullRefreshEnabled = true;
    private LScrollListener mLScrollListener;
    private ArrowRefreshHeader mRefreshHeader;
    private View mEmptyView;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private float mLastY = -1;
    private static final float DRAG_RATE = 2.0f;

    private LRecyclerViewAdapter mWrapAdapter;
    private boolean isNoMore = false;

    //scroll variables begin
    /**
     * 当前RecyclerView类型
     */
    protected LayoutManagerType layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;

    /**
     * 当前滑动的状态
     */
    private int currentScrollState = 0;

    /**
     * 触发在上下滑动监听器的容差距离
     */
    private static final int HIDE_THRESHOLD = 20;

    /**
     * 滑动的距离
     */
    private int mDistance = 0;

    /**
     * 是否需要监听控制
     */
    private boolean mIsScrollDown = true;

    /**
     * Y轴移动的实际距离（最顶部为0）
     */
    private int mScrolledYDistance = 0;

    /**
     * X轴移动的实际距离（最左侧为0）
     */
    private int mScrolledXDistance = 0;
    //scroll variables end

    //swipe menu begin
    /**
     * Left menu.
     */
    public static final int LEFT_DIRECTION = 1;
    /**
     * Right menu.
     */
    public static final int RIGHT_DIRECTION = -1;

    /**
     * Invalid position.
     */
    private static final int INVALID_POSITION = -1;

    private int mDownX;
    private int mDownY;
    private boolean isSwiebeEnable = true;

    protected ViewConfiguration mViewConfig;
    protected SwipeMenuLayout mOldSwipedLayout;
    protected int mOldTouchedPosition = INVALID_POSITION;

    private SwipeMenuCreator mSwipeMenuCreator;
    private OnSwipeMenuItemClickListener mSwipeMenuItemClickListener;
    private DefaultItemTouchHelper mDefaultItemTouchHelper;
    //swipe menu end

    //viewpager begin
    private boolean isViewpagerMode = false;
    private float mTriggerOffset = 0.25f;
    private float mFlingFactor = 0.15f;
    private float mTouchSpan;
    private List<OnPageChangedListener> mOnPageChangedListeners;
    private int mSmoothScrollTargetPosition = -1;
    private int mPositionBeforeScroll = -1;

    private boolean mSinglePageFling;

    boolean mNeedAdjust;
    int mFisrtLeftWhenDragging;
    int mFirstTopWhenDragging;
    View mCurView;
    int mMaxLeftWhenDragging = Integer.MIN_VALUE;
    int mMinLeftWhenDragging = Integer.MAX_VALUE;
    int mMaxTopWhenDragging = Integer.MIN_VALUE;
    int mMinTopWhenDragging = Integer.MAX_VALUE;
    private int mPositionOnTouchDown = -1;
    private boolean mHasCalledOnPageChanged = true;
    private boolean reverseLayout = false;

    //viewpager end
    public LRecyclerView(Context context) {
        this(context, null);
    }

    public LRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mViewConfig = ViewConfiguration.get(getContext());

        initAttrs(context, attrs, defStyle);
        setNestedScrollingEnabled(false);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewPager, defStyle,
                0);
        mFlingFactor = a.getFloat(R.styleable.RecyclerViewPager_rvp_flingFactor, 0.15f);
        mTriggerOffset = a.getFloat(R.styleable.RecyclerViewPager_rvp_triggerOffset, 0.25f);
        mSinglePageFling = a.getBoolean(R.styleable.RecyclerViewPager_rvp_singlePageFling, mSinglePageFling);
        a.recycle();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null && mDataObserver != null) {
            oldAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();

        mWrapAdapter = (LRecyclerViewAdapter) getAdapter();
        mRefreshHeader = mWrapAdapter.getRefreshHeader();

        //add for swipe item
        if (mWrapAdapter.getInnerAdapter() instanceof SwipeMenuAdapter) {
            SwipeMenuAdapter menuAdapter = (SwipeMenuAdapter) mWrapAdapter.getInnerAdapter();
            menuAdapter.setSwipeMenuCreator(mDefaultMenuCreator);
            menuAdapter.setSwipeMenuItemClickListener(mDefaultMenuItemClickListener);
        }


    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();

            if (adapter instanceof LRecyclerViewAdapter) {
                LRecyclerViewAdapter headerAndFooterAdapter = (LRecyclerViewAdapter) adapter;
                if (headerAndFooterAdapter.getInnerAdapter() != null && mEmptyView != null) {
                    int count = headerAndFooterAdapter.getInnerAdapter().getItemCount();
                    if (count == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                        LRecyclerView.this.setVisibility(View.GONE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                        LRecyclerView.this.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (adapter != null && mEmptyView != null) {
                    if (adapter.getItemCount() == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                        LRecyclerView.this.setVisibility(View.GONE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                        LRecyclerView.this.setVisibility(View.VISIBLE);
                    }
                }
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();

                if (mCurView != null) {
                    mMaxLeftWhenDragging = Math.max(mCurView.getLeft(), mMaxLeftWhenDragging);
                    mMaxTopWhenDragging = Math.max(mCurView.getTop(), mMaxTopWhenDragging);
                    mMinLeftWhenDragging = Math.min(mCurView.getLeft(), mMinLeftWhenDragging);
                    mMinTopWhenDragging = Math.min(mCurView.getTop(), mMinTopWhenDragging);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }

                //add for swipe menu
                if (mWrapAdapter.getInnerAdapter() instanceof SwipeMenuAdapter) {
                    if (mOldSwipedLayout != null && mOldSwipedLayout.isMenuOpen()) {
                        mOldSwipedLayout.smoothCloseMenu();
                    }
                }

                break;
            default:
                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLScrollListener != null) {
                            mLScrollListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    private boolean isOnTop() {
        if (pullRefreshEnabled && mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * set view when no content item
     *
     * @param emptyView visiable view when items is empty
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    public void setNoMore(boolean noMore){
        isNoMore = noMore;
    }

    public void setRefreshHeader(ArrowRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
        if(mWrapAdapter != null) {
            mWrapAdapter.setPullRefreshEnabled(enabled);
        }
    }

    public void setRefreshProgressStyle(int style) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
    }

    public void setLScrollListener(LScrollListener listener) {
        mLScrollListener = listener;
    }

    public interface LScrollListener {

        void onRefresh();//pull down to refresh

        void onScrollUp();//scroll down to up

        void onScrollDown();//scroll from up to down

        void onBottom();//load next page

        void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance
    }

    private int mRefreshHeaderHeight;
    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && mLScrollListener != null) {
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            mRefreshHeaderHeight = mRefreshHeader.getMeasuredHeight();
            mRefreshHeader.onMove(mRefreshHeaderHeight);
            mLScrollListener.onRefresh();
        }
    }

    public void forceToRefresh() {
        if (pullRefreshEnabled && mLScrollListener != null) {
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeaderHeight);
            mLScrollListener.onRefresh();
        }
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (null != mLScrollListener) {
            int firstVisibleItemPosition = 0;
            RecyclerView.LayoutManager layoutManager = getLayoutManager();

            if (layoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    layoutManagerType = LayoutManagerType.LinearLayout;
                } else if (layoutManager instanceof GridLayoutManager) {
                    layoutManagerType = LayoutManagerType.GridLayout;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    layoutManagerType = LayoutManagerType.StaggeredGridLayout;
                } else {
                    throw new RuntimeException(
                            "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (layoutManagerType) {
                case LinearLayout:
                    firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case GridLayout:
                    firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case StaggeredGridLayout:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(lastPositions);
                    firstVisibleItemPosition = findMax(lastPositions);
                    break;
            }

            // 根据类型来计算出第一个可见的item的位置，由此判断是否触发到底部的监听器
            // 计算并判断当前是向上滑动还是向下滑动
            calculateScrollUpOrDown(firstVisibleItemPosition, dy);
            // 移动距离超过一定的范围，我们监听就没有啥实际的意义了
            mScrolledXDistance += dx;
            mScrolledYDistance += dy;
            mScrolledXDistance = (mScrolledXDistance < 0) ? 0 : mScrolledXDistance;
            mScrolledYDistance = (mScrolledYDistance < 0) ? 0 : mScrolledYDistance;
            if (mIsScrollDown && (dy == 0)) {
                mScrolledYDistance = 0;
            }
            //Be careful in here
            mLScrollListener.onScrolled(mScrolledXDistance, mScrolledYDistance);
        }

    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        currentScrollState = state;

        if(!isViewpagerMode) {

            if (currentScrollState == RecyclerView.SCROLL_STATE_IDLE && mLScrollListener != null) {
                RecyclerView.LayoutManager layoutManager = getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0
                        && lastVisibleItemPosition >= totalItemCount - 1
                        && totalItemCount > visibleItemCount
                        && !isNoMore
                        && !mIsScrollDown
                        && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                    mLScrollListener.onBottom();
                }

            }

        } else {
            //viewpager begin
            if (state == SCROLL_STATE_DRAGGING) {
                mNeedAdjust = true;
                mCurView = getLayoutManager().canScrollHorizontally() ? ViewUtils.getCenterXChild(this) :
                        ViewUtils.getCenterYChild(this);
                if (mCurView != null) {
                    if (mHasCalledOnPageChanged) {
                        // While rvp is scrolling, mPositionBeforeScroll will be previous value.
                        mPositionBeforeScroll = getChildLayoutPosition(mCurView);
                        mHasCalledOnPageChanged = false;
                    }

                    mFisrtLeftWhenDragging = mCurView.getLeft();
                    mFirstTopWhenDragging = mCurView.getTop();
                } else {
                    mPositionBeforeScroll = -1;
                }
                mTouchSpan = 0;
            } else if (state == SCROLL_STATE_SETTLING) {
                mNeedAdjust = false;
                if (mCurView != null) {
                    if (getLayoutManager().canScrollHorizontally()) {
                        mTouchSpan = mCurView.getLeft() - mFisrtLeftWhenDragging;
                    } else {
                        mTouchSpan = mCurView.getTop() - mFirstTopWhenDragging;
                    }
                } else {
                    mTouchSpan = 0;
                }
                mCurView = null;
            } else if (state == SCROLL_STATE_IDLE) {
                if (mNeedAdjust) {
                    int targetPosition = getLayoutManager().canScrollHorizontally() ? ViewUtils.getCenterXChildPosition(this) :
                            ViewUtils.getCenterYChildPosition(this);
                    if (mCurView != null) {
                        targetPosition = getChildAdapterPosition(mCurView);
                        if (getLayoutManager().canScrollHorizontally()) {
                            int spanX = mCurView.getLeft() - mFisrtLeftWhenDragging;
                            // if user is tending to cancel paging action, don't perform position changing
                            if (spanX > mCurView.getWidth() * mTriggerOffset && mCurView.getLeft() >= mMaxLeftWhenDragging) {
                                if (!reverseLayout) targetPosition--;
                                else targetPosition++;
                            } else if (spanX < mCurView.getWidth() * -mTriggerOffset && mCurView.getLeft() <= mMinLeftWhenDragging) {
                                if (!reverseLayout) targetPosition++;
                                else targetPosition--;
                            }
                        } else {
                            int spanY = mCurView.getTop() - mFirstTopWhenDragging;
                            if (spanY > mCurView.getHeight() * mTriggerOffset && mCurView.getTop() >= mMaxTopWhenDragging) {
                                if (!reverseLayout) targetPosition--;
                                else targetPosition++;
                            } else if (spanY < mCurView.getHeight() * -mTriggerOffset && mCurView.getTop() <= mMinTopWhenDragging) {
                                if (!reverseLayout) targetPosition++;
                                else targetPosition--;
                            }
                        }
                    }
                    smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()));
                    mCurView = null;
                } else if (mSmoothScrollTargetPosition != mPositionBeforeScroll) {

                    if (mOnPageChangedListeners != null) {
                        for (OnPageChangedListener onPageChangedListener : mOnPageChangedListeners) {
                            if (onPageChangedListener != null) {
                                onPageChangedListener.OnPageChanged(mPositionBeforeScroll, mSmoothScrollTargetPosition);
                            }
                        }
                    }
                    mHasCalledOnPageChanged = true;
                    mPositionBeforeScroll = mSmoothScrollTargetPosition;
                }
                // reset
                mMaxLeftWhenDragging = Integer.MIN_VALUE;
                mMinLeftWhenDragging = Integer.MAX_VALUE;
                mMaxTopWhenDragging = Integer.MIN_VALUE;
                mMinTopWhenDragging = Integer.MAX_VALUE;
            }
            //viewpager end

        }


    }

    /**
     * 计算当前是向上滑动还是向下滑动
     */
    private void calculateScrollUpOrDown(int firstVisibleItemPosition, int dy) {
        if (firstVisibleItemPosition == 0) {
            if (!mIsScrollDown) {
                mIsScrollDown = true;
                mLScrollListener.onScrollDown();
            }
        } else {
            if (mDistance > HIDE_THRESHOLD && mIsScrollDown) {
                mIsScrollDown = false;
                mLScrollListener.onScrollUp();
                mDistance = 0;
            } else if (mDistance < -HIDE_THRESHOLD && !mIsScrollDown) {
                mIsScrollDown = true;
                mLScrollListener.onScrollDown();
                mDistance = 0;
            }
        }
        if ((mIsScrollDown && dy > 0) || (!mIsScrollDown && dy < 0)) {
            mDistance += dy;
        }
    }

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

    //method for swipe menu end
    private void initializeItemTouchHelper() {
        if (mDefaultItemTouchHelper == null) {
            mDefaultItemTouchHelper = new DefaultItemTouchHelper();
            mDefaultItemTouchHelper.attachToRecyclerView(this);
        }
    }

    /**
     * Set OnItemMoveListener.
     *
     * @param onItemMoveListener {@link OnItemMoveListener}.
     */
    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        initializeItemTouchHelper();
        mDefaultItemTouchHelper.setOnItemMoveListener(onItemMoveListener);
    }

    /**
     * Set OnItemMovementListener.
     *
     * @param onItemMovementListener {@link OnItemMovementListener}.
     */
    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener) {
        initializeItemTouchHelper();
        mDefaultItemTouchHelper.setOnItemMovementListener(onItemMovementListener);
    }


    /**
     * Set can long press drag.
     *
     * @param canDrag drag true, otherwise is can't.
     */
    public void setLongPressDragEnabled(boolean canDrag) {
        initializeItemTouchHelper();
        mDefaultItemTouchHelper.setLongPressDragEnabled(canDrag);
    }

    /**
     * Get can long press drag.
     *
     * @return drag true, otherwise is can't.
     */
    public boolean isLongPressDragEnabled() {
        initializeItemTouchHelper();
        return this.mDefaultItemTouchHelper.isLongPressDragEnabled();
    }


    /**
     * Set can long press swipe.
     *
     * @param canSwipe swipe true, otherwise is can't.
     */
    public void setItemViewSwipeEnabled(boolean canSwipe) {
        initializeItemTouchHelper();
        isSwiebeEnable = !canSwipe;
        mDefaultItemTouchHelper.setItemViewSwipeEnabled(canSwipe);
    }

    /**
     * Get can long press swipe.
     *
     * @return swipe true, otherwise is can't.
     */
    public boolean isItemViewSwipeEnabled() {
        initializeItemTouchHelper();
        return this.mDefaultItemTouchHelper.isItemViewSwipeEnabled();
    }

    /**
     * Start drag a item.
     *
     * @param viewHolder the ViewHolder to start dragging. It must be a direct child of RecyclerView.
     */
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        initializeItemTouchHelper();
        mDefaultItemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Star swipe a item.
     *
     * @param viewHolder the ViewHolder to start swiping. It must be a direct child of RecyclerView.
     */
    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        initializeItemTouchHelper();
        mDefaultItemTouchHelper.startSwipe(viewHolder);
    }

    /**
     * Set to create menu listener.
     *
     * @param swipeMenuCreator listener.
     */
    public void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        this.mSwipeMenuCreator = swipeMenuCreator;
    }

    /**
     * Set to click menu listener.
     *
     * @param swipeMenuItemClickListener listener.
     */
    public void setSwipeMenuItemClickListener(OnSwipeMenuItemClickListener swipeMenuItemClickListener) {
        this.mSwipeMenuItemClickListener = swipeMenuItemClickListener;
    }

    /**
     * Default swipe menu creator.
     */
    private SwipeMenuCreator mDefaultMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            if (mSwipeMenuCreator != null) {
                mSwipeMenuCreator.onCreateMenu(swipeLeftMenu, swipeRightMenu, viewType);
            }
        }
    };

    /**
     * Default swipe menu item click listener.
     */
    private OnSwipeMenuItemClickListener mDefaultMenuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            if (mSwipeMenuItemClickListener != null) {
                mSwipeMenuItemClickListener.onItemClick(closeable, adapterPosition, menuPosition, direction);
            }
        }
    };

    /**
     * open menu on left.
     *
     * @param position position.
     */
    public void openLeftMenu(int position) {
        openMenu(position, LEFT_DIRECTION, SwipeMenuLayout.DEFAULT_SCROLLER_DURATION);
    }

    /**
     * open menu on left.
     *
     * @param position position.
     * @param duration time millis.
     */
    public void openLeftMenu(int position, int duration) {
        openMenu(position, LEFT_DIRECTION, duration);
    }

    /**
     * open menu on right.
     *
     * @param position position.
     */
    public void openRightMenu(int position) {
        openMenu(position, RIGHT_DIRECTION, SwipeMenuLayout.DEFAULT_SCROLLER_DURATION);
    }

    /**
     * open menu on right.
     *
     * @param position position.
     * @param duration time millis.
     */
    public void openRightMenu(int position, int duration) {
        openMenu(position, RIGHT_DIRECTION, duration);
    }

    /**
     * open menu.
     *
     * @param position  position.
     * @param direction use {@link #LEFT_DIRECTION}, {@link #RIGHT_DIRECTION}.
     * @param duration  time millis.
     */
    public void openMenu(int position, int direction, int duration) {
        if (mOldSwipedLayout != null) {
            if (mOldSwipedLayout.isMenuOpen()) {
                mOldSwipedLayout.smoothCloseMenu();
            }
        }
        ViewHolder vh = findViewHolderForAdapterPosition(position);
        if (vh != null) {
            View itemView = getSwipeMenuView(vh.itemView);
            if (itemView != null && itemView instanceof SwipeMenuLayout) {
                mOldSwipedLayout = (SwipeMenuLayout) itemView;
                if (direction == RIGHT_DIRECTION) {
                    mOldTouchedPosition = position;
                    mOldSwipedLayout.smoothOpenRightMenu(duration);
                } else if (direction == LEFT_DIRECTION) {
                    mOldTouchedPosition = position;
                    mOldSwipedLayout.smoothOpenLeftMenu(duration);
                }
            }
        }
    }

    private View getSwipeMenuView(View itemView) {
        if (itemView instanceof SwipeMenuLayout) return itemView;
        List<View> unvisited = new ArrayList<>();
        unvisited.add(itemView);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            if (!(child instanceof ViewGroup)) { // view
                continue;
            }
            if (child instanceof SwipeMenuLayout) return child;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) unvisited.add(group.getChildAt(i));
        }
        return itemView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean isIntercepted = super.onInterceptTouchEvent(e);
        if( isSwiebeEnable) {
            if (e.getPointerCount() > 1) return true;

            int action = e.getAction();
            int x = (int) e.getX();
            int y = (int) e.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = x;
                    mDownY = y;
                    isIntercepted = false;

                    int touchingPosition = getChildAdapterPosition(findChildViewUnder(x, y));
                    if (touchingPosition != mOldTouchedPosition && mOldSwipedLayout != null && mOldSwipedLayout.isMenuOpen()) {
                        mOldSwipedLayout.smoothCloseMenu();
                        isIntercepted = true;
                    }

                    if (isIntercepted) {
                        mOldSwipedLayout = null;
                        mOldTouchedPosition = INVALID_POSITION;
                    } else {
                        ViewHolder vh = findViewHolderForAdapterPosition(touchingPosition);
                        if (vh != null) {
                            View itemView = getSwipeMenuView(vh.itemView);
                            if (itemView != null && itemView instanceof SwipeMenuLayout) {
                                mOldSwipedLayout = (SwipeMenuLayout) itemView;
                                mOldTouchedPosition = touchingPosition;
                            }
                        }
                    }
                    break;
                // They are sensitive to retain sliding and inertia.
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isIntercepted = handleUnDown(x, y, isIntercepted);
                    break;
            }
        }

        return isIntercepted;
    }

    private boolean handleUnDown(int x, int y, boolean defaultValue) {
        int disX = mDownX - x;
        int disY = mDownY - y;
        // swipe
        if (Math.abs(disX) > mViewConfig.getScaledTouchSlop())
            defaultValue = false;
        // click
        if (Math.abs(disY) < mViewConfig.getScaledTouchSlop() && Math.abs(disX) < mViewConfig.getScaledTouchSlop())
            defaultValue = false;
        return defaultValue;
    }
    //method for swipe menu end

    //method for viewpager begin

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            Field fLayoutState = state.getClass().getDeclaredField("mLayoutState");
            fLayoutState.setAccessible(true);
            Object layoutState = fLayoutState.get(state);
            Field fAnchorOffset = layoutState.getClass().getDeclaredField("mAnchorOffset");
            Field fAnchorPosition = layoutState.getClass().getDeclaredField("mAnchorPosition");
            fAnchorPosition.setAccessible(true);
            fAnchorOffset.setAccessible(true);
            if (fAnchorOffset.getInt(layoutState) > 0) {
                fAnchorPosition.set(layoutState, fAnchorPosition.getInt(layoutState) - 1);
            } else if (fAnchorOffset.getInt(layoutState) < 0) {
                fAnchorPosition.set(layoutState, fAnchorPosition.getInt(layoutState) + 1);
            }
            fAnchorOffset.setInt(layoutState, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onRestoreInstanceState(state);
    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout instanceof LinearLayoutManager) {
            reverseLayout = ((LinearLayoutManager) layout).getReverseLayout();
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {

        if(isViewpagerMode) {
            boolean flinging = super.fling((int) (velocityX * mFlingFactor), (int) (velocityY * mFlingFactor));
            if(flinging) {
                if (getLayoutManager().canScrollHorizontally()) {
                    adjustPositionX(velocityX);
                } else {
                    adjustPositionY(velocityY);
                }
            }

            return flinging;
        } else {
            return super.fling(velocityX,velocityY);
        }

    }

    @Override
    public void smoothScrollToPosition(int position) {
        mSmoothScrollTargetPosition = position;
        if (getLayoutManager() != null && getLayoutManager() instanceof LinearLayoutManager && isViewpagerMode) {
            // exclude item decoration
            LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            if (getLayoutManager() == null) {
                                return null;
                            }
                            return ((LinearLayoutManager) getLayoutManager())
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        @Override
                        protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                            if (getLayoutManager() == null) {
                                return;
                            }
                            int dx = calculateDxToMakeVisible(targetView,
                                    getHorizontalSnapPreference());
                            int dy = calculateDyToMakeVisible(targetView,
                                    getVerticalSnapPreference());
                            if (dx > 0) {
                                dx = dx - getLayoutManager()
                                        .getLeftDecorationWidth(targetView);
                            } else {
                                dx = dx + getLayoutManager()
                                        .getRightDecorationWidth(targetView);
                            }
                            if (dy > 0) {
                                dy = dy - getLayoutManager()
                                        .getTopDecorationHeight(targetView);
                            } else {
                                dy = dy + getLayoutManager()
                                        .getBottomDecorationHeight(targetView);
                            }
                            final int distance = (int) Math.sqrt(dx * dx + dy * dy);
                            final int time = calculateTimeForDeceleration(distance);
                            if (time > 0) {
                                action.update(-dx, -dy, time, mDecelerateInterpolator);
                            }
                        }
                    };
            linearSmoothScroller.setTargetPosition(position);
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            getLayoutManager().startSmoothScroll(linearSmoothScroller);
        } else {
            super.smoothScrollToPosition(position);
        }
    }

    @Override
    public void scrollToPosition(int position) {
        mPositionBeforeScroll = getCurrentPosition();
        mSmoothScrollTargetPosition = position;
        super.scrollToPosition(position);

        if(isViewpagerMode) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    if (mSmoothScrollTargetPosition >= 0 && mSmoothScrollTargetPosition < getItemCount()) {
                        if (mOnPageChangedListeners != null) {
                            for (OnPageChangedListener onPageChangedListener : mOnPageChangedListeners) {
                                if (onPageChangedListener != null) {
                                    onPageChangedListener.OnPageChanged(mPositionBeforeScroll, getCurrentPosition());
                                }
                            }
                        }
                    }
                }
            });
        }


    }

    private int getItemCount() {
        return mWrapAdapter.getItemCount();
    }

    /**
     * get item position in center of viewpager
     */
    public int getCurrentPosition() {
        int curPosition;
        if (getLayoutManager().canScrollHorizontally()) {
            curPosition = ViewUtils.getCenterXChildPosition(this);
        } else {
            curPosition = ViewUtils.getCenterYChildPosition(this);
        }
        if (curPosition < 0) {
            curPosition = mSmoothScrollTargetPosition;
        }
        return curPosition;
    }

    /***
     * adjust position before Touch event complete and fling action start.
     */
    protected void adjustPositionX(int velocityX) {
        if (reverseLayout) velocityX *= -1;

        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = ViewUtils.getCenterXChildPosition(this);
            int childWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            int flingCount = getFlingCount(velocityX, childWidth);
            int targetPosition = curPosition + flingCount;
            if (mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount));
                targetPosition = flingCount == 0 ? curPosition : mPositionOnTouchDown + flingCount;

            }
            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, getItemCount() - 1);
            if (targetPosition == curPosition
                    && (!mSinglePageFling || mPositionOnTouchDown == curPosition)) {
                View centerXChild = ViewUtils.getCenterXChild(this);
                if (centerXChild != null) {
                    if (mTouchSpan > centerXChild.getWidth() * mTriggerOffset * mTriggerOffset && targetPosition != 0) {
                        if (!reverseLayout) targetPosition--;
                        else targetPosition++;
                    } else if (mTouchSpan < centerXChild.getWidth() * -mTriggerOffset && targetPosition != getItemCount() - 1) {
                        if (!reverseLayout) targetPosition++;
                        else targetPosition--;
                    }
                }
            }

            smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()));
        }
    }

    public void addOnPageChangedListener(OnPageChangedListener listener) {
        if (mOnPageChangedListeners == null) {
            mOnPageChangedListeners = new ArrayList<>();
        }
        mOnPageChangedListeners.add(listener);
    }

    public void removeOnPageChangedListener(OnPageChangedListener listener) {
        if (mOnPageChangedListeners != null) {
            mOnPageChangedListeners.remove(listener);
        }
    }

    public void clearOnPageChangedListeners() {
        if (mOnPageChangedListeners != null) {
            mOnPageChangedListeners.clear();
        }
    }

    /***
     * adjust position before Touch event complete and fling action start.
     */
    protected void adjustPositionY(int velocityY) {
        if (reverseLayout) velocityY *= -1;

        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = ViewUtils.getCenterYChildPosition(this);
            int childHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            int flingCount = getFlingCount(velocityY, childHeight);
            int targetPosition = curPosition + flingCount;
            if (mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount));
                targetPosition = flingCount == 0 ? curPosition : mPositionOnTouchDown + flingCount;
            }

            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, getItemCount() - 1);
            if (targetPosition == curPosition
                    && (!mSinglePageFling || mPositionOnTouchDown == curPosition)) {
                View centerYChild = ViewUtils.getCenterYChild(this);
                if (centerYChild != null) {
                    if (mTouchSpan > centerYChild.getHeight() * mTriggerOffset && targetPosition != 0) {
                        if (!reverseLayout) targetPosition--;
                        else targetPosition++;
                    } else if (mTouchSpan < centerYChild.getHeight() * -mTriggerOffset && targetPosition != getItemCount() - 1) {
                        if (!reverseLayout) targetPosition++;
                        else targetPosition--;
                    }
                }
            }

            smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (isViewpagerMode && ev.getAction() == MotionEvent.ACTION_DOWN && getLayoutManager() != null) {

            mPositionOnTouchDown = getLayoutManager().canScrollHorizontally()
                    ? ViewUtils.getCenterXChildPosition(this)
                    : ViewUtils.getCenterYChildPosition(this);

        }
        return super.dispatchTouchEvent(ev);
    }

    private int getFlingCount(int velocity, int cellSize) {
        if (velocity == 0) {
            return 0;
        }
        int sign = velocity > 0 ? 1 : -1;
        return (int) (sign * Math.ceil((velocity * sign * mFlingFactor / cellSize)
                - mTriggerOffset));
    }

    private int safeTargetPosition(int position, int count) {
        if (position < 0) {
            return 0;
        }
        if (position >= count) {
            return count - 1;
        }
        return position;
    }

    public interface OnPageChangedListener {
        void OnPageChanged(int oldPosition, int newPosition);
    }

    /**
     * 判断是否Viewpager
     * @param isViewpagerMode
     */
    public void setViewPagerMode(boolean isViewpagerMode) {
        this.isViewpagerMode = isViewpagerMode;
    }
    //method for viewpager end
}
