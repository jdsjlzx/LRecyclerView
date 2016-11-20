package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.bean.Goods;

import java.util.List;

/**
 * Created by lizhixian on 2016/11/19.
 */

public class GoodsAdapter extends ListBaseAdapter<Goods> {

    private LayoutInflater mLayoutInflater;

    public GoodsAdapter(Context context, List<Goods> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        setDataList(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.layout_list_item_nest_inner, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.titleTextView.setText(getDataList().get(position).title);

    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
