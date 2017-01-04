package com.github.jdsjlzx.recyclerview;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.view.LoadingFooter;

/**
 *
 * @author Lzx
 * @created 2016/9/9 16:45
 *
 */
public class LuRecyclerView extends RecyclerView {
    private boolean mLoadMoreEnabled = true;
    private boolean flag = false;//标记是否setAdapter
    private boolean isNoMore = false;
    private LScrollListener mLScrollListener;
    private OnLoadMoreListener mLoadMoreListener;
    private View mEmptyView;
    private View mFootView;
    private final AdapterDataObserver mDataObserver = new DataObserver();

    private LuRecyclerViewAdapter mWrapAdapter;

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


    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;

    public LuRecyclerView(Context context) {
        this(context, null);
    }

    public LuRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LoadingFooter footView = new LoadingFooter(getContext().getApplicationContext());
        mFootView = footView;
        mFootView.setVisibility(GONE);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = (LuRecyclerViewAdapter) adapter;
        super.setAdapter(mWrapAdapter);

        if(flag) {
            mWrapAdapter.getInnerAdapter().unregisterAdapterDataObserver(mDataObserver);
        }
        mWrapAdapter.getInnerAdapter().registerAdapterDataObserver(mDataObserver);
        flag = true;

        mDataObserver.onChanged();

        if(mLoadMoreEnabled) {
            mWrapAdapter.addFooterView(mFootView);
        }

    }

    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();

            if (adapter instanceof LRecyclerViewAdapter) {
                LRecyclerViewAdapter headerAndFooterAdapter = (LRecyclerViewAdapter) adapter;
                if (headerAndFooterAdapter.getInnerAdapter() != null && mEmptyView != null) {
                    int count = headerAndFooterAdapter.getInnerAdapter().getItemCount();
                    if (count == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                        LuRecyclerView.this.setVisibility(View.GONE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                        LuRecyclerView.this.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (adapter != null && mEmptyView != null) {
                    if (adapter.getItemCount() == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                        LuRecyclerView.this.setVisibility(View.GONE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                        LuRecyclerView.this.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart + mWrapAdapter.getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart + mWrapAdapter.getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart + mWrapAdapter.getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int headerViewsCountCount = mWrapAdapter.getHeaderViewsCount();
            mWrapAdapter.notifyItemRangeChanged(fromPosition + headerViewsCountCount , toPosition + headerViewsCountCount + itemCount);
        }

    }

    public void setNoMore(boolean noMore){
        isNoMore = noMore;
        if(isNoMore) {
            setFooterViewState(LoadingFooter.State.NoMore,true);
        } else {
            setFooterViewState(LoadingFooter.State.Normal,true);
        }
    }

    public void loadMoreComplete() {
        setFooterViewState(LoadingFooter.State.Normal,false);
    }

    private void setFooterViewState(LoadingFooter.State state,boolean isScroolUp) {

        if (mFootView instanceof LoadingFooter) {
            mFootView.setVisibility(VISIBLE);
            ((LoadingFooter) mFootView).setState(state);
        }
        if(isScroolUp) {
            scrollToPosition(mWrapAdapter.getItemCount() - 1);
        }

    }

    public void setOnNetWorkErrorListener(final OnNetWorkErrorListener listener) {
        final LoadingFooter loadingFooter = ((LoadingFooter) mFootView);
        loadingFooter.setState(LoadingFooter.State.NetWorkError);
        loadingFooter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFooterViewState(LoadingFooter.State.Loading,false);
                listener.reload();
            }
        });
    }

    public void setFootViewHint(String loading, String noMore,String noNetWork) {
        if(mFootView instanceof LoadingFooter){
            LoadingFooter loadingFooter = ((LoadingFooter) mFootView);
            loadingFooter.setLoadingHint(loading);
            loadingFooter.setNoMoreHint(noMore);
            loadingFooter.setNoNetWorkHint(noNetWork);
        }
    }

    public void setLoadingMoreProgressStyle(int style) {
        if (mFootView instanceof LoadingFooter) {
            ((LoadingFooter) mFootView).setProgressStyle(style);
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

    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreEnabled = enabled;
        if (!enabled) {
            if(mWrapAdapter.getFooterViewsCount() > 0) {
                mFootView = mWrapAdapter.getFooterView();
            }
            if (mFootView instanceof LoadingFooter && null != mWrapAdapter) {
                mWrapAdapter.removeFooterView();
            } else {
                mFootView.setVisibility(VISIBLE);
            }
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public void setLScrollListener(LScrollListener listener) {
        mLScrollListener = listener;
    }

    public interface LScrollListener {

        void onScrollUp();//scroll down to up

        void onScrollDown();//scroll from up to down

        void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance

        void onScrollStateChanged(int state);
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        int firstVisibleItemPosition = 0;
        LayoutManager layoutManager = getLayoutManager();

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
        if (null != mLScrollListener) {
            mLScrollListener.onScrolled(mScrolledXDistance, mScrolledYDistance);
        }

    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        int currentScrollState = state;

        if (mLScrollListener != null) {
            mLScrollListener.onScrollStateChanged(state);
        }

        if (mLoadMoreListener != null && mLoadMoreEnabled) {

            if (currentScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0
                        && lastVisibleItemPosition >= totalItemCount - 1
                        && totalItemCount > visibleItemCount ) {
                    mLoadMoreListener.onLoadMore();

                }

            }
        }

    }

    /**
     * 计算当前是向上滑动还是向下滑动
     */
    private void calculateScrollUpOrDown(int firstVisibleItemPosition, int dy) {
        if (mLScrollListener != null) {
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
        }

        if ((mIsScrollDown && dy > 0) || (!mIsScrollDown && dy < 0)) {
            mDistance += dy;
        }
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

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if(p!=null && p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if(child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout)child;
                    break;
                }
            }
            if(appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

}
