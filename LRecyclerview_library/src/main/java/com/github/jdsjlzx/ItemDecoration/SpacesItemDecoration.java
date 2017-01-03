package com.github.jdsjlzx.ItemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Adds spaces (between) between Item views.
 *
 * Supports GridLayoutManager and LinearLayoutManager. Extend this class and override the
 * {@link #getSpanLookup(android.view.View, android.support.v7.widget.RecyclerView)} method to support other
 * LayoutManagers.
 *
 * Currently only supports LayoutManagers in VERTICAL orientation.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int itemSplitMarginEven;
    private final int itemSplitMarginLarge;
    private final int itemSplitMarginSmall;

    private final int verticalSpacing;

    public static SpacesItemDecoration newInstance(int horizontalSpacing, int verticalSpacing, int spanCount) {
        int maxNumberOfSpaces = spanCount - 1;
        int totalSpaceToSplitBetweenItems = maxNumberOfSpaces * horizontalSpacing;

        int itemSplitMarginEven = (int) (0.5f * horizontalSpacing);
        int itemSplitMarginLarge = totalSpaceToSplitBetweenItems / spanCount;
        int itemSplitMarginSmall = horizontalSpacing - itemSplitMarginLarge;

        return new SpacesItemDecoration(itemSplitMarginEven, itemSplitMarginLarge, itemSplitMarginSmall, verticalSpacing);
    }

    private SpacesItemDecoration(int itemSplitMarginEven, int itemSplitMarginLarge, int itemSplitMarginSmall, int verticalSpacing) {
        this.itemSplitMarginEven = itemSplitMarginEven;
        this.itemSplitMarginLarge = itemSplitMarginLarge;
        this.itemSplitMarginSmall = itemSplitMarginSmall;
        this.verticalSpacing = verticalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewPosition();
        int childCount = parent.getAdapter().getItemCount();

        SpanLookup spanLookup = getSpanLookup(view, parent);
        applyItemHorizontalOffsets(spanLookup, itemPosition, outRect);
        applyItemVerticalOffsets(outRect, itemPosition, childCount, spanLookup.getSpanCount(), spanLookup);
    }

    protected SpanLookup getSpanLookup(View view, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return SpanLookupFactory.gridLayoutSpanLookup((GridLayoutManager) layoutManager);
        }
        return SpanLookupFactory.singleSpan();
    }

    private void applyItemVerticalOffsets(Rect outRect, int itemPosition, int childCount, int spanCount, SpanLookup spanLookup) {
        outRect.top = getItemTopSpacing(spanLookup, verticalSpacing, itemPosition, spanCount, childCount);
        outRect.bottom = getItemBottomSpacing(spanLookup, verticalSpacing, itemPosition, childCount);
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

    private static int getItemTopSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int spanCount, int childCount) {
        if (itemIsOnTheTopRow(spanLookup, itemPosition, spanCount, childCount)) {
            return 0;
        } else {
            return (int) (.5f * verticalSpacing);
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

    private static int getItemBottomSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int childCount) {
        if (itemIsOnTheBottomRow(spanLookup, itemPosition, childCount)) {
            return 0;
        } else {
            return (int) (.5f * verticalSpacing);
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
