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
import android.widget.RelativeLayout;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.DataAdapter;
import com.lzx.demo.bean.BannerInfo;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.imageloader.ImageLoader;
import com.lzx.demo.imageloader.ImageLoaderUtil;
import com.lzx.demo.view.SampleFooter;
import com.stay4it.banner.Banner;
import com.stay4it.banner.BannerConfig;
import com.stay4it.banner.Transformer;
import com.stay4it.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 带HeaderView、FooterView的LinearLayout RecyclerView
 */
public class AlphaChangeActivity extends AppCompatActivity{

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private Banner banner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_change);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
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
        banner = (Banner) LayoutInflater.from(this).inflate(R.layout.layout_banner_header,(ViewGroup)findViewById(android.R.id.content), false);

        ArrayList<BannerInfo> data = new ArrayList<>();
        data.add(new BannerInfo("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg", "第一张图片"));
        data.add(new BannerInfo("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg", "第三张图片"));
        data.add(new BannerInfo("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg", "第四张图片"));

        initBanner(data);

        mLRecyclerViewAdapter.addHeaderView(banner);

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

        final RelativeLayout alpha_title
                = (RelativeLayout) findViewById(R.id.alpha_title);
        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                int lHeight = banner.getHeight() - 45;
                if(distanceY < 0){
                    alpha_title.getBackground().setAlpha(0);
                }else {
                    if (distanceY < lHeight) {
                        int progress = (int) (new Float(distanceY) / new Float(lHeight) * 200);//255
                        alpha_title.getBackground().setAlpha(progress);
                    } else {
                        alpha_title.getBackground().setAlpha(255 - 55);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });

    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * 初始化
     */
    private void initBanner(final List<BannerInfo> list) {
        banner.setDelayTime(2000)
                .setBannerAnimation(Transformer.DepthPage)
                .setImages(list)
                .setBannerTitles(getBannerTitleList(list))
                .setBannerStyle(BannerConfig.ONLY_TITLE)
                .setImageLoader(new com.stay4it.banner.loader.ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        BannerInfo bannerInfo = (BannerInfo) path;

                        ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil();
                        ImageLoader imageLoader = new ImageLoader.Builder()
                                .imgView(imageView)
                                .url(bannerInfo.imgUrl)
                                .build();

                        imageLoaderUtil.loadImage(AlphaChangeActivity.this, imageLoader);

                    }
                }).start();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerInfo bannerInfo = list.get(position);
                //todo
            }
        });
    }

    private List<String> getBannerTitleList(List<BannerInfo> list) {
        List<String> titles = new ArrayList<>();
        for (BannerInfo banner: list) {
            titles.add(banner.title);
        }
        return titles;
    }



}