package com.lzx.demo.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzx.demo.R;
import com.lzx.demo.adapter.ProductsAdapter;
import com.lzx.demo.bean.Product;
import com.lzx.demo.bean.ProductList;
import com.lzx.demo.bean.Visitable;

import java.util.List;


/**
 * Created by lizhixian on 2016/12/24.
 */

public class ProductListViewHolder extends BetterViewHolder {

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    public ProductListViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ProductsAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bindItem(Visitable visitable) {
        ProductList productList = (ProductList) visitable;
        List<Product> products = productList.products;
        adapter.setData(products);
        adapter.notifyDataSetChanged();
    }

}
