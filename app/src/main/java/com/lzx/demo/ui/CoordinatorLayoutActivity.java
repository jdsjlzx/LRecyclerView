package com.lzx.demo.ui;

import com.lzx.demo.base.BaseMainActivity;

public class CoordinatorLayoutActivity extends BaseMainActivity {

    private static final Class<?>[] ACTIVITY = {CollapsingToolbarLayoutActivity.class, CollapsingToolbarLayoutActivity2.class, AlipayHomeActivity.class};
    private static final String[] TITLE = {"CollapsingToolbarLayoutActivity", "CollapsingToolbarLayoutActivity2", "AlipayHomeActivity"};

    public Class<?>[] getActivitys() {
        return ACTIVITY;
    }

    @Override
    public String[] getTitles() {
        return TITLE;
    }
}
