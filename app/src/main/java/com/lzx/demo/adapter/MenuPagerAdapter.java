package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.swipe.SwipeMenuAdapter;
import com.lzx.demo.R;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.TLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuPagerAdapter extends SwipeMenuAdapter<MenuPagerAdapter.DefaultViewHolder> {
    protected List<ItemModel> mDataList = new ArrayList<>();

    private Context mContext;
    public MenuPagerAdapter() {
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

    public void remove(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        if(position != mDataList.size()){ // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, mDataList.size() - position);
        }

    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pic, parent, false);
    }

    @Override
    public MenuPagerAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MenuPagerAdapter.DefaultViewHolder holder, final int position) {
        String item = mDataList.get(position).title;

        DefaultViewHolder viewHolder = holder;
        viewHolder.tvTitle.setText(item);

        viewHolder.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin_vertical = mContext.getResources().getDimensionPixelSize(R.dimen.pager_item_vertical_margin);
        int margin_horizontal = mContext.getResources().getDimensionPixelSize(R.dimen.pager_item_horizontal_margin);
        int margin_special = mContext.getResources().getDimensionPixelSize(R.dimen.pager_item_big_margin);

        TLog.error("onBindViewHolder position = " + position);
        if(position == 0 ) {
            lp.setMargins(margin_special, margin_vertical,
                    margin_horizontal, margin_vertical);
        }else if(position == mDataList.size() - 1) {
            lp.setMargins(margin_horizontal, margin_vertical,
                    margin_special, margin_vertical);
        }else {
            lp.setMargins(margin_horizontal, margin_vertical, margin_horizontal, margin_vertical);
        }

        viewHolder.itemView.setLayoutParams(lp);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View closeView;

        public DefaultViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            closeView = itemView.findViewById(R.id.close_layout);
        }

        public void setData(String title) {
            this.tvTitle.setText(title);
        }

    }

}
