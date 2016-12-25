package com.lzx.demo.bean;

import com.lzx.demo.type.TypeFactory;

/**
 * Created by lizhixian on 2016/12/24.
 */

public class Category implements Visitable{
    public String title;

    public Category(String title) {
        this.title = title;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
