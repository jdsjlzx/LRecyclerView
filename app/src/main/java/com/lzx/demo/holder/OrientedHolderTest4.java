package com.lzx.demo.holder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.bean.HolderOrientedModel;

import java.util.List;


/**
 * Created by 杜坤鹏 on 2019/5/28.
 */

public class OrientedHolderTest4 extends OrientedBaseHolder {
    private final static String TAG = "TestHolder4";
    TextView tv_4;

    public OrientedHolderTest4(View itemView) {
        super(itemView);
        tv_4 = (TextView) itemView.findViewById(R.id.tv_4);

    }


    @Override
    public void bindItemView(List<HolderOrientedModel> mDataList, int position) {
        int id = mDataList.get(position).getId();
        if (tv_4!=null){

            tv_4.setText("第"+id+"个类型的Item,position = "+position);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Log.d(TAG,"onItemClick pos = " +position);
    }
}
