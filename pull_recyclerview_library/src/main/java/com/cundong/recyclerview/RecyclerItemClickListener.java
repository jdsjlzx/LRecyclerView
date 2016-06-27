package com.cundong.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView item点击和长按事件
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mListener;

    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && mListener != null) {
                    int adjPosition = recyclerView.getChildAdapterPosition(childView);
                    RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
                    if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
                        int headerViewCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
                        int footerViewsCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterViewsCount();
                        int count = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getItemCount();
                        if (headerViewCounter > 0) {
                            adjPosition = adjPosition - headerViewCounter;
                            if ( adjPosition > -1 && (adjPosition < count - headerViewCounter - footerViewsCounter)) {
                                mListener.onItemLongClick(childView, adjPosition);
                            }
                        }

                    }
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            int adjPosition = recyclerView.getChildAdapterPosition(childView);
            RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
            if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
                int headerViewCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
                int footerViewsCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterViewsCount();
                int count = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getItemCount();
                if (headerViewCounter > 0) {
                    adjPosition = adjPosition - headerViewCounter;
                    if ( adjPosition > -1 && (adjPosition < count - headerViewCounter - footerViewsCounter)) {
                        mListener.onItemClick(childView, adjPosition);
                    }
                }

            }

        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
