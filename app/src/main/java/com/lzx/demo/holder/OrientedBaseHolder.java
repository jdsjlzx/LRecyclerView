package com.lzx.demo.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzx.demo.bean.HolderOrientedModel;

import java.util.List;

public abstract class OrientedBaseHolder extends RecyclerView.ViewHolder  {
    public OrientedBaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindItemView(List<HolderOrientedModel> mDataList, int position);

    public abstract void onItemClick(View v, int position);


}
