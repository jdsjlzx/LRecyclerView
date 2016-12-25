package com.lzx.demo.type;

import android.view.View;

import com.lzx.demo.bean.Category;
import com.lzx.demo.bean.HotList;
import com.lzx.demo.bean.ProductList;
import com.lzx.demo.holder.BetterViewHolder;

public interface TypeFactory {

    int type(Category title);
    int type(ProductList products);
    int type(HotList products);

    BetterViewHolder onCreateViewHolder(View itemView, int viewType);

}
