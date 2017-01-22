package com.lzx.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzx.demo.R;
import com.lzx.demo.view.PullScrollView;

public class ScrollViewLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);


        final PullScrollView refreshLayout = (PullScrollView) findViewById(R.id.refresh_layout);
        //设置头部加载颜色
        refreshLayout.setHeaderViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
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

        refreshLayout.refreshWithPull();
    }
}
