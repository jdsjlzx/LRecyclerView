package com.lzx.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzx.demo.R;
import com.lzx.demo.view.PullScrollView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        final PullScrollView refreshLayout = (PullScrollView) findViewById(R.id.refresh_layout);
        final WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        //设置头部加载颜色
        refreshLayout.setHeaderViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        refreshLayout.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        refreshLayout.setRefreshListener(new PullScrollView.RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("http://www.stay4it.com");
                        refreshLayout.setRefreshCompleted();
                    }
                }, 1000);
            }
        });

        refreshLayout.refreshWithPull();
    }
}
