package com.lzx.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzx.demo.R;

import java.util.List;

public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.DefaultViewHolder> {

    private List<String> titles;

    private List<String> descriptions;

    public MainItemAdapter(List<String> titles, List<String> descriptions) {
        this.titles = titles;
        this.descriptions = descriptions;
    }

    @Override
    public int getItemCount() {
        return titles == null ? 0 : titles.size();
    }

    @Override
    public MainItemAdapter.DefaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DefaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_swipe, parent, false));
    }

    @Override
    public void onBindViewHolder(MainItemAdapter.DefaultViewHolder holder, int position) {
        holder.setData(titles.get(position), descriptions.get(position));
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_des);
        }

        public void setData(String title, String des) {
            this.tvTitle.setText(title);
            this.tvDescription.setText(des);
        }

    }

}
