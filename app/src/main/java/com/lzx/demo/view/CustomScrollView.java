package com.lzx.demo.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
    private int slop;
    private int touch;

    public Callbacks mCallbacks;

    public CustomScrollView(Context context) {
        super(context);
        setSlop(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSlop(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSlop(context);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(l, t, oldl, oldt);
        }
    }
    //定义接口用于回调
    public interface Callbacks {
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //保存当前touch的纵坐标值
                touch = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //保存当前touch的纵坐标值
                touch = (int) ev.getRawY();
                //  滑动距离大于slop值时，返回true
                if (Math.abs((int) ev.getRawY() - touch) > slop)  {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }*/


    /**
     * 获取相应context的touch slop值（即在用户滑动之前，能够滑动的以像素为单位的距离）
     * @param context ScrollView对应的context
     */
    private void setSlop(Context context) {
        slop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
}
