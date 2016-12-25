package com.lzx.demo.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzx.demo.R;
import com.lzx.demo.adapter.HotsAdapter;
import com.lzx.demo.bean.HotList;
import com.lzx.demo.bean.Product;
import com.lzx.demo.bean.Visitable;

import java.util.List;


/**
 * Created by lizhixian on 2016/12/24.
 */

public class HotListViewHolder extends BetterViewHolder {

    private RecyclerView recyclerView;
    private HotsAdapter adapter;
    public HotListViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HotsAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bindItem(Visitable visitable) {
        HotList hotList = (HotList) visitable;
        List<Product> products = hotList.products;
        adapter.setData(products);
        adapter.notifyDataSetChanged();
    }

}
