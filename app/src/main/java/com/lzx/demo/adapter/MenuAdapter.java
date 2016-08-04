package com.lzx.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jdsjlzx.swipe.SwipeMenuAdapter;
import com.lzx.demo.R;
import com.lzx.demo.bean.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuAdapter extends SwipeMenuAdapter<MenuAdapter.DefaultViewHolder> {

    protected List<ItemModel> mDataList = new ArrayList<>();

    public MenuAdapter() {
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<ItemModel> getDataList() {
        return mDataList;
    }

    public void setDataList(Collection<ItemModel> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<ItemModel> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void delete(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text_swipe, parent, false);
    }

    @Override
    public MenuAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {

        String item = mDataList.get(position).title;

        DefaultViewHolder viewHolder = holder;
        viewHolder.tvTitle.setText(item);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }

    }

}
