package com.lzx.demo.ui;

import com.lzx.demo.base.BaseMainActivity;

public class CommonActivity extends BaseMainActivity {

    private static final Class<?>[] ACTIVITY = {
            LinearLayoutActivity.class,
            EndlessLinearLayoutActivity.class,
            EndlessGridLayoutActivity.class,
            EndlessStaggeredGridLayoutActivity.class,
            EmptyViewActivity.class,
            CollapsingToolbarLayoutActivity.class,
            SwipeRefreshLayoutActivity.class,
            ExpandableRecyclerViewOneActivity.class,
            MulItemLinearLayoutActivity.class,
            MulItemGridLayoutActivity.class,
            PartialRefreshActivity.class,
            NestRecyclerViewActivity.class,
            Nest2RecyclerViewActivity.class,
            BannerHeaderLayoutActivity.class};

    private static final String[] TITLE = {
            "LinearLayoutSample",
            "EndlessLinearLayoutActivity",
            "EndlessGridLayoutActivity",
            "EndlessStaggeredGridLayoutActivity",
            "EmptyViewActivity",
            "CollapsingToolbarLayoutActivity",
            "SwipeRefreshLayoutActivity",
            "ExpandableRecyclerViewOneActivity",
            "MulItemLinearLayoutActivity","MulItemGridLayoutActivity",
            "(局部刷新)PartialRefreshActivity",
            "(Recylcerview嵌套)NestRecyclerViewActivity",
            "Nest2RecyclerViewActivity",
            "BannerHeaderLayoutActivity"};

    public Class<?>[] getActivitys() {
        return ACTIVITY;
    }

    @Override
    public String[] getTitles() {
        return TITLE;
    }


}
