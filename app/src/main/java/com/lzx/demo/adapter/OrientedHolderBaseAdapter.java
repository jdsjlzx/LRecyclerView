package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzx.demo.bean.HolderOrientedModel;
import com.lzx.demo.holder.OrientedBaseHolder;
import com.lzx.demo.holder.OrientedHolderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜坤鹏 on 2019/5/27.
 */

public  class OrientedHolderBaseAdapter<T> extends RecyclerView.Adapter<OrientedBaseHolder> {

    protected Context mContext;
    private LayoutInflater mInflater;
    private final static String TAG = "MyBaseAdapter";

    protected List<T> mDataList = new ArrayList<>();
    protected List<HolderOrientedModel> mList = new ArrayList<HolderOrientedModel>();

    public OrientedHolderBaseAdapter(Context context) {
        this.mContext =  context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public OrientedBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder type"+viewType);
        return OrientedHolderFactory.createViewHolder(mInflater,parent,viewType);
    }


    @Override
    public void onBindViewHolder(final OrientedBaseHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder position"+position);
        holder.bindItemView(mList,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.onItemClick(v,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList!=null? mList.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        HolderOrientedModel model =(HolderOrientedModel) mList.get(position);
        int type = model.getType();
        return type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(ArrayList<HolderOrientedModel> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


}
