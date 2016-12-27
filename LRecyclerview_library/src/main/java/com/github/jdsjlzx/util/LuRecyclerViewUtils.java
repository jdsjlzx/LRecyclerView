package com.github.jdsjlzx.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;

/**
 *
 * @author lizhixian
 * @time 16/9/10 09:54
 */
public class LuRecyclerViewUtils {

    /**
     * 设置HeaderView
     *
     * @param recyclerView
     * @param view
     */
    @Deprecated
    public static void setHeaderView(RecyclerView recyclerView, View view) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof LuRecyclerViewAdapter)) {
            return;
        }

        LuRecyclerViewAdapter luRecyclerViewAdapter = (LuRecyclerViewAdapter) outerAdapter;
        luRecyclerViewAdapter.addHeaderView(view);
    }

    /**
     * 设置FooterView
     *
     * @param recyclerView
     * @param view
     */
    @Deprecated
    public static void setFooterView(RecyclerView recyclerView, View view) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof LuRecyclerViewAdapter)) {
            return;
        }

        LuRecyclerViewAdapter luRecyclerViewAdapter = (LuRecyclerViewAdapter) outerAdapter;

        if (luRecyclerViewAdapter.getFooterViewsCount() > 0) {
            luRecyclerViewAdapter.removeFooterView();
        }
        luRecyclerViewAdapter.addFooterView(view);
    }

    /**
     * 移除FooterView
     *
     * @param recyclerView
     */
    public static void removeFooterView(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter != null && outerAdapter instanceof LuRecyclerViewAdapter) {

            int footerViewCounter = ((LuRecyclerViewAdapter) outerAdapter).getFooterViewsCount();
            if (footerViewCounter > 0) {
                ((LuRecyclerViewAdapter) outerAdapter).removeFooterView();
            }
        }
    }

    /**
     * 移除HeaderView
     *
     * @param recyclerView
     */
    public static void removeHeaderView(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter != null && outerAdapter instanceof LuRecyclerViewAdapter) {

            int headerViewCounter = ((LuRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
            if (headerViewCounter > 0) {
                View headerView = ((LuRecyclerViewAdapter) outerAdapter).getHeaderView();
                ((LuRecyclerViewAdapter) outerAdapter).removeHeaderView(headerView);
            }
        }
    }

    /**
     * 请使用本方法替代RecyclerView.ViewHolder的getLayoutPosition()方法
     *
     * @param recyclerView
     * @param holder
     * @return
     */
    public static int getLayoutPosition(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof LuRecyclerViewAdapter) {

            int headerViewCounter = ((LuRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
            if (headerViewCounter > 0) {
                return holder.getLayoutPosition() - headerViewCounter;
            }
        }

        return holder.getLayoutPosition();
    }

    /**
     * 请使用本方法替代RecyclerView.ViewHolder的getAdapterPosition()方法
     *
     * @param recyclerView
     * @param holder
     * @return
     */
    public static int getAdapterPosition(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof LuRecyclerViewAdapter) {

            int headerViewCounter = ((LuRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
            if (headerViewCounter > 0) {
                return holder.getAdapterPosition() - headerViewCounter;
            }
        }

        return holder.getAdapterPosition();
    }
}