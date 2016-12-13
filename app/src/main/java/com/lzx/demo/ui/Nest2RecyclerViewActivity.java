package com.lzx.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.RelatedGoodsAdapter;
import com.lzx.demo.base.Entity;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.bean.Goods;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.TLog;
import com.lzx.demo.view.SampleHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView嵌套RecyclerView
 * 参考：http://blog.csdn.net/z593492734/article/details/51492472
 */
public class Nest2RecyclerViewActivity extends AppCompatActivity{
    private static final String TAG = "lzx";

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private List<Entity> mItemModels = new ArrayList<>();
    private List<Goods> mGoodsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);
        mRecyclerView.setNestedScrollingEnabled(false);

        mDataAdapter = new DataAdapter(this,mGoodsList);

        //setLayoutManager must before setAdapter
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                //这里指定第一个位置为横向滑动列表，在实际项目中可以根据 mItemModels.get(position) instanceof XX 的结果来判断
                TLog.error("11 getSpanSize ");
                switch(mDataAdapter.getItemViewType(position)){
                    case DataAdapter.TYPE_LIST_ITEMS:
                        TLog.error("11 getSpanSize " + position);
                        return layoutManager.getSpanCount();
                    case DataAdapter.TYPE_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);

        initData();

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mDataAdapter.addAll(mItemModels);


        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete();
                    }
                },1000);
            }
        });

        mRecyclerView.setRefreshing(true);

    }

    private void initData() {
        for (int i = 0; i < 12; i++) {
            ItemModel item = new ItemModel();
            item.id = i;
            item.title = "item" + (item.id);
            mItemModels.add(item);
        }

        for (int i = 0; i < 20; i++) {
            Goods goods = new Goods();
            goods.price = String.valueOf(100+i);
            goods.title = "正品羊毛衫"+i;
            mGoodsList.add(goods);
        }

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Entity> list) {

        mDataAdapter.addAll(list);

    }

    private class DataAdapter extends ListBaseAdapter<Entity> {
        private static final int TYPE_ITEM = 1001;
        private static final int TYPE_LIST_ITEMS = 1002;

        private LayoutInflater mLayoutInflater;
        private List<Goods> mGoodsList = new ArrayList<>();

        public DataAdapter(Context context, List<Goods> goodsList) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
            this.mGoodsList = goodsList;
        }

        @Override
        public int getItemViewType(int position) {

            int type = super.getItemViewType(position);

            if (position == 0) { //指定第一个位置为横向滑动列表
                type = TYPE_LIST_ITEMS;
            } else {
                type = TYPE_ITEM;
            }

            return type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            RecyclerView.ViewHolder customItemViewHolder = null;
            switch (viewType) {
                case TYPE_ITEM:
                    view = mLayoutInflater.inflate(R.layout.sample_item_card, parent, false);
                    customItemViewHolder = new TextViewHolder(view);
                    break;
                case TYPE_LIST_ITEMS:
                    view = mLayoutInflater.inflate(R.layout.layout_list_item_goods_related, parent, false);
                    customItemViewHolder = new RelatedGoodsViewHolder(view);

                    break;
                default:
                    break;
            }



            return customItemViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof TextViewHolder) {
                bindTextViewHolder((TextViewHolder) holder, (ItemModel) mDataList.get(position));
            } else if (holder instanceof RelatedGoodsViewHolder) {
                bindRelatedGoodsViewHolder((RelatedGoodsViewHolder) holder);
            }

        }

        private class TextViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public TextViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);
            }
        }

        private class RelatedGoodsViewHolder extends RecyclerView.ViewHolder {

            protected RecyclerView relatedItemsRecyclerView;

            public RelatedGoodsViewHolder(View itemView) {
                super(itemView);
                relatedItemsRecyclerView = (RecyclerView) itemView.findViewById(R.id.related_recyclerview);

            }
        }

        private void bindTextViewHolder(TextViewHolder holder, final ItemModel itemModel) {

        }

        private void bindRelatedGoodsViewHolder(final RelatedGoodsViewHolder holder) {
            final LinearLayoutManager layoutManager
                    = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            RelatedGoodsAdapter adapter = new RelatedGoodsAdapter(mContext, mGoodsList);
            holder.relatedItemsRecyclerView.setAdapter(adapter);
            holder.relatedItemsRecyclerView.setLayoutManager(layoutManager);
            holder.relatedItemsRecyclerView.setHasFixedSize(true);
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_refresh) {
            mRecyclerView.forceToRefresh();
        }
        return true;
    }
}