package com.github.jdsjlzx.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.swipe.SwipeMenuAdapter;
import com.github.jdsjlzx.swipe.SwipeMenuLayout;
import com.github.jdsjlzx.swipe.SwipeMenuView;
import com.github.jdsjlzx.view.ArrowRefreshHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter with Header and Footer
 * 
 */
public class LRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_REFRESH_HEADER = 10000;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER_VIEW = 10001;
    private static final int HEADER_INIT_INDEX = 10002;
    private static List<Integer> mHeaderTypes = new ArrayList<>();

    private boolean pullRefreshEnabled = true;
    private int mRefreshProgressStyle = ProgressStyle.SysProgress;
    private ArrowRefreshHeader mRefreshHeader;

    /**
     * RecyclerView使用的，真正的Adapter
     */
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart + getHeaderViewsCount() + 1, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart + getHeaderViewsCount() + 1, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart + getHeaderViewsCount() + 1, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int headerViewsCountCount = getHeaderViewsCount();
            notifyItemRangeChanged(fromPosition + headerViewsCountCount + 1, toPosition + headerViewsCountCount + 1+ itemCount);
        }
    };

    public LRecyclerViewAdapter(Context context, RecyclerView.Adapter innerAdapter) {
        mContext = context;
        this.mInnerAdapter = innerAdapter;
        //setRefreshHeader();

        //setAdapter(innerAdapter);
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    public void setRefreshHeader(){
        if (pullRefreshEnabled) {
            ArrowRefreshHeader refreshHeader = new ArrowRefreshHeader(mContext);
            refreshHeader.setProgressStyle(mRefreshProgressStyle);
            mRefreshHeader = refreshHeader;

        }
    }

    public void setRefreshHeader(ArrowRefreshHeader refreshHeader){
        mRefreshHeader = refreshHeader;
    }

    public ArrowRefreshHeader getRefreshHeader(){
        return mRefreshHeader;
    }

    /**
     * 设置adapter
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {

        if (mInnerAdapter != null) {
            notifyItemRangeRemoved(getHeaderViewsCount(), mInnerAdapter.getItemCount());
            mInnerAdapter.unregisterAdapterDataObserver(mDataObserver);
        }

        this.mInnerAdapter = adapter;
        mInnerAdapter.registerAdapterDataObserver(mDataObserver);
        notifyItemRangeInserted(getHeaderViewsCount(), mInnerAdapter.getItemCount());

    }

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public void addHeaderView(View view) {

        if (view == null) {
            throw new RuntimeException("header is null");
        }

        mHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
    }

    public void addFooterView(View footer) {

        if (footer == null) {
            throw new RuntimeException("footer is null");
        }

        mFooterViews.add(footer);
        this.notifyDataSetChanged();
    }

    /**
     * 根据header的ViewType判断是哪个header
     * @param itemType
     * @return
     */
    private View getHeaderViewByType(int itemType) {
        if(!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    /**
     * 判断一个type是否为HeaderType
     * @param itemViewType
     * @return
     */
    private boolean isHeaderType(int itemViewType) {
        return  mHeaderViews.size() > 0 &&  mHeaderTypes.contains(itemViewType);
    }

    /**
     * 返回第一个FootView
     * @return
     */
    public View getFooterView() {
        return  getFooterViewsCount()>0 ? mFooterViews.get(0) : null;
    }

    /**
     * 返回第一个HeaderView
     * @return
     */
    public View getHeaderView() {
        return  getHeaderViewsCount()>0 ? mHeaderViews.get(0) : null;
    }

    public ArrayList<View> getHeaderViews() {
        return mHeaderViews;
    }

    public void removeHeaderView(View view) {
        mHeaderViews.remove(view);
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        mFooterViews.remove(view);
        this.notifyDataSetChanged();
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public boolean isHeader(int position) {
        return position >= 1 && position < mHeaderViews.size() + 1;
    }

    public boolean isRefreshHeader(int position) {
        return position == 0;
    }

    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - getFooterViewsCount();
        return getFooterViewsCount() > 0 && position >= lastPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_REFRESH_HEADER) {
            return new ViewHolder(mRefreshHeader);
        } else if (isHeaderType(viewType)) {
            return new ViewHolder(getHeaderViewByType(viewType));
        } else if (viewType == TYPE_FOOTER_VIEW) {
            return new ViewHolder(mFooterViews.get(0));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position) || isRefreshHeader(position)) {
            return;
        }
        final int adjPosition = position - (getHeaderViewsCount() + 1);
        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mInnerAdapter.onBindViewHolder(holder, adjPosition);

                if (mInnerAdapter instanceof SwipeMenuAdapter) {
                    View itemView = holder.itemView;
                    if (itemView instanceof SwipeMenuLayout) {
                        SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) itemView;
                        int childCount = swipeMenuLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View childView = swipeMenuLayout.getChildAt(i);
                            if (childView instanceof SwipeMenuView) {
                                ((SwipeMenuView) childView).bindAdapterPosition(adjPosition);
                            }
                        }
                    }

                }

                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener()  {
                        @Override
                        public void onClick(View v)
                        {
                            mOnItemClickListener.onItemClick(holder.itemView, adjPosition);
                        }
                    });

                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v)
                        {
                            mOnItemClickListener.onItemLongClick(holder.itemView, adjPosition);
                            return true;
                        }
                    });
                }

                return;
            }
     }
    }

    @Override
    public int getItemCount() {
        if (mInnerAdapter != null) {
            return getHeaderViewsCount() + getFooterViewsCount() + mInnerAdapter.getItemCount() + 1;
        } else {
            return getHeaderViewsCount() + getFooterViewsCount() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int adjPosition = position - (getHeaderViewsCount() + 1);
        if (isRefreshHeader(position)) {
            return TYPE_REFRESH_HEADER;
        }
        if (isHeader(position)) {
            position = position - 1;
            return mHeaderTypes.get(position);
        }
        if (isFooter(position)) {
            return TYPE_FOOTER_VIEW;
        }
        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemViewType(adjPosition);
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        if (mInnerAdapter != null && position >= getHeaderViewsCount()) {
            int adjPosition = position - getHeaderViewsCount();
            int adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if(isHeader(holder.getLayoutPosition()) ||isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition())) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewRecycled(holder);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     *
     * @param isCallback whether position is from callback interface
     * @param position
     * @return
     */
    public int getAdapterPosition(boolean isCallback, int position) {
        if(isCallback) {
            int adjPosition = position - getHeaderViewsCount() - 1;
            int adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adjPosition;
            }
        }else {
            return  (position + getHeaderViewsCount() + 1);
        }

        return -1;
    }

}
