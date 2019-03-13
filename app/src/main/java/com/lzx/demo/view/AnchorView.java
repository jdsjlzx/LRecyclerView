package com.lzx.demo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzx.demo.R;
import com.lzx.demo.adapter.DataAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.AppToast;

import java.util.Collection;

public class AnchorView extends LinearLayout {

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 34;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private TextView tvAnchor;
    private LuRecyclerView mRecyclerView = null;
    private DataAdapter mDataAdapter = null;
    private LuRecyclerViewAdapter mLRecyclerViewAdapter = null;

    public AnchorView(Context context) {
        this(context, null);
    }

    public AnchorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnchorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_anchor, this, true);
        tvAnchor = view.findViewById(R.id.tv_anchor);

        mRecyclerView = findViewById(R.id.list);

        mRecyclerView.setNestedScrollingEnabled(false);

        mDataAdapter = new DataAdapter(context);
        mLRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setManualLoadMore(true);


        ClickLoadingFooter custLoadingFooter = new ClickLoadingFooter(context);
        custLoadingFooter.setProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadMoreFooter(custLoadingFooter,true);

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    Log.e("lzx","load more");
                    loadMoreListener.onLoadMore();
                }
            }
        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {
                    ItemModel item = mDataAdapter.getDataList().get(position);
                    AppToast.showShortText(getContext().getApplicationContext(), item.title);
                }

            }

        });


    }

    public void setAnchorTxt(String txt) {
        tvAnchor.setText(txt);
    }

    public void addAll(Collection list) {
        mDataAdapter.addAll(list);
        mRecyclerView.refreshComplete(REQUEST_COUNT);
        Log.e("lzx","addAll " + mDataAdapter.getDataList().size());
        //mLRecyclerViewAdapter.notifyDataSetChanged();
        //requestLayout();
        if (mDataAdapter.getDataList().size() >= TOTAL_COUNTER) {
            mRecyclerView.setNoMore(true,false);
        } else {
            mRecyclerView.refreshComplete(REQUEST_COUNT);
        }

    }

    public int getItemCount() {
        return mDataAdapter.getItemCount();
    }

    LoadMoreListener loadMoreListener;

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    public interface LoadMoreListener {
        void  onLoadMore();
    }
}
