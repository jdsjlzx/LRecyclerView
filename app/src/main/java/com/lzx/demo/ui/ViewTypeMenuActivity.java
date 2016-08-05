package com.lzx.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.Closeable;
import com.github.jdsjlzx.interfaces.OnItemClickLitener;
import com.github.jdsjlzx.interfaces.OnSwipeMenuItemClickListener;
import com.github.jdsjlzx.interfaces.SwipeMenuCreator;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.swipe.SwipeMenu;
import com.github.jdsjlzx.swipe.SwipeMenuItem;
import com.github.jdsjlzx.util.RecyclerViewUtils;
import com.lzx.demo.ItemDecoration.ListViewDecoration;
import com.lzx.demo.R;
import com.lzx.demo.adapter.MenuAdapter;
import com.lzx.demo.adapter.MenuViewTypeAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.AppToast;
import com.lzx.demo.weight.SampleFooter;
import com.lzx.demo.weight.SampleHeader;

import java.util.ArrayList;

public class ViewTypeMenuActivity extends AppCompatActivity {
    private Activity mContext;

    private LRecyclerView mRecyclerView = null;

    private MenuAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ItemModel itemModel = new ItemModel();
            if(i % 2 == 0) {
                itemModel.title = "item" + i + "   我有菜单";
            } else {
                itemModel.title = "item" + i + "   我没有菜单";
            }
            dataList.add(itemModel);
        }

        mDataAdapter = new MenuViewTypeAdapter();
        mDataAdapter.setDataList(dataList);

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mRecyclerView.openLeftMenu(0);
        mRecyclerView.openRightMenu(0);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setPullRefreshEnabled(false);

        RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

        //add a FooterView
        RecyclerViewUtils.setFooterView(mRecyclerView, new SampleFooter(this));

        mLRecyclerViewAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String text = mDataAdapter.getDataList().get(position).title;
                Toast.makeText(ViewTypeMenuActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {


            }
        });


    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            if (viewType == MenuViewTypeAdapter.VIEW_TYPE_NONE) {// 根据Adapter的ViewType来决定菜单的样式、颜色等属性、或者是否添加菜单。
                // Do nothing.
            } else if (viewType == MenuViewTypeAdapter.VIEW_TYPE_MENU) {
                int size = getResources().getDimensionPixelSize(R.dimen.item_height);

                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.mipmap.ic_action_wechat)
                        .setText("微信")
                        .setWidth(size)
                        .setHeight(size);

                swipeLeftMenu.addMenuItem(deleteItem);
                swipeRightMenu.addMenuItem(deleteItem);

                SwipeMenuItem wechatItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_add)
                        .setText("添加")
                        .setWidth(size)
                        .setHeight(size);

                swipeLeftMenu.addMenuItem(wechatItem);
                swipeRightMenu.addMenuItem(wechatItem);
            }
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == LRecyclerView.RIGHT_DIRECTION) {
                AppToast.showShortText(ViewTypeMenuActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition);
            } else if (direction == LRecyclerView.LEFT_DIRECTION) {
                AppToast.showShortText(ViewTypeMenuActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}