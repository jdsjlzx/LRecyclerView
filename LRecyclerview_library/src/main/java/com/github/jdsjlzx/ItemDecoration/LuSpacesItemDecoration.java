package com.github.jdsjlzx.ItemDecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;

/**
 * Adds spaces (between) between Item views.
 *
 * Supports GridLayoutManager and LinearLayoutManager. Extend this class and override the
 * {@link #getSpanLookup(View, RecyclerView)} method to support other
 * LayoutManagers.
 *
 * Currently only supports LayoutManagers in VERTICAL orientation.
 */
public class LuSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int itemSplitMarginEven;
    private final int itemSplitMarginLarge;
    private final int itemSplitMarginSmall;

    private final int verticalSpacing;

    private Paint mPaint;

    public static LuSpacesItemDecoration newInstance(int horizontalSpacing, int verticalSpacing, int spanCount, int color) {
        int maxNumberOfSpaces = spanCount - 1;
        int totalSpaceToSplitBetweenItems = maxNumberOfSpaces * horizontalSpacing;

        int itemSplitMarginEven = (int) (0.5f * horizontalSpacing);
        int itemSplitMarginLarge = totalSpaceToSplitBetweenItems / spanCount;
        int itemSplitMarginSmall = horizontalSpacing - itemSplitMarginLarge;

        return new LuSpacesItemDecoration(itemSplitMarginEven, itemSplitMarginLarge, itemSplitMarginSmall, verticalSpacing, color);
    }

    private LuSpacesItemDecoration(int itemSplitMarginEven, int itemSplitMarginLarge, int itemSplitMarginSmall, int verticalSpacing, int color) {
        this.itemSplitMarginEven = itemSplitMarginEven;
        this.itemSplitMarginLarge = itemSplitMarginLarge;
        this.itemSplitMarginSmall = itemSplitMarginSmall;
        this.verticalSpacing = verticalSpacing;
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.Adapter adapter = parent.getAdapter();

        LuRecyclerViewAdapter LuRecyclerViewAdapter;
        if (adapter instanceof LuRecyclerViewAdapter) {
            LuRecyclerViewAdapter = (LuRecyclerViewAdapter) adapter;
        } else {
            throw new RuntimeException("the adapter must be LuRecyclerViewAdapter");
        }

        drawHorizontal(c,parent,LuRecyclerViewAdapter);
        drawVertical(c,parent,LuRecyclerViewAdapter);
    }

    public void drawHorizontal(Canvas c, RecyclerView parent,LuRecyclerViewAdapter adapter) {
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final int top = child.getBottom();
            final int bottom = top + verticalSpacing;

            int left = child.getLeft() ;
            int right = child.getRight();

            int position = parent.getChildAdapterPosition(child);

            c.save();

            if (adapter. isHeader(position) || adapter.isFooter(position)) {
                c.drawRect(0, 0, 0, 0, mPaint);
            }else {
                c.drawRect(left, top, right, bottom, mPaint);
            }

            c.restore();
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent,LuRecyclerViewAdapter adapter) {
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop();
            //final int bottom = child.getBottom() + params.bottomMargin;
            final int bottom = child.getBottom() + verticalSpacing; //这里使用verticalSpacing 代替 params.bottomMargin
            final int left = child.getRight() + params.rightMargin;
            final int right = left + itemSplitMarginEven*2;

            int position = parent.getChildAdapterPosition(child);

            c.save();

            if (adapter. isHeader(position) || adapter.isFooter(position)) {
                c.drawRect(0, 0, 0, 0, mPaint);
            }else {
                c.drawRect(left, top, right, bottom, mPaint);
            }

            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();

        LuRecyclerViewAdapter LuRecyclerViewAdapter;
        if (adapter instanceof LuRecyclerViewAdapter) {
            LuRecyclerViewAdapter = (LuRecyclerViewAdapter) adapter;
        } else {
            throw new RuntimeException("the adapter must be LuRecyclerViewAdapter");
        }

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewLayoutPosition();
        int childCount = parent.getAdapter().getItemCount();

        SpanLookup spanLookup = getSpanLookup(view, parent);
        applyItemHorizontalOffsets(spanLookup, itemPosition, outRect);
        applyItemVerticalOffsets(outRect, itemPosition, childCount, spanLookup.getSpanCount(), spanLookup,LuRecyclerViewAdapter);
    }

    protected SpanLookup getSpanLookup(View view, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return SpanLookupFactory.gridLayoutSpanLookup((GridLayoutManager) layoutManager);
        }
        return SpanLookupFactory.singleSpan();
    }

    private void applyItemVerticalOffsets(Rect outRect, int itemPosition, int childCount, int spanCount, SpanLookup spanLookup,LuRecyclerViewAdapter adapter) {
        outRect.top = getItemTopSpacing(spanLookup, verticalSpacing, itemPosition, spanCount, childCount,adapter);
        outRect.bottom = getItemBottomSpacing(spanLookup, verticalSpacing, itemPosition, childCount,adapter);
    }

    private void applyItemHorizontalOffsets(SpanLookup spanLookup, int itemPosition, Rect offsets) {
        if (itemIsFullSpan(spanLookup, itemPosition)) {
            offsets.left = 0;
            offsets.right = 0;
            return;
        }

        if (itemStartsAtTheLeftEdge(spanLookup, itemPosition)) {
            offsets.left = 0;
            offsets.right = itemSplitMarginLarge;
            return;
        }

        if (itemEndsAtTheRightEdge(spanLookup, itemPosition)) {
            offsets.left = itemSplitMarginLarge;
            offsets.right = 0;
            return;
        }

        if (itemIsNextToAnItemThatStartsOnTheLeftEdge(spanLookup, itemPosition)) {
            offsets.left = itemSplitMarginSmall;
        } else {
            offsets.left = itemSplitMarginEven;
        }

        if (itemIsNextToAnItemThatEndsOnTheRightEdge(spanLookup, itemPosition)) {
            offsets.right = itemSplitMarginSmall;
        } else {
            offsets.right = itemSplitMarginEven;
        }
    }

    private static boolean itemIsNextToAnItemThatStartsOnTheLeftEdge(SpanLookup spanLookup, int itemPosition) {
        return !itemStartsAtTheLeftEdge(spanLookup, itemPosition) && itemStartsAtTheLeftEdge(spanLookup, itemPosition - 1);
    }

    private static boolean itemIsNextToAnItemThatEndsOnTheRightEdge(SpanLookup spanLookup, int itemPosition) {
        return !itemEndsAtTheRightEdge(spanLookup, itemPosition) && itemEndsAtTheRightEdge(spanLookup, itemPosition + 1);
    }

    private static boolean itemIsFullSpan(SpanLookup spanLookup, int itemPosition) {
        return itemStartsAtTheLeftEdge(spanLookup, itemPosition) && itemEndsAtTheRightEdge(spanLookup, itemPosition);
    }

    private static boolean itemStartsAtTheLeftEdge(SpanLookup spanLookup, int itemPosition) {
        return spanLookup.getSpanIndex(itemPosition) == 0;
    }

    private static boolean itemEndsAtTheRightEdge(SpanLookup spanLookup, int itemPosition) {
        return spanLookup.getSpanIndex(itemPosition) + spanLookup.getSpanSize(itemPosition) == spanLookup.getSpanCount();
    }

    private static int getItemTopSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int spanCount, int childCount, LuRecyclerViewAdapter adapter) {
        if(adapter.isHeader(itemPosition) || adapter.isFooter(itemPosition)) {
            return 0;
        } else {
            if (itemIsOnTheTopRow(spanLookup, itemPosition, spanCount, childCount)) {
                return 0;
            } else {
                return (int) (.5f * verticalSpacing);
            }
        }

    }

    private static boolean itemIsOnTheTopRow(SpanLookup spanLookup, int itemPosition, int spanCount, int childCount) {
        int latestCheckedPosition = 0;
        for (int i = 0; i < childCount; i++) {
            latestCheckedPosition = i;
            int spanEndIndex = spanLookup.getSpanIndex(i) + spanLookup.getSpanSize(i) - 1;
            if (spanEndIndex == spanCount - 1) {
                break;
            }
        }
        return itemPosition <= latestCheckedPosition;
    }

    private static int getItemBottomSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int childCount, LuRecyclerViewAdapter adapter) {

        if(adapter.isHeader(itemPosition) || adapter.isFooter(itemPosition)) {
            return 0;
        } else {
            if (itemIsOnTheBottomRow(spanLookup, itemPosition, childCount)) {
                return 0;
            } else {
                return (int) (.5f * verticalSpacing);
            }
        }

    }

    private static boolean itemIsOnTheBottomRow(SpanLookup spanLookup, int itemPosition, int childCount) {
        int latestCheckedPosition = 0;
        for (int i = childCount - 1; i >= 0; i--) {
            latestCheckedPosition = i;
            int spanIndex = spanLookup.getSpanIndex(i);
            if (spanIndex == 0) {
                break;
            }
        }
        return itemPosition >= latestCheckedPosition;
    }

}
