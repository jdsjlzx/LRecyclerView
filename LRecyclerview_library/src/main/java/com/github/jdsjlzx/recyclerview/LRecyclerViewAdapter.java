package com.github.jdsjlzx.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.IRefreshHeader;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;

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

    private IRefreshHeader mRefreshHeader;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * RecyclerView使用的，真正的Adapter
     */
    private RecyclerView.Adapter mInnerAdapter;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    private SpanSizeLookup mSpanSizeLookup;


    public LRecyclerViewAdapter(RecyclerView.Adapter innerAdapter) {
        this.mInnerAdapter = innerAdapter;
    }

    public void setRefreshHeader(IRefreshHeader refreshHeader){
        mRefreshHeader = refreshHeader;
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

    public void addFooterView(View view) {

        if (view == null) {
            throw new RuntimeException("footer is null");
        }

        removeFooterView();
        mFooterViews.add(view);
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

    public void removeHeaderView() {
        if (getHeaderViewsCount() > 0) {
            View headerView = getHeaderView();
            mHeaderViews.remove(headerView);
            this.notifyDataSetChanged();
        }

    }

    public void removeFooterView() {
        if (getFooterViewsCount() > 0) {
            View footerView = getFooterView();
            mFooterViews.remove(footerView);
            this.notifyDataSetChanged();
        }

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
            return new ViewHolder(mRefreshHeader.getHeaderView());
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

                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener()  {
                        @Override
                        public void onClick(View v)
                        {
                            mOnItemClickListener.onItemClick(holder.itemView, adjPosition);
                        }
                    });

                }

                if (mOnItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v)
                        {
                            mOnItemLongClickListener.onItemLongClick(holder.itemView, adjPosition);
                            return true;
                        }
                    });
                }

            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder,position);
        } else {

            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            final int adjPosition = position - (getHeaderViewsCount() + 1);
            int adapterCount;
            if (mInnerAdapter != null) {
                adapterCount = mInnerAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    mInnerAdapter.onBindViewHolder(holder, adjPosition, payloads);
                }
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
            //判断是否setHasStableIds(true);
            if(hasStableIds()) {
                adjPosition--;
            }
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
                    if (mSpanSizeLookup == null) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    } else {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : mSpanSizeLookup.getSpanSize(gridManager,  (position - (getHeaderViewsCount() + 1)));
                    }

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

    /*@Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewRecycled(holder);
    }*/


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     *
     * @param isCallback whether position is from callback interface
     * @param position
     * @return
     */
    public int getAdapterPosition(boolean isCallback, int position) {
        if(isCallback) {
            int adjPosition = position - (getHeaderViewsCount() + 1);
            int adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adjPosition;
            }
        }else {
            return  (position + getHeaderViewsCount()) + 1;
        }

        return -1;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mOnItemLongClickListener = itemLongClickListener;
    }

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    /**
     * @param spanSizeLookup
     * only used to GridLayoutManager
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

}
