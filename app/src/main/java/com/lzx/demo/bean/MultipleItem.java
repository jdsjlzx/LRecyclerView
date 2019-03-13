package com.lzx.demo.bean;

public class MultipleItem implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int LIST = 3;
    public static final int FOOT = 4;
    public static final int TEXT_SPAN_SIZE = 1;
    public static final int IMG_SPAN_SIZE = 3;
    private int itemType;
    private int spanSize;

    public static final int FOOT_MY_GROUP = 100;
    public static final int FOOT_PUBLIC_GROUP= 101;



    public MultipleItem(int itemType) {
        this.itemType = itemType;
    }

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    private String title;
    private int footCategory;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFootCategory() {
        return footCategory;
    }

    public void setFootCategory(int footCategory) {
        this.footCategory = footCategory;
    }

}
