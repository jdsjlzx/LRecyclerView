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

public class OrientedHolderTest1 extends OrientedBaseHolder {
    private final static String TAG = "TestHolder";
    TextView tv_1;

    public OrientedHolderTest1(View itemView) {
        super(itemView);
        tv_1 = (TextView) itemView.findViewById(R.id.tv_1);
    }

    @Override
    public void bindItemView(List<HolderOrientedModel> mDataList, int position) {

        int id = mDataList.get(position).getId();
        if (tv_1!=null){

            tv_1.setText("第"+id+"个类型的Item,i = "+position);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Log.d(TAG,"onItemClick pos = " +position);
    }

}
