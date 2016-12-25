package com.lzx.demo.holder;

import android.view.View;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.bean.Category;
import com.lzx.demo.bean.Visitable;


/**
 * Created by lizhixian on 2016/12/24.
 */

public class CategoryViewHolder extends BetterViewHolder {

    private TextView titleText;
    public CategoryViewHolder(View itemView) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void bindItem(Visitable visitable) {
        Category category = (Category) visitable;
        titleText.setText(category.title);

    }
}
