package com.lzx.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.base.SuperViewHolder;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.AppToast;
import com.lzx.demo.util.AppUtil;
import com.lzx.demo.util.NetworkUtils;
import com.lzx.demo.view.SampleHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 *结合SwipeRefreshLayout的分页加载LinearLayout RecyclerView
 */
public class SwipeRefreshLayoutActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "lzx";

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 34;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_swpierefresh_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);


        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this,48));
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        mDataAdapter = new DataAdapter(this);

        //setLayoutManager放在setAdapter之前配置
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //GridLayoutManager manager = new GridLayoutManager(this, 2);
        //mRecyclerView.setLayoutManager(manager);

        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);

        LuDividerDecoration divider = new LuDividerDecoration.Builder(this,mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);



        mLuRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        //如果使用了自动加载更多，就不要添加FooterView了
        //mLuRecyclerViewAdapter.addFooterView(new SampleFooter(this));

        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                AppToast.showShortText(SwipeRefreshLayoutActivity.this, item.title);
            }

        });

        mLuRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                AppToast.showShortText(SwipeRefreshLayoutActivity.this, "onItemLongClick - " + item.title);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
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
        });

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");

        onRefresh();
    }

    @Override
    public void onRefresh() {
        mCurrentCounter = 0;
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<ItemModel> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<SwipeRefreshLayoutActivity> ref;

        PreviewHandler(SwipeRefreshLayoutActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SwipeRefreshLayoutActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:
                    if(activity.mSwipeRefreshLayout.isRefreshing()){
                        activity.mDataAdapter.clear();
                        mCurrentCounter = 0;
                    }

                    int currentSize = activity.mDataAdapter.getItemCount();

                    //模拟组装10个数据
                    ArrayList<ItemModel> newList = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        ItemModel item = new ItemModel();
                        item.id = currentSize + i;
                        item.title = "item" + (item.id);

                        newList.add(item);
                    }


                    activity.addItems(newList);

                    if(mSwipeRefreshLayout.isRefreshing()){
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                    }
                    activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    if(mSwipeRefreshLayout.isRefreshing()){
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                        activity.notifyDataSetChanged();
                    }else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                                activity.notifyDataSetChanged();
                                requestData();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 模拟请求网络
     */
    private void requestData() {
        Log.d(TAG, "requestData");

        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if(NetworkUtils.isNetAvailable(SwipeRefreshLayoutActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    private class DataAdapter extends ListBaseAdapter<ItemModel> {

        public DataAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.list_item_text;
        }

        @Override
        public void onBindItemHolder(SuperViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            TextView titleText = holder.getView(R.id.info_text);
            titleText.setText(item.title);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}