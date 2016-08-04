package com.github.jdsjlzx.swipe.touch;


import android.support.v7.widget.helper.CompatItemTouchHelper;

public class DefaultItemTouchHelper extends CompatItemTouchHelper {

    private DefaultItemTouchHelperCallback mDefaultItemTouchHelperCallback;

    /**
     * Create default item touch helper.
     */
    public DefaultItemTouchHelper() {
        this(new DefaultItemTouchHelperCallback());
    }

    /**
     * @param callback the behavior of ItemTouchHelper.
     */
    private DefaultItemTouchHelper(DefaultItemTouchHelperCallback callback) {
        super(callback);
        mDefaultItemTouchHelperCallback = (DefaultItemTouchHelperCallback) getCallback();
    }

    /**
     * Set OnItemMoveListener.
     *
     * @param onItemMoveListener {@link OnItemMoveListener}.
     */
    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        mDefaultItemTouchHelperCallback.setOnItemMoveListener(onItemMoveListener);
    }

    /**
     * Get OnItemMoveListener.
     *
     * @return {@link OnItemMoveListener}.
     */
    public OnItemMoveListener getOnItemMoveListener() {
        return mDefaultItemTouchHelperCallback.getOnItemMoveListener();
    }

    /**
     * Set OnItemMovementListener.
     *
     * @param onItemMovementListener {@link OnItemMovementListener}.
     */
    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener) {
        mDefaultItemTouchHelperCallback.setOnItemMovementListener(onItemMovementListener);
    }

    /**
     * Get OnItemMovementListener.
     *
     * @return {@link OnItemMovementListener}.
     */
    public OnItemMovementListener getOnItemMovementListener() {
        return mDefaultItemTouchHelperCallback.getOnItemMovementListener();
    }

    /**
     * Set can long press drag.
     *
     * @param canDrag drag true, otherwise is can't.
     */
    public void setLongPressDragEnabled(boolean canDrag) {
        mDefaultItemTouchHelperCallback.setLongPressDragEnabled(canDrag);
    }

    /**
     * Get can long press drag.
     *
     * @return drag true, otherwise is can't.
     */
    public boolean isLongPressDragEnabled() {
        return mDefaultItemTouchHelperCallback.isLongPressDragEnabled();
    }


    /**
     * Set can long press swipe.
     *
     * @param canSwipe swipe true, otherwise is can't.
     */
    public void setItemViewSwipeEnabled(boolean canSwipe) {
        mDefaultItemTouchHelperCallback.setItemViewSwipeEnabled(canSwipe);
    }

    /**
     * Get can long press swipe.
     *
     * @return swipe true, otherwise is can't.
     */
    public boolean isItemViewSwipeEnabled() {
        return this.mDefaultItemTouchHelperCallback.isItemViewSwipeEnabled();
    }

}
