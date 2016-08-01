package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzx.demo.ItemDecoration.DoubleHeaderAdapter;
import com.lzx.demo.R;

public class DoubleHeaderTestAdapter extends RecyclerView.Adapter<DoubleHeaderTestAdapter.ViewHolder> implements
        DoubleHeaderAdapter<DoubleHeaderTestAdapter.HeaderHolder, DoubleHeaderTestAdapter.SubHeaderHolder> {

    private LayoutInflater mInflater;

    public DoubleHeaderTestAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.item_test, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.item.setText("Item " + i);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public long getHeaderId(int position) {
        return position / 14;
    }

    @Override
    public long getSubHeaderId(int position) {
        return position / 7;
    }

    @Override
    public HeaderHolder onCreateHeaderHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.super_header_test, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public SubHeaderHolder onCreateSubHeaderHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.header_test, parent, false);
        return new SubHeaderHolder(view);
    }

    @Override
    public void onBindHeaderHolder(HeaderHolder viewholder, int position) {
        viewholder.timeline.setText("Header " + getHeaderId(position));
    }

    @Override
    public void onBindSubHeaderHolder(SubHeaderHolder viewholder, int position) {
        viewholder.date.setText("Sub-header " + getSubHeaderId(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;

        public ViewHolder(View itemView) {
            super(itemView);

            item = (TextView) itemView;
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView timeline;

        public HeaderHolder(View itemView) {
            super(itemView);

            timeline = (TextView) itemView;
        }
    }

    static class SubHeaderHolder extends RecyclerView.ViewHolder {
        public TextView date;

        public SubHeaderHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView;
        }
    }
}
