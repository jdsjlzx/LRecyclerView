package com.lzx.demo.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.base.SuperViewHolder;
import com.lzx.demo.bean.Goods;

import java.util.List;

/**
 * Created by lizhixian on 2016/11/19.
 */

public class GoodsAdapter extends ListBaseAdapter<Goods> {

    public GoodsAdapter(Context context, List<Goods> list) {
        super(context);
        setDataList(list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_list_item_nest_inner;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView titleTextView = holder.getView(R.id.title);
        titleTextView.setText(getDataList().get(position).title);
    }

}
