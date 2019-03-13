package com.lzx.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.CategoryAdapter;
import com.lzx.demo.bean.MultipleItem;
import com.lzx.demo.util.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 手动加载示例
 */
public class ClickLoadingFootActivity extends AppCompatActivity{
    private static final String TAG = "lzx";

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 64;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private LuRecyclerView mRecyclerView = null;

    private CategoryAdapter mMultipleItemAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LuRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_click_loading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.list);

        mMultipleItemAdapter = new CategoryAdapter(this);
        mLRecyclerViewAdapter = new LuRecyclerViewAdapter(mMultipleItemAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMultipleItemAdapter.setLoadMoreListener(new CategoryAdapter.LoadMoreListener() {
            @Override
            public void loadMore(int footCategory) {
                requestData(footCategory);
            }
        });

        /*mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    requestData();
                } else {
                    //the end
                    mRecyclerView.setNoMore(true);
                }
            }
        });*/

        requestData(MultipleItem.FOOT_MY_GROUP);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData(MultipleItem.FOOT_PUBLIC_GROUP);
            }
        },1000);

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<MultipleItem> list, int category) {
        mMultipleItemAdapter.addData(list,category);
    }

    private class PreviewHandler extends Handler {

        private WeakReference<ClickLoadingFootActivity> ref;

        PreviewHandler(ClickLoadingFootActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ClickLoadingFootActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:
                    int category = (int) msg.obj;
                    int currentSize = 0;

                    if (category == MultipleItem.FOOT_MY_GROUP) {
                        currentSize = activity.mMultipleItemAdapter.getSize_first_list();
                    } else if (category == MultipleItem.FOOT_PUBLIC_GROUP) {
                        currentSize = activity.mMultipleItemAdapter.getSize_second_list();
                    }
                    //模拟组装10个数据
                    ArrayList<MultipleItem> newList = new ArrayList<>();
                    MultipleItem item ;

                    if (currentSize == 0) {

                        if (category == MultipleItem.FOOT_MY_GROUP) {
                            activity.mMultipleItemAdapter.setTotalFirstList(33);
                        } else if (category == MultipleItem.FOOT_PUBLIC_GROUP) {
                            //item.setTitle("公开群组");
                            activity.mMultipleItemAdapter.setTotalSecondList(33);
                        }
                    }

                    for (int i = 0; i < 10; i++) {
                        item = new MultipleItem(MultipleItem.LIST);
                        if (category == MultipleItem.FOOT_MY_GROUP) {
                            item.setTitle("我的item"+(currentSize+i));
                        } else if (category == MultipleItem.FOOT_PUBLIC_GROUP) {
                            item.setTitle("公开item"+(currentSize+i));
                        }
                        newList.add(item);
                    }

                    /*if (currentSize == 0) {
                        item = new MultipleItem(MultipleItem.FOOT);
                        item.setFootCategory(category);
                        newList.add(item);
                    } else {
                        if (category == MultipleItem.FOOT_MY_GROUP) {
                            int lastFootIndex = mMultipleItemAdapter.getFootViewIndex(MultipleItem.FOOT_MY_GROUP);
                            Log.e("lzx","FOOT_MY_GROUP lastFootIndex " + lastFootIndex);
                            mMultipleItemAdapter.removeItem(MultipleItem.FOOT_MY_GROUP, lastFootIndex);
                            Log.e("lzx","FOOT_MY_GROUP getSize_first_list() " + mMultipleItemAdapter.getSize_first_list());

                        } else if (category == MultipleItem.FOOT_PUBLIC_GROUP) {
                            int lastFootIndex = mMultipleItemAdapter.getFootViewIndex(MultipleItem.FOOT_PUBLIC_GROUP);
                            Log.e("lzx","FOOT_PUBLIC_GROUP lastFootIndex " + lastFootIndex);
                            mMultipleItemAdapter.removeItem(MultipleItem.FOOT_PUBLIC_GROUP, lastFootIndex);
                            if (mMultipleItemAdapter.getSize_second_list() + newList.size() < 33) {
                                item = new MultipleItem(MultipleItem.FOOT);
                                item.setFootCategory(MultipleItem.FOOT_PUBLIC_GROUP);
                                newList.add(item);
                            }
                        }

                    }*/

                    activity.addItems(newList,category);

                    activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                    activity.notifyDataSetChanged();
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    /*activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                    activity.notifyDataSetChanged();
                    activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            requestData();
                        }
                    });*/
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 模拟请求网络
     */
    private void requestData(final int footCategory) {
        Log.d(TAG, "requestData footCategory " + footCategory);
        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                //模拟一下网络请求失败的情况
                if(NetworkUtils.isNetAvailable(ClickLoadingFootActivity.this)) {
                    mHandler.obtainMessage(-1, footCategory).sendToTarget();
                } else {
                    mHandler.obtainMessage(-3, footCategory).sendToTarget();
                }
            }
        }.start();
    }

}