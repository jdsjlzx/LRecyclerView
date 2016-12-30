package com.lzx.demo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.base.SuperViewHolder;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.TLog;
import com.lzx.demo.view.SampleFooter;
import com.lzx.demo.view.SampleHeader;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * 功能基本开发完成（2016-12-18）
 */
public class ListDragMenuActivity extends AppCompatActivity {

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //setLayoutManager must before setAdapter
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
        }

        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setDataList(dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        //add a FooterView
        mLRecyclerViewAdapter.addFooterView(new SampleFooter(this));

        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);

        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);

    }


    private ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0, swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //设置侧滑方向为从左到右和从右到左都可以
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType()) {
                return false;
            } else {
                mDataAdapter.onItemDragMoving(source, target);
                return true;//返回true表示执行拖动
            }

        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = mLRecyclerViewAdapter.getAdapterPosition(true,viewHolder.getAdapterPosition());
            TLog.error("onSwiped position " + position);
            mDataAdapter.getDataList().remove(position);
            mDataAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变Item的透明度
                final float alpha = 1 - Math.abs(dX) / (float)viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        }
    };

    private class DataAdapter extends ListBaseAdapter<ItemModel> {


        public DataAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.sample_item_text;
        }

        @Override
        public void onBindItemHolder(SuperViewHolder holder, int position) {
            TextView textView = holder.getView(R.id.info_text);

            String item = mDataList.get(position).title;
            textView.setText(item);
        }

        public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            int from = getViewHolderPosition(source);
            int to = getViewHolderPosition(target);
            TLog.error("onItemDragMoving from " + from + "   to  " + to);
            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(getDataList(), i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(getDataList(), i, i - 1);
                }
            }

            mLRecyclerViewAdapter.notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
        }

        public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() - (mLRecyclerViewAdapter.getHeaderViewsCount() + 1);
        }

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_drag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);//必须重新setAdapter

        }else if (item.getItemId() == R.id.menu_linear) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        }
        return true;
    }

}