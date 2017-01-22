package com.lzx.demo.ui;

import com.lzx.demo.base.BaseMainActivity;

public class PulldownRefreshActivity extends BaseMainActivity {

    private static final Class<?>[] ACTIVITY = {ScrollViewLayoutActivity.class, WebViewActivity.class};
    private static final String[] TITLE = {"ScrollViewLayoutActivity","WebViewActivity"};

    public Class<?>[] getActivitys() {
        return ACTIVITY;
    }

    @Override
    public String[] getTitles() {
        return TITLE;
    }


}
