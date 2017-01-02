package com.github.jdsjlzx.util;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.view.LoadingFooter;

/**
 * 分页展示数据时，RecyclerView的FooterView State 操作工具类
 * RecyclerView一共有几种State：Normal/Loading/Error/TheEnd
 * @author lizhixian
 * @time 16/9/10 09:56
 */
@Deprecated
public class RecyclerViewStateUtils {

    /**
     * 设置LRecyclerViewAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param pageSize      分页展示时，recyclerView每一页的数量
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState(Activity instance, RecyclerView recyclerView, int pageSize, LoadingFooter.State state, View.OnClickListener errorListener) {
        if (instance == null || instance.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof LRecyclerViewAdapter)) {
            return;
        }

        LRecyclerViewAdapter lRecyclerViewAdapter = (LRecyclerViewAdapter) outerAdapter;

        //只有一页，不显示FooterView
        if (lRecyclerViewAdapter.getInnerAdapter().getItemCount() < pageSize) {
            return;
        }

        LoadingFooter footerView;
        //已经有footerView了
        if (lRecyclerViewAdapter.getFooterViewsCount() > 0) {
            footerView = (LoadingFooter)lRecyclerViewAdapter.getFooterView();
            footerView.setState(state);
            footerView.setVisibility(View.VISIBLE);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            } else if (state == LoadingFooter.State.NoMore){
                ((LRecyclerView)recyclerView).setNoMore(true);
            }

        }
        recyclerView.scrollToPosition(lRecyclerViewAdapter.getItemCount() - 1);

    }

    /**
     * 设置LRecyclerViewAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param pageSize      分页展示时，recyclerView每一页的数量
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState(Fragment instance, RecyclerView recyclerView, int pageSize, LoadingFooter.State state, View.OnClickListener errorListener) {
        if (instance == null || instance.isDetached()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof LRecyclerViewAdapter)) {
            return;
        }

        LRecyclerViewAdapter lRecyclerViewAdapter = (LRecyclerViewAdapter) outerAdapter;

        //只有一页，不显示FooterView
        if (lRecyclerViewAdapter.getInnerAdapter().getItemCount() < pageSize) {
            return;
        }

        LoadingFooter footerView;
        //已经有footerView了
        if (lRecyclerViewAdapter.getFooterViewsCount() > 0) {
            footerView = (LoadingFooter)lRecyclerViewAdapter.getFooterView();
            footerView.setState(state);
            footerView.setVisibility(View.VISIBLE);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            } else if (state == LoadingFooter.State.NoMore){
                ((LRecyclerView)recyclerView).setNoMore(true);
            }

        }
        recyclerView.scrollToPosition(lRecyclerViewAdapter.getItemCount() - 1);

    }

    /**
     * 获取当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     */
    public static LoadingFooter.State getFooterViewState(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof LRecyclerViewAdapter) {
            if (((LRecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((LRecyclerViewAdapter) outerAdapter).getFooterView();
                return footerView.getState();
            }
        }

        return LoadingFooter.State.Normal;
    }

    /**
     * 设置当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     * @param state
     */
    public static void setFooterViewState(RecyclerView recyclerView, LoadingFooter.State state) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof LRecyclerViewAdapter) {
            if (((LRecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((LRecyclerViewAdapter) outerAdapter).getFooterView();
                footerView.setState(state);
            }
        }
    }
}
