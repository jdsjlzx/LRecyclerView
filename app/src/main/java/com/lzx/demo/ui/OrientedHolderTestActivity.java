package com.lzx.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzx.demo.R;
import com.lzx.demo.adapter.OrientedHolderTestAdapter;
import com.lzx.demo.bean.HolderOrientedModel;

import java.util.ArrayList;

/**
 * Created by 杜坤鹏 on 2019/5/28.
 */

public class OrientedHolderTestActivity extends Activity {
    private RecyclerView myRecyclerView;
    private ArrayList<HolderOrientedModel> mList;
    private OrientedHolderTestAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oriented_holder_test);
        ArrayList<HolderOrientedModel> list = initTestData();

        myAdapter = new OrientedHolderTestAdapter(OrientedHolderTestActivity.this);
        myRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        myRecyclerView.setAdapter(myAdapter);
        myAdapter.setData(list);

        findViewById(R.id.btn_mybutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reLoadData();
            }
        });
    }


    /**
     * 模拟测试用数据
     * @return
     */
    @NonNull
    private ArrayList<HolderOrientedModel> initTestData() {
        mList = new ArrayList<HolderOrientedModel>();
        HolderOrientedModel baseModel = new HolderOrientedModel();
        baseModel.setId(1);
        baseModel.setType(1);

        HolderOrientedModel baseModel2 = new HolderOrientedModel();
        baseModel2.setId(2);
        baseModel2.setType(2);

        HolderOrientedModel baseModel3 = new HolderOrientedModel();
        baseModel3.setId(3);
        baseModel3.setType(3);
        mList.add(baseModel);

        for (int i =0;i<20;i++){
            if (i==8){
                mList.add(baseModel3);
            }else {
                mList.add(baseModel2);
            }
        }
        return mList;
    }

    /**
     * 模拟重新加载网络数据,新增一个类型的Item
     */
    private void reLoadData() {
        if (mList==null)return;
        HolderOrientedModel modle4 = new HolderOrientedModel();
        modle4.setId(4);
        modle4.setType(4);
        for (int i =0;i<mList.size();i++){
            if (i==2){
                mList.add(2,modle4);
            }
        }
        myAdapter.setData(mList);

    }
}
