package com.lzx.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzx.demo.R;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.base.SuperViewHolder;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.NetworkUtils;
import com.lzx.demo.view.PullScrollView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CollapsingToolbarLayoutActivity2 extends AppCompatActivity {

    private int mOffset = 0;
    private int mScrollY = 0;

    private int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar_two_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final View parallax = findViewById(R.id.parallax);
        final View buttonBar = findViewById(R.id.buttonBarLayout);

        final PullScrollView refreshLayout = (PullScrollView) findViewById(R.id.scrollView);
        //设置头部加载颜色
        refreshLayout.setHeaderViewColor(R.color.colorAccent, R.color.dark ,android.R.color.transparent);
        refreshLayout.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        refreshLayout.setRefreshListener(new PullScrollView.RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshCompleted();
                    }
                }, 1000);
            }
        });


        refreshLayout.setScrollViewListener(new PullScrollView.OnScrollChangeListener() {

            private int lastScrollY = 0;
            private int h = 340;
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) & 0x00ffffff;

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBar.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });

        buttonBar.setAlpha(0);
        toolbar.setBackgroundColor(0);


        setImages();

    }

    private void setImages() {
        ((ImageView) findViewById(R.id.iv1)).setImageDrawable(ContextCompat
                .getDrawable(getApplicationContext(), R.mipmap.img1));
        ((ImageView) findViewById(R.id.iv2)).setImageDrawable(ContextCompat
                .getDrawable(getApplicationContext(), R.mipmap.img2));
        ((ImageView) findViewById(R.id.iv3)).setImageDrawable(ContextCompat
                .getDrawable(getApplicationContext(), R.mipmap.img3));
        ((ImageView) findViewById(R.id.iv4)).setImageDrawable(ContextCompat
                .getDrawable(getApplicationContext(), R.mipmap.img4));
        ((ImageView) findViewById(R.id.iv5)).setImageDrawable(ContextCompat
                .getDrawable(getApplicationContext(), R.mipmap.img5));
        ((ImageView) findViewById(R.id.iv6)).setImageDrawable(ContextCompat
                .getDrawable(getApplicationContext(), R.mipmap.img6));
        ((ImageView) findViewById(R.id.iv7)).setImageDrawable(ContextCompat
                .getDrawable(getApplicationContext(), R.mipmap.loading_bg));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
