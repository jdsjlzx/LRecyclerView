package com.lzx.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import com.github.jdsjlzx.swipe.touch.OnItemMoveListener;
import com.lzx.demo.ItemDecoration.ListViewDecoration;
import com.lzx.demo.R;
import com.lzx.demo.adapter.MenuAdapter;
import com.lzx.demo.bean.ItemModel;

import java.util.ArrayList;
import java.util.Collections;

public class ListDragMenuActivity extends AppCompatActivity {
    private Activity mContext;

    private LRecyclerView mRecyclerView = null;

    private MenuAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        mContext = this;
        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
        }

        mDataAdapter = new MenuAdapter();
        mDataAdapter.setDataList(dataList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setPullRefreshEnabled(false);

        mRecyclerView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
        mRecyclerView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。

        mLRecyclerViewAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String text = mDataAdapter.getDataList().get(position).title;
                Toast.makeText(ListDragMenuActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {


            }
        });


    }

    /**
     * 当Item移动的时候。
     */
    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            // 当Item被拖拽的时候。
            Collections.swap(mDataAdapter.getDataList(), fromPosition, toPosition);
            mDataAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;// 返回true表示处理了，返回false表示你没有处理。
        }

        @Override
        public void onItemDismiss(int position) {
            // 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
            // 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
        }
    };

    /**
     * 菜单创建器
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int size = getResources().getDimensionPixelSize(R.dimen.item_height);

            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                    .setBackgroundDrawable(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_delete)
                    .setWidth(size)
                    .setHeight(size);

            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧菜单。

            SwipeMenuItem wechatItem = new SwipeMenuItem(mContext)
                    .setBackgroundDrawable(R.drawable.selector_green)
                    .setImage(R.mipmap.ic_action_wechat)
                    .setWidth(size)
                    .setHeight(size);

            swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。
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
                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == LRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };

}