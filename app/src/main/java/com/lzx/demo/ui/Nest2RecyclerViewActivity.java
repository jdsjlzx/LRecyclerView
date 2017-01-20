package com.lzx.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.ShopAdapterBase;
import com.lzx.demo.bean.Goods;
import com.lzx.demo.bean.MultipleItem;
import com.lzx.demo.view.SampleHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView嵌套RecyclerView
 * 参考：http://blog.csdn.net/z593492734/article/details/51492472
 */
public class Nest2RecyclerViewActivity extends AppCompatActivity{
    private static final String TAG = "lzx";

    private LRecyclerView mRecyclerView = null;

    private ShopAdapterBase mShopAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private List<MultipleItem> mItemModels = new ArrayList<>();
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

        mShopAdapter = new ShopAdapterBase(this,mGoodsList);

        //setLayoutManager must before setAdapter
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        initData();

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mShopAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mShopAdapter.addAll(mItemModels);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);

        mLRecyclerViewAdapter.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                MultipleItem item = mShopAdapter.getDataList().get(position);
                if (item.getItemType() == MultipleItem.LIST) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }

            }
        });

        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                    }
                },1000);
            }
        });

        mRecyclerView.refresh();

        //是否允许嵌套滑动
        mRecyclerView.setNestedScrollingEnabled(false);

    }

    private void initData() {
        for (int i = 0; i < 22; i++) {
            MultipleItem item ;
            if(i == 2){
                item = new MultipleItem(MultipleItem.LIST);
            }else {
                item = new MultipleItem(MultipleItem.TEXT);
            }
            item.setTitle("item"+i);
            mItemModels.add(item);
        }

        for (int i = 0; i < 20; i++) {
            Goods goods = new Goods();
            goods.price = String.valueOf(100+i);
            goods.title = "正品羊毛衫"+i;
            mGoodsList.add(goods);
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