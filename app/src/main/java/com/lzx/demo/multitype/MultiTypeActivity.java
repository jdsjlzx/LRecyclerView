package com.lzx.demo.multitype;

import com.lzx.demo.base.BaseMainActivity;

public class MultiTypeActivity extends BaseMainActivity {

    private static final Class<?>[] ACTIVITY = {BilibiliActivity.class, ExpandableActivity.class};
    private static final String[] TITLE = {"BilibiliActivity","ExpandableActivity"};

    public Class<?>[] getActivitys() {
        return ACTIVITY;
    }

    @Override
    public String[] getTitles() {
        return TITLE;
    }


}
