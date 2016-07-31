package com.lzx.demo.interfaces;

/**
 * Created by Oubowu on 2016/7/24 23:53.
 * <p>
 * 顶部标签点击监听
 */
public interface OnHeaderClickListener<T> {

    void onHeaderClick(int id, int position, T data);

    void onHeaderLongClick(int id, int position, T data);

    void onHeaderDoubleClick(int id, int position, T data);

}
