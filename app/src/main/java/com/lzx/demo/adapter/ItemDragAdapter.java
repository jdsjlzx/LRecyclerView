package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.base.BaseItemDraggableAdapter;
import com.lzx.demo.bean.ItemModel;

/**
 * Created by luoxw on 2016/6/20.
 */
public class ItemDragAdapter extends BaseItemDraggableAdapter<ItemModel,ItemDragAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;

    public ItemDragAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String item = "item"+position;

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.info_text);
        }
    }
}
