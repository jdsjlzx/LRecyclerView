package com.lzx.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzx.demo.R;
import com.lzx.demo.adapter.GoodsAdapter;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.base.SuperViewHolder;
import com.lzx.demo.bean.Goods;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.AppToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * RecyclerView嵌套RecyclerView
 * 参考：http://blog.csdn.net/z593492734/article/details/51492472
 */
public class NestRecyclerViewActivity extends AppCompatActivity{
    private static final String TAG = "lzx";

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private List<ItemModel> mItemModels = new ArrayList<>();
    private List<Goods> mGoodsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);
        mRecyclerView.setNestedScrollingEnabled(false);

        initData();

        mDataAdapter = new DataAdapter(this,mGoodsList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mDataAdapter.addAll(mItemModels);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        //mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                    }
                },2000);
            }
        });

        mRecyclerView.refresh();

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                AppToast.showShortText(NestRecyclerViewActivity.this, item.title);
            }

        });

        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                AppToast.showShortText(NestRecyclerViewActivity.this, "onItemLongClick - " + item.title);
            }
        });

    }

    private void initData() {
        for (int i = 0; i < 12; i++) {
            ItemModel item = new ItemModel();
            item.id = i;
            item.title = "item" + (item.id);
            mItemModels.add(item);
        }

        Random random = new Random();
        int count = random.nextInt(9);
        if(count == 0) {
            count = 2;
        }
        for (int i = 0; i < count; i++) {
            Goods goods = new Goods();
            goods.price = String.valueOf(100+i);
            goods.title = "正品羊毛衫"+i;
            mGoodsList.add(goods);
        }

    }

    private class DataAdapter extends ListBaseAdapter<ItemModel> {

        private List<Goods> mGoodsList = new ArrayList<>();


        public DataAdapter(Context context, List<Goods> goodsList) {
            super(context);
            this.mGoodsList = goodsList;
        }

        @Override
        public int getLayoutId() {
            return R.layout.layout_list_item_nest_wrapper;
        }

        @Override
        public void onBindItemHolder(SuperViewHolder holder, int position) {
            TextView textView = holder.getView(R.id.info_text);
            RecyclerView recyclerView = holder.getView(R.id.recyclerview_inner);

            ItemModel item = mDataList.get(position);

            textView.setText(item.title);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(new GoodsAdapter(mContext, mGoodsList));
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_refresh) {
            mRecyclerView.forceToRefresh();
        }
        return true;
    }
}