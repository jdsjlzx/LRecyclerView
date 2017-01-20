package com.lzx.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzx.demo.R;
import com.lzx.demo.adapter.MultipleItemAdapter;
import com.lzx.demo.bean.MultipleItem;
import com.lzx.demo.util.AppToast;
import com.lzx.demo.view.SampleFooter;
import com.lzx.demo.view.SampleHeader;

import java.util.ArrayList;
import java.util.List;

public class MulItemGridLayoutActivity extends AppCompatActivity{
    private static final String TAG = "lzx";

    private LRecyclerView mRecyclerView = null;

    private MultipleItemAdapter mMultipleItemAdapter  = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<MultipleItem> mItemModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        initData();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        mMultipleItemAdapter = new MultipleItemAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMultipleItemAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mMultipleItemAdapter.addAll(mItemModels);

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
                if (position == 3) {
                    return gridLayoutManager.getSpanCount();
                } else if (position == 7 ) {
                    return gridLayoutManager.getSpanCount() - 1;
                } else {
                    return 1;
                }

            }
        });


        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        mLRecyclerViewAdapter.addHeaderView( new SampleHeader(this));

        SampleFooter sampleFooter = new SampleFooter(this);
        mLRecyclerViewAdapter.addFooterView(sampleFooter);

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

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MultipleItem item = mMultipleItemAdapter.getDataList().get(position);
                AppToast.showShortText(MulItemGridLayoutActivity.this, item.getTitle());
            }
        });

        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                MultipleItem item = mMultipleItemAdapter.getDataList().get(position);
                AppToast.showShortText(MulItemGridLayoutActivity.this, "onItemLongClick - " + item.getTitle());
            }
        });


    }



    private void initData() {

        for (int i = 0; i < 18; i++) {

            MultipleItem item ;
            if(i == 2){
                item = new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE);
            }else {
                item = new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE);
            }
            item.setTitle("item"+i);

            mItemModels.add(item);
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