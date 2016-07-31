package com.lzx.demo.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ViewGroup;

public class FullSpanUtil {

    public static void onAttachedToRecyclerView(RecyclerView recyclerView, final RecyclerView.Adapter adapter, final int pinnedHeaderType) {
        Log.e("lzx","FullSpanUtil gonAttachedToRecyclerView");

        // 如果是网格布局，这里处理标签的布局占满一行
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (adapter.getItemViewType(position) == pinnedHeaderType) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    public static void onViewAttachedToWindow(RecyclerView.ViewHolder holder, RecyclerView.Adapter adapter, int pinnedHeaderType) {
        // 如果是瀑布流布局，这里处理标签的布局占满一行
        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            final StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) lp;
            slp.setFullSpan(adapter.getItemViewType(holder.getLayoutPosition()) == pinnedHeaderType);
        }
}


}
