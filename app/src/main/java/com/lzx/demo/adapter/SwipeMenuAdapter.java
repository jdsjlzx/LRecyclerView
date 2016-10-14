package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.base.ListBaseAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.AppToast;
import com.lzx.demo.view.SwipeMenuView;

/**
 * 参考：https://github.com/mcxtzhang/SwipeDelMenuViewGroup
 */

public class SwipeMenuAdapter extends ListBaseAdapter<ItemModel> {
    private Context mContext;
    private LayoutInflater mInfalter;

    public SwipeMenuAdapter(Context context) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
    }

    @Override
    public SwipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SwipeViewHolder(mInfalter.inflate(R.layout.list_item_swipe, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SwipeViewHolder viewHolder = (SwipeViewHolder) holder;
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView) viewHolder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);

        viewHolder.title.setText(getDataList().get(position).title + (position % 2 == 0 ? "我只能右滑动" : "我只能左滑动"));

        //隐藏控件
        viewHolder.btnUnRead.setVisibility(position % 3 == 0 ? View.GONE : View.VISIBLE);

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        (viewHolder.contentView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToast.makeShortToast(mContext, getDataList().get(position).title);
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
        //置顶：
        viewHolder.btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnSwipeListener){
                    mOnSwipeListener.onTop(position);
                }

            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    private class SwipeViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        TextView title;
        Button btnDelete;
        Button btnUnRead;
        Button btnTop;

         SwipeViewHolder(View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.swipe_content);
            title = (TextView) itemView.findViewById(R.id.title);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnUnRead = (Button) itemView.findViewById(R.id.btnUnRead);
            btnTop = (Button) itemView.findViewById(R.id.btnTop);
        }
    }
}

