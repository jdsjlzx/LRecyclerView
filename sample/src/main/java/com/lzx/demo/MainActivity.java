package com.lzx.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cundong.recyclerview.CustRecyclerView;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample入口
 */
public class MainActivity extends AppCompatActivity {

    private static final Class<?>[] ACTIVITY = {LinearLayoutActivity.class, EndlessLinearLayoutActivity.class, EndlessGridLayoutActivity.class, EndlessStaggeredGridLayoutActivity.class};
    private static final String[] TITLE = {"LinearLayoutSample", "EndlessLinearLayoutActivity", "EndlessGridLayoutActivity", "EndlessStaggeredGridLayoutActivity"};

    private CustRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;
    private ArrayList<ListItem> mDataList = null;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        mRecyclerView = (CustRecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDataList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {

            ListItem item = new ListItem();
            item.title = TITLE[i];
            item.activity = ACTIVITY[i];
            mDataList.add(item);
        }

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setData(mDataList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(this, mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ListItem listItem = mDataAdapter.getDataList().get(position);
                startActivity(new Intent(MainActivity.this, listItem.activity));
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                ListItem listItem = mDataAdapter.getDataList().get(position);
                startActivity(new Intent(MainActivity.this, listItem.activity));
            }
        }));

    }

    private static class ListItem {
        public String title;
        public Class<?> activity;
    }

    private class DataAdapter extends RecyclerView.Adapter {

        private LayoutInflater mLayoutInflater;
        private ArrayList<ListItem> mDataList = new ArrayList<>();

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<ListItem> list) {
            this.mDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_text, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ListItem listItem = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(listItem.title);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public List<ListItem> getDataList() {
            return mDataList;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);
            }
        }
    }
}