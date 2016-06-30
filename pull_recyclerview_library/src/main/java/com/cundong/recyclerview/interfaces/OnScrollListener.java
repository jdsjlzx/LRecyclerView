package com.cundong.recyclerview.interfaces;


/**
 * Created by lzx on 2016/1/5.
 * RecyclerView
 */
public interface OnScrollListener {

    public abstract void onScrollUp();//scroll down to up

    public abstract void onScrollDown();//scroll from up to down

    public abstract void onBottom();//load next page

    public abstract void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance

}
