package com.lzx.demo.ui;

import com.lzx.demo.base.BaseMainActivity;

public class SwipeMenuActivity extends BaseMainActivity {

    private static final Class<?>[] ACTIVITY = {SwipeDeleteActivity.class, ListDragMenuActivity.class,LinearLayoutDelActivity.class};
    private static final String[] TITLE = {"SwipeDeleteActivity", "ListDragMenuActivity","LinearLayoutDelActivity"};

    public Class<?>[] getActivitys() {
        return ACTIVITY;
    }

    @Override
    public String[] getTitles() {
        return TITLE;
    }


}
