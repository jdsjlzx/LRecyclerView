package com.github.jdsjlzx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.R;


/**
 * RecyclerView的HeaderView，简单的展示一个TextView
 */
public class CommonHeader extends RelativeLayout {
    int resId;
    public CommonHeader(Context context,int resId) {
        super(context);
        init(context,resId);
    }

    public CommonHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.layout_recyclerview_header, this);
    }
    public void init(Context context,int resId) {

        inflate(context, resId, this);
    }
}