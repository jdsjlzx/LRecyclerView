package com.github.jdsjlzx.ItemDecoration;

import android.support.v7.widget.GridLayoutManager;

class SpanLookupFactory {

    static SpanLookup singleSpan() {
        return new SpanLookup() {
            @Override
            public int getSpanCount() {
                return 1;
            }

            @Override
            public int getSpanIndex(int itemPosition) {
                return 0;
            }

            @Override
            public int getSpanSize(int itemPosition) {
                return 1;
            }
        };
    }

    static SpanLookup gridLayoutSpanLookup(final GridLayoutManager layoutManager) {
        return new SpanLookup() {
            @Override
            public int getSpanCount() {
                return layoutManager.getSpanCount();
            }

            @Override
            public int getSpanIndex(int itemPosition) {
                return layoutManager.getSpanSizeLookup().getSpanIndex(itemPosition, getSpanCount());
            }

            @Override
            public int getSpanSize(int itemPosition) {
                return layoutManager.getSpanSizeLookup().getSpanSize(itemPosition);
            }
        };
    }

}
