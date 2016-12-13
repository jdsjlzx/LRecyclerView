package com.lzx.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzx.demo.R;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.AppToast;
import com.lzx.demo.view.SampleFooter;
import com.lzx.demo.view.SampleHeader;

import java.util.ArrayList;
import java.util.List;

public class MulItemGridLayoutActivity extends AppCompatActivity{
    private static final String TAG = "lzx";

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<ItemModel> mItemModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        initData();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        mDataAdapter = new DataAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mDataAdapter.addAll(mItemModels);

        mLRecyclerViewAdapter.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if (position == 3) {
                    return gridLayoutManager.getSpanCount();
                } else if (position == 7 ) {
                    return gridLayoutManager.getSpanCount() - 1;
                } else {
                    return 1;
                }

            }
        });


        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        mLRecyclerViewAdapter.addHeaderView( new SampleHeader(this));

        SampleFooter sampleFooter = new SampleFooter(this);
        mLRecyclerViewAdapter.addFooterView(sampleFooter);

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

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                AppToast.showShortText(MulItemGridLayoutActivity.this, item.title);
            }
        });

        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                AppToast.showShortText(MulItemGridLayoutActivity.this, "onItemLongClick - " + item.title);
            }
        });


    }



    private void initData() {
        for (int i = 0; i < 18; i++) {
            ItemModel item = new ItemModel();
            item.id = i;
            item.title = "item" + (item.id);
            mItemModels.add(item);
        }
    }

    private class DataAdapter extends ListBaseAdapter<ItemModel> {
        private static final int TYPE_ITEM = 0; //普通类型
        private static final int TYPE_PHOTO_ITEM = 1; //图文类型

        private LayoutInflater mLayoutInflater;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if (position %2 == 0) {
                return TYPE_PHOTO_ITEM;
            } else {
                return TYPE_ITEM;
            }


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case TYPE_PHOTO_ITEM://图文类型
                    return new PhotoViewHolder(mLayoutInflater.inflate(R.layout.list_item_pic, parent, false));
                default:
                    return new TextViewHolder(mLayoutInflater.inflate(R.layout.list_item_text, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemModel item = mDataList.get(position);

            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case TYPE_PHOTO_ITEM:
                    PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
                    photoViewHolder.textView.setText(item.title);
                    photoViewHolder.avatarImage.setImageResource(R.mipmap.icon);
                    break;
                default:
                    TextViewHolder viewHolder = (TextViewHolder) holder;
                    viewHolder.textView.setText(item.title);
                    break;
            }

        }

        private class TextViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public TextViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);
            }
        }
        private class PhotoViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private ImageView avatarImage;

            public PhotoViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);
                avatarImage = (ImageView) itemView.findViewById(R.id.avatar_image);
            }
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
            //mDataAdapter.remove(mLRecyclerViewAdapter.getAdapterPosition(false,3));
        }
        return true;
    }
}