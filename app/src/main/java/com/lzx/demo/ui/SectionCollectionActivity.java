package com.lzx.demo.ui;

import com.lzx.demo.base.BaseMainActivity;

public class SectionCollectionActivity extends BaseMainActivity {

    private static final Class<?>[] ACTIVITY = {SectionLayoutActivity.class, SectionAnimalActivity.class};
    private static final String[] TITLE = {"SectionLayoutActivity", "SectionAnimalActivity"};


    public Class<?>[] getActivitys() {
        return ACTIVITY;
    }

    @Override
    public String[] getTitles() {
        return TITLE;
    }
}
