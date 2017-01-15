package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.base.BaseMultiAdapter;
import com.lzx.demo.base.SuperViewHolder;
import com.lzx.demo.bean.Goods;
import com.lzx.demo.bean.MultipleItem;
import com.lzx.demo.util.HorizontalItemDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lzx on 2016/12/30.
 */

public class ShopAdapterBase extends BaseMultiAdapter<MultipleItem> {

    private List<Goods> goodsList = new ArrayList<>();

    public ShopAdapterBase(Context context, List<Goods> goodsList) {
        super(context);
        addItemType(MultipleItem.TEXT, R.layout.list_item_text);
        addItemType(MultipleItem.LIST, R.layout.layout_list_item_goods_related);
        this.goodsList = goodsList;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MultipleItem item = getDataList().get(position);
        switch (item.getItemType()) {
            case MultipleItem.TEXT:
                bindTextItem(holder,item);
                break;
            case MultipleItem.LIST:
                bindListItem(holder,item);
                break;
            default:
                break;
        }

    }

    private void bindTextItem(SuperViewHolder holder, MultipleItem item) {
        TextView textView = holder.getView(R.id.info_text);
        textView.setText(item.getTitle());
    }

    private void bindListItem(SuperViewHolder holder, MultipleItem item) {
        RecyclerView recyclerView = holder.getView(R.id.related_recyclerview);
        final LinearLayoutManager layoutManager  = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        RelatedGoodsAdapter adapter = new RelatedGoodsAdapter(mContext, goodsList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalItemDecorator((int) mContext.getResources().getDimension(R.dimen.padding_size_small)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }


}
