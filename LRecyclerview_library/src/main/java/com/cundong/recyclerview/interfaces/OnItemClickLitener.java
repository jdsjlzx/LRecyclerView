package com.cundong.recyclerview.interfaces;

import android.view.View;

/**
 * Click and LongClick
 */
public interface OnItemClickLitener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view , int position);
}
