package com.lzx.demo.holder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzx.demo.R;


/**
 * Created by 杜坤鹏 on 2019/5/28.
 * 根据不同的type生产Holder
 */

public class OrientedHolderFactory {
    private final static String TAG = "HolderFactory";
    private final static int HOLDER_DEFULT = 0;
    private final static int HOLDER_ITEM_ONE = HOLDER_DEFULT+1;
    private final static int HOLDER_ITEM_TWO = HOLDER_ITEM_ONE+1;
    private final static int HOLDER_ITEM_THREE = HOLDER_ITEM_TWO+1;
    private final static int HOLDER_ITEM_FOUR = HOLDER_ITEM_THREE+1;


    public static OrientedBaseHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent , int viewType){
        Log.d(TAG,"createViewHolder viewType = "+viewType);
        OrientedBaseHolder baseHoler=null;
        switch (viewType){
            case HOLDER_ITEM_ONE:
                View view = mInflater.inflate(R.layout.oriented_holder_test_item_1,parent,false);
                baseHoler = new OrientedHolderTest1(view);
                break;
            case HOLDER_ITEM_TWO:
                View view2 = mInflater.inflate(R.layout.oriented_holder_test_item_2,parent,false);
                baseHoler = new OrientedHolderTest2(view2);
                break;
            case HOLDER_ITEM_THREE:
                View view3 = mInflater.inflate(R.layout.oriented_holder_test_item_3,parent,false);
                baseHoler = new OrientedHolderTest3(view3);
                break;

            case HOLDER_ITEM_FOUR:
                View view4 = mInflater.inflate(R.layout.oriented_holder_test_item_4,parent,false);
                baseHoler = new OrientedHolderTest4(view4);
                break;

            case HOLDER_DEFULT:
                View viewdefult = mInflater.inflate(R.layout.oriented_holder_test_item_3,parent,false);
                baseHoler = new OrientedHolderDefult(viewdefult);
               break;
        }
        return baseHoler;

    }



}
