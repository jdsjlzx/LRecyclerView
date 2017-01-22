package com.lzx.demo.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.github.jdsjlzx.interfaces.IRefreshHeader;
import com.github.jdsjlzx.recyclerview.AppBarStateChangeListener;
import com.github.jdsjlzx.view.ArrowRefreshHeader;


/**
 * 自定义下拉刷新ScrollView
 * <p/>
 */
public class PullScrollView extends ScrollView {
    private RefreshListener mRefreshListener;

    private IRefreshHeader mRefreshHeader;
    private boolean isRefreshEnabled = true;    //设置下拉刷新是否可用
    private float   dragRate         = 2;       //下拉刷新滑动阻力系数，越大需要手指下拉的距离越大才能刷新

    private boolean isRefreshing;   //是否正在刷新
    private float mLastY = -1;      //上次触摸的的Y值
    private int     topY;
    private float   sumOffSet;
    private boolean isAdded;

    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;

    public PullScrollView(Context context) {
        this(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isRefreshEnabled) {
            mRefreshHeader = new ArrowRefreshHeader(getContext());
        }
    }

    private void setLayout() {
        if (!isAdded) {
            isAdded = true;

            ViewGroup group = (ViewGroup) getParent();
            LinearLayout container = new LinearLayout(getContext());
            container.setOrientation(LinearLayout.VERTICAL);
            int index = group.indexOfChild(this);
            group.removeView(this);
            group.addView(container, index, getLayoutParams());
            container.addView(mRefreshHeader.getHeaderView(), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            container.addView(this, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public View getRefreshHeaderView() {
        return mRefreshHeader.getHeaderView();
    }

    /**
     * 设置下拉刷新上拉加载回调
     */
    public void setRefreshListener(RefreshListener listener) {
        mRefreshListener = listener;
    }

    /**
     * 设置自定义的header
     */
    public void setRefreshHeader(IRefreshHeader mRefreshHeader) {
        this.mRefreshHeader = mRefreshHeader;
    }

    /**
     * 下拉刷新是否可用
     */
    public void setPullRefreshEnabled(boolean enabled) {
        isRefreshEnabled = enabled;
    }

    /**
     * 下拉刷新滑动阻力系数，越大需要手指下拉的距离越大才能刷新
     */
    public void setDragRate(int dragRate) {
        this.dragRate = dragRate;
    }

    /**
     * 设置下拉刷新的进度条风格
     */
    public void setRefreshProgressStyle(int style) {
        if (mRefreshHeader != null && mRefreshHeader instanceof ArrowRefreshHeader) {
            ((ArrowRefreshHeader) mRefreshHeader).setProgressStyle(style);
        }
    }

    /**
     * 设置下拉刷新的箭头图标
     */
    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null && mRefreshHeader instanceof ArrowRefreshHeader) {
            ((ArrowRefreshHeader) mRefreshHeader).setArrowImageView(resId);
        }
    }

    /**
     * 设置颜色
     * @param indicatorColor Only call the method setRefreshProgressStyle(int style) to take effect
     * @param hintColor
     * @param backgroundColor
     */
    public void setHeaderViewColor(int indicatorColor, int hintColor, int backgroundColor) {
        if (mRefreshHeader != null && mRefreshHeader instanceof ArrowRefreshHeader) {
            ArrowRefreshHeader arrowRefreshHeader = ((ArrowRefreshHeader) mRefreshHeader);
            arrowRefreshHeader.setIndicatorColor(ContextCompat.getColor(getContext(),indicatorColor));
            arrowRefreshHeader.setHintTextColor(hintColor);
            arrowRefreshHeader.setViewBackgroundColor(backgroundColor);
        }

    }


    /**
     * 手动调用直接刷新，无下拉效果
     */
    public void refresh() {
        if (mRefreshListener != null) {
            isRefreshing = true;
            mRefreshListener.onRefresh();
        }
    }

    /**
     * 手动调用下拉刷新，有下拉效果
     */
    public void refreshWithPull() {
        setRefreshing(true);
        refresh();
    }

    /**
     * 下拉刷新和到底加载完成
     */
    public void setRefreshCompleted() {
        if (isRefreshing) {
            isRefreshing = false;
            mRefreshHeader.refreshComplete();
        }
    }

    /**
     * 手动调用加载状态，此函数不会调用 {@link RefreshListener#onRefresh()}加载数据
     * 如果需要加载数据和状态显示调用 {@link #refreshWithPull()}
     */
    public void setRefreshing(final boolean refreshing) {
        if (refreshing && isRefreshEnabled) {
            isRefreshing = true;
            mRefreshHeader.onRefreshing();

            int offSet = mRefreshHeader.getHeaderView()
                                      .getMeasuredHeight();
            mRefreshHeader.onMove(offSet, offSet);
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
                sumOffSet = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = (ev.getRawY() - mLastY) / dragRate;
                mLastY = ev.getRawY();
                sumOffSet += deltaY;
                if (isOnTop() && isRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader.onMove(deltaY, sumOffSet);
                    if (mRefreshHeader.getVisibleHeight() > 0 && !isRefreshing) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1; // reset
                if (isOnTop() && isRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.onRelease()) {
                        if (mRefreshListener != null) {
                            isRefreshing = true;
                            mRefreshListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        topY = t;
    }

    /**
     * 如果在HeaderView已经被添加到布局中，说明已经到顶部
     */
    private boolean isOnTop() {
        return topY == 0;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //解决和AppBarLayout冲突的问题
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }

        if (p != null) {
            AppBarLayout appBarLayout = null;
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }

            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
        setLayout();
    }

    public interface RefreshListener {
        void onRefresh();
    }
}