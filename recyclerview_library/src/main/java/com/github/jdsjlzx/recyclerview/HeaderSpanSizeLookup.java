package com.github.jdsjlzx.recyclerview;


import android.support.v7.widget.GridLayoutManager;

/**
 * Created by lizhixian.
 * <p/>
 * RecyclerView为GridLayoutManager时，设置了HeaderView，就会用到这个SpanSizeLookup
 */
public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private LRecyclerViewAdapter adapter;
    private int mSpanSize = 1;

    public HeaderSpanSizeLookup(LRecyclerViewAdapter adapter, int spanSize) {
        this.adapter = adapter;
        this.mSpanSize = spanSize;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = adapter.isRefreshHeader(position) || adapter.isHeader(position) || adapter.isFooter(position);
        return isHeaderOrFooter ? mSpanSize : 1;
    }
}