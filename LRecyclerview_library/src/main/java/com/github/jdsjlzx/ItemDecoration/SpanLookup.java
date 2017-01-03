package com.github.jdsjlzx.ItemDecoration;

public interface SpanLookup {

    /**
     * @return number of spans in a row
     */
    int getSpanCount();

    /**
     * @param itemPosition
     * @return start span for the item at the given adapter position
     */
    int getSpanIndex(int itemPosition);

    /**
     * @param itemPosition
     * @return number of spans the item at the given adapter position occupies
     */
    int getSpanSize(int itemPosition);

}
