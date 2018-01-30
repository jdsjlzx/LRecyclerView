package com.lzx.demo.ui;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.DataAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.StatusBarUtil;
import com.lzx.demo.view.SampleFooter;

import java.util.ArrayList;

/**
 * 仿支付宝首页
 */
public class AlipayHomeActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private AppBarLayout abl_bar;
    private View v_title_big_mask;
    private View v_toolbar_small_mask;
    private View v_toolbar_search_mask;
    private View include_toolbar_search;
    private View include_toolbar_small;
    private int mMaskColor;

    private LRecyclerView mRecyclerView = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private DataAdapter mDataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_home);

        StatusBarUtil.setColor(AlipayHomeActivity.this, ContextCompat.getColor(this,R.color.blue_black), 0);

        //AppBarLayout
        abl_bar = (AppBarLayout) findViewById(R.id.abl_bar);
        abl_bar.addOnOffsetChangedListener(this);
        //顶部搜索布局
        include_toolbar_search = findViewById(R.id.include_toolbar_search);
        //扫一扫 付一付 聊一聊 咻一咻 的小图标布局
        include_toolbar_small = findViewById(R.id.include_toolbar_small);


        //顶部搜索布局遮罩
        v_toolbar_search_mask = findViewById(R.id.v_toolbar_search_mask);
        //扫一扫 付一付 聊一聊 咻一咻 的大图标布局遮罩
        v_title_big_mask = findViewById(R.id.v_title_big_mask);
        //扫一扫 付一付 聊一聊 咻一咻 的大图标布局遮罩
        v_toolbar_small_mask = findViewById(R.id.v_toolbar_small_mask);
        //背景颜色
        mMaskColor = getResources().getColor(R.color.blue_dark);


        mRecyclerView = findViewById(R.id.list);
        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
        }

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setDataList(dataList);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);

        SampleFooter sampleFooter = new SampleFooter(this);
        //add a FooterView
        mLRecyclerViewAdapter.addFooterView(sampleFooter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                    }
                }, 800);

            }
        });

    }

    //效果不是很完美
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d("lzx", "verticalOffset=" + verticalOffset);
        //720*1080手机 verticalOffset取值范围[0-200]px
        int absVerticalOffset = Math.abs(verticalOffset);//AppBarLayout竖直方向偏移距离px
        int totalScrollRange = appBarLayout.getTotalScrollRange();//AppBarLayout总的距离px
        //背景颜色转化成RGB的渐变色
        int argb = Color.argb(absVerticalOffset, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
        int argbDouble = Color.argb(absVerticalOffset * 2, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
        //appBarLayout上滑一半距离后小图标应该由渐变到全透明
        int title_small_offset = (200 - absVerticalOffset) < 0 ? 0 : 200 - absVerticalOffset;
        int title_small_argb = Color.argb(title_small_offset * 2, Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        //appBarLayout上滑不到一半距离
        if (absVerticalOffset <= totalScrollRange / 2) {
            include_toolbar_search.setVisibility(View.VISIBLE);
            include_toolbar_small.setVisibility(View.GONE);
            //为了和下面的大图标渐变区分,乘以2倍渐变
            v_toolbar_search_mask.setBackgroundColor(argbDouble);
        } else {
            include_toolbar_search.setVisibility(View.GONE);
            include_toolbar_small.setVisibility(View.VISIBLE);
            //appBarLayout上滑一半距离后小图标应该由渐变到全透明
            v_toolbar_small_mask.setBackgroundColor(title_small_argb);

        }
        //上滑时遮罩由全透明到半透明
        v_title_big_mask.setBackgroundColor(argb);
    }
}
