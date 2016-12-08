package com.github.jdsjlzx.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Lzx on 2016/12/8.
 */

public class InnerRecyclerView extends RecyclerView {

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;

    public InnerRecyclerView(Context context) {
        super(context);
    }

    public InnerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("lzx","onInterceptTouchEvent");

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("lzx","onTouchEvent");
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指在屏幕滑动的时候
            x2 = event.getX();
            y2 = event.getY();
            boolean isUpSlip = Math.abs(y1-y2)>Math.abs(x1-x2)&&Math.abs(y1-y2)>=5;
            boolean isDownSlip = Math.abs(y2-y1)>Math.abs(x2-x1)&&Math.abs(y2-y1)>=5;
            if(isUpSlip || isDownSlip) {//上下滑动
                // 通知父view是否要处理touch事件
                Log.e("lzx","通知父view是否要处理touch事件");
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);

            }
        }
        return super.onTouchEvent(event);
    }
}
