package com.lzx.demo.type;

import android.view.View;

import com.lzx.demo.R;
import com.lzx.demo.bean.Category;
import com.lzx.demo.bean.HotList;
import com.lzx.demo.bean.ProductList;
import com.lzx.demo.holder.BetterViewHolder;
import com.lzx.demo.holder.CategoryViewHolder;
import com.lzx.demo.holder.HotListViewHolder;
import com.lzx.demo.holder.ProductListViewHolder;


public class ListTypeFactory implements TypeFactory {


    @Override
    public int type(Category category) {
        return R.layout.layout_list_item_category;
    }

    @Override
    public int type(ProductList products) {
        return R.layout.layout_item_list;
    }

    @Override
    public int type(HotList products) {
        return R.layout.layout_item_horizontal_list;
    }

    @Override
    public BetterViewHolder onCreateViewHolder(View itemView, int viewType) {
        BetterViewHolder viewHolder = null;
        switch (viewType) {
            case R.layout.layout_list_item_category:
                viewHolder = new CategoryViewHolder(itemView);
                break;
            case R.layout.layout_item_list:
                viewHolder = new ProductListViewHolder(itemView);
                break;
            case R.layout.layout_item_horizontal_list:
                viewHolder = new HotListViewHolder(itemView);
                break;
            default:
                break;
        }

        return viewHolder;
    }
}
