package com.cundong.recyclerview.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cundong.recyclerview.CustRecyclerView;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.HeaderSpanSizeLookup;
import com.cundong.recyclerview.RecyclerItemClickListener;
import com.cundong.recyclerview.RecyclerOnScrollListener;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.cundong.recyclerview.sample.utils.NetworkUtils;
import com.cundong.recyclerview.sample.utils.RecyclerViewStateUtils;
import com.cundong.recyclerview.sample.weight.LoadingFooter;
import com.cundong.recyclerview.sample.weight.SampleHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cundong on 2015/10/29.
 *
 * 带HeaderView的分页加载GridLayout RecyclerView
 */
public class EndlessGridLayoutActivity extends AppCompatActivity {

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 34;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private int mCurrentCounter = 0;

    private CustRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        mRecyclerView = (CustRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add("item" + i);
        }

        mCurrentCounter = dataList.size();

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.addAll(dataList);

        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(this, mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        //setLayoutManager
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        mRecyclerView.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String text = mDataAdapter.getDataList().get(position);
                Toast.makeText(EndlessGridLayoutActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                String text = mDataAdapter.getDataList().get(position);
                Toast.makeText(EndlessGridLayoutActivity.this, "onItemLongClick - " + text, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<String> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private RecyclerOnScrollListener mOnScrollListener = new RecyclerOnScrollListener() {

        @Override
        public void onBottom() {
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(EndlessGridLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(EndlessGridLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private static class PreviewHandler extends Handler {

        private WeakReference<EndlessGridLayoutActivity> ref;

        PreviewHandler(EndlessGridLayoutActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final EndlessGridLayoutActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();

                    //模拟组装10个数据
                    ArrayList<String> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        newList.add("item" + (currentSize + i));
                    }

                    activity.addItems(newList);
                    RecyclerViewStateUtils.setFooterViewState(activity.mRecyclerView, LoadingFooter.State.Normal);
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, activity.mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
                    break;
            }
        }
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(EndlessGridLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if(NetworkUtils.isNetAvailable(EndlessGridLayoutActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    private class DataAdapter extends RecyclerView.Adapter {

        private LayoutInflater mLayoutInflater;
        private ArrayList<String> mDataList = new ArrayList<>();

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        private void addAll(ArrayList<String> list) {
            int lastIndex = this.mDataList.size();
            if (this.mDataList.addAll(list)) {
                notifyItemRangeInserted(lastIndex, list.size());
            }
        }

        public List<String> getDataList() {
            return mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_card, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            String item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(item);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);
            }
        }
    }
}