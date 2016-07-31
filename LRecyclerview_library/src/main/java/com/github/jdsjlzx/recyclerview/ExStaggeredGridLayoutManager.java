package com.github.jdsjlzx.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by cundong on 2015/10/9.
 * <p/>
 * 拓展的StaggeredGridLayoutManager，tks @Jack Tony
 */
public class ExStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private final String TAG = getClass().getSimpleName();
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    GridLayoutManager.SpanSizeLookup mSpanSizeLookup;

    public ExStaggeredGridLayoutManager(int spanCount, int orientation,LRecyclerViewAdapter adapter) {
        super(spanCount, orientation);
        this.mLRecyclerViewAdapter = adapter;
    }

    /**
     * Returns the current used by the GridLayoutManager.
     *
     * @return The current used by the GridLayoutManager.
     */
    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return mSpanSizeLookup;
    }

    /**
     * 设置某个位置的item的跨列程度，这里和GridLayoutManager有点不一样，
     * 如果你设置某个位置的item的span>1了，那么这个item会占据所有列
     *
     * @param spanSizeLookup instance to be used to query number of spans
     *                       occupied by each item
     */
    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int itemCount = mLRecyclerViewAdapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            Log.d(TAG, "lookup  i = " + i + " itemCount = " + itemCount);
            Log.e(TAG,"mSpanSizeLookup.getSpanSize(i) " + mSpanSizeLookup.getSpanSize(i));
            /*if (mSpanSizeLookup.getSpanSize(i) > 1) {

                View view = recycler.getViewForPosition(i);
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }*/

        }
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
    }

}