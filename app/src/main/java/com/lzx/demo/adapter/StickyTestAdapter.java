package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lzx.demo.ItemDecoration.StickyHeaderAdapter;
import com.lzx.demo.R;


public class StickyTestAdapter extends RecyclerView.Adapter<StickyTestAdapter.ViewHolder> implements
        StickyHeaderAdapter<StickyTestAdapter.HeaderHolder> {

    private LayoutInflater mInflater;
    private Context mContext;

    public StickyTestAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.item_test, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.item.setText("Item " + position);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public long getHeaderId(int position) {
        return (long) position / 7;
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.header_test, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final HeaderHolder viewholder, final int position) {
        viewholder.header.setText("Header " + getHeaderId(position));

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"onBindHeaderViewHolder item clicked position = " + position,Toast.LENGTH_SHORT).show();
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;

        public ViewHolder(View itemView) {
            super(itemView);

            item = (TextView) itemView;
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);

            header = (TextView) itemView;
        }
    }
}
