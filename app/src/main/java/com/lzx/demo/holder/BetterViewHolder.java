package com.lzx.demo.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzx.demo.bean.Visitable;

public abstract class BetterViewHolder<T extends Visitable> extends RecyclerView.ViewHolder {

    public BetterViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindItem(T t);
}
