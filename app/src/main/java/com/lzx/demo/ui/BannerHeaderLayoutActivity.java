package com.lzx.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.why168.LoopViewPagerLayout;
import com.github.why168.listener.OnLoadImageViewListener;
import com.github.why168.modle.BannerInfo;
import com.github.why168.modle.LoopStyle;
import com.lzx.demo.R;
import com.lzx.demo.adapter.DataAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.imageloader.ImageLoader;
import com.lzx.demo.imageloader.ImageLoaderUtil;
import com.lzx.demo.view.SampleFooter;

import java.util.ArrayList;

/**
 *
 * 带HeaderView、FooterView的LinearLayout RecyclerView
 */
public class BannerHeaderLayoutActivity extends AppCompatActivity{

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private LoopViewPagerLayout mLoopViewPagerLayout;

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

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setDataList(dataList);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //add a HeaderView
        //LoopViewPagerLayout使用方法详见github：https://github.com/why168/LoopViewPagerLayout
        mLoopViewPagerLayout = (LoopViewPagerLayout) LayoutInflater.from(this).inflate(R.layout.layout_banner_header,(ViewGroup)findViewById(android.R.id.content), false);
        mLoopViewPagerLayout.setLoop_ms(2000);//轮播的速度(毫秒)
        mLoopViewPagerLayout.setLoop_duration(800);//滑动的速率(毫秒)
        mLoopViewPagerLayout.setLoop_style(LoopStyle.Empty);//轮播的样式-默认empty
        mLoopViewPagerLayout.initializeData(this);//初始化数据
        ArrayList<BannerInfo> data = new ArrayList<>();
        data.add(new BannerInfo<Integer>(R.mipmap.slient, "第一张图片"));
        data.add(new BannerInfo<Integer>(R.mipmap.arrow_down, "第三张图片"));
        data.add(new BannerInfo<Integer>(R.mipmap.ic_action_add, "第四张图片"));
        data.add(new BannerInfo<Integer>(R.mipmap.smile, "第五张图片"));
        mLoopViewPagerLayout.setOnLoadImageViewListener(new OnLoadImageViewListener() {
            @Override
            public void onLoadImageView(ImageView imageView, Object parameter) {
                ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil();
                ImageLoader imageLoader = new ImageLoader.Builder()
                        .imgView(imageView)
                        .url(parameter)
                        .build();

                imageLoaderUtil.loadImage(BannerHeaderLayoutActivity.this, imageLoader);
            }

            @Override
            public ImageView createImageView(Context context) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        mLoopViewPagerLayout.setLoopData(data);

        mLRecyclerViewAdapter.addHeaderView(mLoopViewPagerLayout);

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
        mRecyclerView.setPullRefreshEnabled(true);

        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);

        //add a FooterView
        mLRecyclerViewAdapter.addFooterView(sampleFooter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete(10);
                    }

                }, 1000);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoopViewPagerLayout.startLoop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoopViewPagerLayout.stopLoop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}