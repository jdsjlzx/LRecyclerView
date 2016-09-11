package com.lzx.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.swipe.touch.OnItemMoveListener;
import com.lzx.demo.ItemDecoration.ListViewDecoration;
import com.lzx.demo.R;
import com.lzx.demo.adapter.MenuAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.AppToast;

import java.util.ArrayList;
import java.util.Collections;

public class SwipeDeleteListActivity extends AppCompatActivity {
    private Activity mContext;

    private LRecyclerView mRecyclerView = null;

    private MenuAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
        }

        mDataAdapter = new MenuAdapter();
        mDataAdapter.setDataList(dataList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setPullRefreshEnabled(false);

        // 这里不用添加菜单，滑动删除和滑动菜单互相冲突

        mRecyclerView.setLongPressDragEnabled(true);
        mRecyclerView.setItemViewSwipeEnabled(true);// 开启滑动删除。
        mRecyclerView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。

    }

    /**
     * 当Item移动的时候。
     */
    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            final int adjFromPosition = fromPosition - (mLRecyclerViewAdapter.getHeaderViewsCount() + 1);
            final int adjToPosition = toPosition - (mLRecyclerViewAdapter.getHeaderViewsCount() + 1);
            // 当Item被拖拽的时候。
            Collections.swap(mDataAdapter.getDataList(), adjFromPosition, adjToPosition);
            //Be carefull in here!
            mLRecyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            final int adjPosition = position - (mLRecyclerViewAdapter.getHeaderViewsCount() + 1);
            mDataAdapter.remove(adjPosition);
            AppToast.showShortText(SwipeDeleteListActivity.this, "现在的第" + adjPosition + "条被删除。");
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}