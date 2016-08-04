package com.github.jdsjlzx.swipe.touch;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class DefaultItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private OnItemMovementListener onItemMovementListener;

    private OnItemMoveListener onItemMoveListener;

    private boolean isItemViewSwipeEnabled;

    private boolean isLongPressDragEnabled;

    public DefaultItemTouchHelperCallback() {
    }

    public void setLongPressDragEnabled(boolean canDrag) {
        this.isLongPressDragEnabled = canDrag;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    public void setItemViewSwipeEnabled(boolean canSwipe) {
        this.isItemViewSwipeEnabled = canSwipe;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isItemViewSwipeEnabled;
    }

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        this.onItemMoveListener = onItemMoveListener;
    }

    public OnItemMoveListener getOnItemMoveListener() {
        return onItemMoveListener;
    }

    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener) {
        this.onItemMovementListener = onItemMovementListener;
    }

    public OnItemMovementListener getOnItemMovementListener() {
        return onItemMovementListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder) {
        if (onItemMovementListener != null) {
            int dragFlags = onItemMovementListener.onDragFlags(recyclerView, targetViewHolder);
            int swipeFlags = onItemMovementListener.onSwipeFlags(recyclerView, targetViewHolder);
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            //适用GridLayoutManager类型和LinearLayoutManager类型。
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                //拖拽的方向。
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                //侧滑的方向：left right。
                return makeMovementFlags(dragFlags, 0);
            } else if (layoutManager instanceof LinearLayoutManager) {
                //线性方向分为：水平和垂直方向。
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    //拖拽的方向。
                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    //侧滑的方向：left right。
                    int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }
        }
        return makeMovementFlags(0, 0);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //判断当前是否是swipe方式：侧滑。
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //1.ItemView--ViewHolder; 2.侧滑条目的透明度程度关联谁？dX(delta增量，范围：当前条目-width~width)。
            float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);//1~0
        }
        // super里面自动实现了viewHolder.itemView.setTranslationX(dX);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }


    @Override
    public boolean onMove(RecyclerView arg0, RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        if (srcHolder.getItemViewType() == targetHolder.getItemViewType()) {// 相同类型的允许切换。
            if (onItemMoveListener == null)
                return false;
            //回调适配器里面的方法，让其刷新数据及界面。
            return onItemMoveListener.onItemMove(srcHolder.getAdapterPosition(), targetHolder.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //回调适配器里面的方法，让其刷新数据及界面。
        if (onItemMoveListener != null)
            onItemMoveListener.onItemDismiss(viewHolder.getAdapterPosition());
    }
}