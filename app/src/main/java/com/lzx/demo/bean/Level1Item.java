package com.lzx.demo.bean;


import com.lzx.demo.adapter.ExpandableItemAdapter;

public class Level1Item extends AbstractExpandableItem<Person> implements MultiItemEntity{
    public String title;
    public String subTitle;

    public Level1Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_ONE;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}