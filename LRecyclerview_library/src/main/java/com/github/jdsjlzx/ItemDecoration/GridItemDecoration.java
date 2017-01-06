package com.github.jdsjlzx.ItemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Paint mPaint;

    public GridItemDecoration(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        LRecyclerView recyclerView = (LRecyclerView) parent;
        LRecyclerViewAdapter adapter = (LRecyclerViewAdapter) parent.getAdapter();
        for (int i = 0; i < childCount; i++) {
            if ((recyclerView.isOnTop() && (adapter.isHeader(i) || adapter.isRefreshHeader(i))) || adapter.isFooter(i)) {
                Log.d("horizontal---no-->", String.valueOf(i)+"----"+childCount);
                c.drawRect(0, 0, 0, 0, mPaint);
            } else {
                Log.d("horizontal---yes-->", String.valueOf(i));
                final View child = parent.getChildAt(i);
                final int top = child.getBottom();
                final int bottom = top + 20;
                int left = child.getLeft();
                int right = child.getRight();
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        LRecyclerView recyclerView = (LRecyclerView) parent;
        LRecyclerViewAdapter adapter = (LRecyclerViewAdapter) parent.getAdapter();
        for (int i = 0; i < childCount; i++) {
            if ((recyclerView.isOnTop() && (adapter.isHeader(i) || adapter.isRefreshHeader(i))) || adapter.isFooter(i)) {
                c.drawRect(0, 0, 0, 0, mPaint);
            } else {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getTop();
                //final int bottom = child.getBottom() + params.bottomMargin;
                final int bottom = child.getBottom() + 20; //这里使用verticalSpacing 代替 params// .bottomMargin
                final int left = child.getRight() + params.rightMargin;
                final int right = left + 20;
                c.drawRect(left, top, right, bottom, mPaint);
            }

        }
    }

    /**
     * @param parent     RecyclerView
     * @param pos        当前item的位置
     * @param spanCount  每行显示的item个数
     * @param childCount child个数
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int leftCount = childCount - childCount % spanCount;//3
            Log.d("left--->", String.valueOf(leftCount));
            //leftCount:若childCount能被span整除为childCount否则为去掉最后一行的item总数
            if (pos > leftCount)// 如果是最后一行，则不需要绘制底部
            {
                Log.d("no_draw------->", String.valueOf(pos));
                return true;
            }
        }
        return false;
    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int total) {
        Log.d("column---->", pos + "----" + spanCount);
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (pos % spanCount == 0 || pos == total)// 如果是最后一列，则不需要绘制右边
                return true;
        }
        return false;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        Log.d("position------->", String.valueOf(itemPosition));
        LRecyclerViewAdapter adapter = (LRecyclerViewAdapter) parent.getAdapter();
        if (adapter.isFooter(itemPosition)|| adapter.isHeader(itemPosition)||adapter
                .isRefreshHeader(itemPosition)) {
            outRect.set(0, 0, 0, 0);
        } else {
            if (!(parent.getLayoutManager() instanceof GridLayoutManager)) {
                Log.d("manager--------->", "LinearLayoutManager: ");
                if (itemPosition == (childCount - 2))
                    outRect.set(0, 0, 0, 0);
                else
                    outRect.set(0, 0, 0, 20);
            } else {
                Log.d("manager--------->", "GridManager: ");
                if (isLastRaw(parent, itemPosition, spanCount, childCount - 2) && isLastColumn(parent, itemPosition, spanCount, childCount - 2)) {
                    outRect.set(0, 0, 20, 20);
                    return;
                }
//                if (isLastRaw(parent, itemPosition, spanCount, childCount - 2))// 最后一行，无需绘制底部
//                {
//                    outRect.set(0, 0, 20, 0);
//                    return;
//                }
                if (isLastColumn(parent, itemPosition, spanCount, childCount - 2))//最后一列无需绘制右边
                {
                    outRect.set(0, 0, 0, 20);
                    return;
                }
                outRect.set(0, 0, 20, 20);

            }

        }

    }
}
