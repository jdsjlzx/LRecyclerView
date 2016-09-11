package com.github.jdsjlzx.util;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.view.LoadingFooter;

/**
 * 分页展示数据时，RecyclerView的FooterView State 操作工具类
 * RecyclerView一共有几种State：Normal/Loading/Error/TheEnd
 * @author lizhixian
 * @time 16/9/10 09:56
 */
public class LuRecyclerViewStateUtils {

    /**
     * 设置LuRecyclerViewAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param pageSize      分页展示时，recyclerView每一页的数量
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState(Activity instance, RecyclerView recyclerView, int pageSize, LoadingFooter.State state, View.OnClickListener errorListener) {
        if(instance==null || instance.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof LuRecyclerViewAdapter)) {
            return;
        }

        LuRecyclerViewAdapter LuRecyclerViewAdapter = (LuRecyclerViewAdapter) outerAdapter;

        //只有一页的时候，就别加什么FooterView了
        if (LuRecyclerViewAdapter.getInnerAdapter().getItemCount() < pageSize) {
            return;
        }

        LoadingFooter footerView;
        //已经有footerView了
        if (LuRecyclerViewAdapter.getFooterViewsCount() > 0) {
            footerView = (LoadingFooter) LuRecyclerViewAdapter.getFooterView();
            footerView.setState(state);
            footerView.setVisibility(View.VISIBLE);
            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }

        }

    }

    /**
     * 设置LuRecyclerViewAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param pageSize      分页展示时，recyclerView每一页的数量
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState2(Activity instance, RecyclerView recyclerView, int pageSize, LoadingFooter.State state, View.OnClickListener errorListener) {

        if(instance==null || instance.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof LuRecyclerViewAdapter)) {
            return;
        }

        LuRecyclerViewAdapter LuRecyclerViewAdapter = (LuRecyclerViewAdapter) outerAdapter;

        LoadingFooter footerView;

        //已经有footerView了
        if (LuRecyclerViewAdapter.getFooterViewsCount() > 0) {
            footerView = (LoadingFooter) LuRecyclerViewAdapter.getFooterView();
            footerView.setState(state);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }
            recyclerView.scrollToPosition(0);
        } else {
            footerView = new LoadingFooter(instance);
            footerView.setState(state);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }

            LuRecyclerViewAdapter.addFooterView(footerView);
            recyclerView.scrollToPosition(0);
        }
    }

    /**
     * 获取当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     */
    public static LoadingFooter.State getFooterViewState(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof LuRecyclerViewAdapter) {
            if (((LuRecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((LuRecyclerViewAdapter) outerAdapter).getFooterView();
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
        if (outerAdapter != null && outerAdapter instanceof LuRecyclerViewAdapter) {
            if (((LuRecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((LuRecyclerViewAdapter) outerAdapter).getFooterView();
                footerView.setState(state);
            }
        }
    }
}
