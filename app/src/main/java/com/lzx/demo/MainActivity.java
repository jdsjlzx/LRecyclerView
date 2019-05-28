package com.lzx.demo;

import android.content.Intent;
import android.os.Bundle;

import com.lzx.demo.base.BaseMainActivity;
import com.lzx.demo.multitype.MultiTypeActivity;
import com.lzx.demo.ui.CommonActivity;
import com.lzx.demo.ui.CoordinatorLayoutActivity;
import com.lzx.demo.ui.OrientedHolderTestActivity;
import com.lzx.demo.ui.PulldownRefreshActivity;
import com.lzx.demo.ui.SectionCollectionActivity;
import com.lzx.demo.ui.SwipeMenuActivity;

public class MainActivity extends BaseMainActivity {

    private static final Class<?>[] ACTIVITY = {CommonActivity.class, CoordinatorLayoutActivity.class, MultiTypeActivity.class, SectionCollectionActivity.class, SwipeMenuActivity.class,PulldownRefreshActivity.class,OrientedHolderTestActivity.class};
    private static final String[] TITLE = {"CommonActivity","CoordinatorLayoutActivity","MultiTypeActivity", "SectionCollectionActivity","SwipeMenuActivity","PulldownRefreshActivity","OrientedHolderTestActivity"};

    @Override
    public Class<?>[] getActivitys() {
        return ACTIVITY;
    }

    @Override
    public String[] getTitles() {
        return TITLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
