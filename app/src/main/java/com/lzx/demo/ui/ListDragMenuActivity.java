package com.lzx.demo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.ItemDragAdapter;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.callback.ItemDragAndSwipeCallback;
import com.lzx.demo.interfaces.OnItemDragListener;
import com.lzx.demo.interfaces.OnItemSwipeListener;
import com.lzx.demo.util.TLog;
import com.lzx.demo.view.SampleFooter;

import java.util.ArrayList;

/**
 *
 * 功能开发中，请等待
 */
public class ListDragMenuActivity extends AppCompatActivity {

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private ItemDragAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
        }


        mAdapter = new ItemDragAdapter(this);
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
        mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(listener);


        /*mDataAdapter = new DataAdapter(this);
        mDataAdapter.setDataList(dataList);*/
        mAdapter.setDataList(dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        SampleFooter sampleFooter = new SampleFooter(this);
        sampleFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 加载更多
                ArrayList<ItemModel> dataList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    ItemModel itemModel = new ItemModel();
                    itemModel.title = "item" + (i + mDataAdapter.getItemCount());
                    dataList.add(itemModel);
                }
                mDataAdapter.addAll(dataList);
            }
        });

        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);

        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);


    }

    private OnItemDragListener listener = new OnItemDragListener() {
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            TLog.error("drag start");
            ((CardView)viewHolder.itemView).setCardBackgroundColor(ContextCompat.getColor(ListDragMenuActivity.this, R.color.color_light_blue));
        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            TLog.error("move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
        }

        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            TLog.error("drag end");
        }
    };

    private OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            TLog.error("view swiped start: " + pos);
        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            TLog.error("View reset: " + pos);
        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
            TLog.error("View Swiped: " + pos);
        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

        }
    };

    private class DataAdapter extends ListBaseAdapter<ItemModel> {

        private LayoutInflater mLayoutInflater;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_text, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            String item = mDataList.get(position).title;

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(item);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}