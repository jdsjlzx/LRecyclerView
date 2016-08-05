package com.github.jdsjlzx.interfaces;

public interface OnSwipeMenuItemClickListener {

    /**
     * Invoke when the menu item is clicked.
     *
     * @param closeable       closeable.
     * @param adapterPosition adapterPosition.
     * @param menuPosition    menuPosition.
     * @param direction       can be {@link com.github.jdsjlzx.recyclerview.LRecyclerView#LEFT_DIRECTION}, {@link com.github.jdsjlzx.recyclerview.LRecyclerView#RIGHT_DIRECTION}.
     */
    void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction);

}