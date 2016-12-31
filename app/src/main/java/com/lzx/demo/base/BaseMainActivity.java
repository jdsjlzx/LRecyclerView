package com.lzx.demo.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzx.demo.ItemDecoration.DividerDecoration;
import com.lzx.demo.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;
    private ArrayList<ListItem> mDataList = null;

    public abstract Class<?>[] getActivitys();
    public abstract String[] getTitles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDataList = new ArrayList<>();
        for (int i = 0; i < getActivitys().length; i++) {

            ListItem item = new ListItem();
            item.title = getTitles()[i];
            item.activity = getActivitys()[i];
            mDataList.add(item);
        }

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setData(mDataList);
        mRecyclerView.setAdapter(mDataAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setColorResource(R.color.divider)
                .build();
        mRecyclerView.addItemDecoration(divider);

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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ListItem listItem = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(listItem.title);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ListItem listItem = mDataList.get(position);
                    startActivity(new Intent(BaseMainActivity.this, listItem.activity));
                }
            });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_setion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jdsjlzx/LRecyclerView"));
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
