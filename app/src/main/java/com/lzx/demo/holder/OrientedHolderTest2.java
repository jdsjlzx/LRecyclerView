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

public class OrientedHolderTest2 extends OrientedBaseHolder {
    private final static String TAG = "TestHolder2";
    TextView tv_2;

    public OrientedHolderTest2(View itemView) {
        super(itemView);
        tv_2 = (TextView) itemView.findViewById(R.id.tv_2);

    }


    @Override
    public void bindItemView(List<HolderOrientedModel> mDataList, int position) {
        int id = mDataList.get(position).getId();
        if (tv_2!=null){

            tv_2.setText("第"+id+"个类型的Item,p = "+position);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Log.d(TAG,"onItemClick pos = " +position);
    }

}
