package com.lzx.demo.adapter;

public class MenuViewTypeAdapter extends MenuAdapter {

    public static final int VIEW_TYPE_MENU = 1;
    public static final int VIEW_TYPE_NONE = 2;

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? VIEW_TYPE_MENU : VIEW_TYPE_NONE;
    }
}
