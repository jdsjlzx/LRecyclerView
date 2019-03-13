package com.lzx.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.lzx.demo.R;
import com.lzx.demo.base.BaseMultiAdapter;
import com.lzx.demo.base.SuperViewHolder;
import com.lzx.demo.bean.MultipleItem;
import com.lzx.demo.view.ClickLoadingFooter;

import java.util.List;

/**
 * Created by Lzx on 2016/12/30.
 */

public class CategoryAdapter extends BaseMultiAdapter<MultipleItem> {

    public CategoryAdapter(Context context) {
        super(context);
        addItemType(MultipleItem.TEXT, R.layout.list_item_category);
        addItemType(MultipleItem.LIST, R.layout.list_item_list);
        addItemType(MultipleItem.FOOT, R.layout.list_item_foot);
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MultipleItem item = getDataList().get(position);
        switch (item.getItemType()) {
            case MultipleItem.TEXT:
                bindTextItem(holder,item);
                break;
            case MultipleItem.LIST:
                bindListItem(holder,item);
                break;
            case MultipleItem.FOOT:
                bindFootItem(holder,item);
                break;
            default:
                break;
        }

    }

    private void bindTextItem(SuperViewHolder holder, MultipleItem item) {
        TextView textView = holder.getView(R.id.info_text);
        textView.setText(item.getTitle());
    }

    private void bindListItem(SuperViewHolder holder, MultipleItem item) {
        TextView textView = holder.getView(R.id.content);
        textView.setText(item.getTitle());
    }

    private void bindFootItem(SuperViewHolder holder, final MultipleItem item) {
        ClickLoadingFooter footer = holder.getView(R.id.footer);
        footer.setOnClickLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("lzx","CategoryAdapter onLoadMore getFootCategory() " + item.getFootCategory());
                loadMoreListener.loadMore(item.getFootCategory());
            }
        });
    }

    public void removeItem(int footCategory,int index) {
        if (footCategory == MultipleItem.FOOT_MY_GROUP) {
            getDataList().remove(index);
            size_first_list--;
        } else if (footCategory == MultipleItem.FOOT_PUBLIC_GROUP) {
            getDataList().remove(index);
            size_second_list--;
        }
    }


    public int getSize_first_list() {
        return size_first_list;
    }

    public int getSize_second_list() {
        return size_second_list;
    }

    public int getFootViewIndex(int footCategory) {
        Log.e("lzx","getFootViewIndex getDataList().size()  " + getDataList().size()
                + " size_first_list " + size_first_list + " size_second_list " + size_second_list);
        int index = 0;
        if (footCategory == MultipleItem.FOOT_MY_GROUP) {
            if (size_first_list > 0) {
                index =  this.size_first_list - 1;
            }
        } else if (footCategory == MultipleItem.FOOT_PUBLIC_GROUP) {
            if (getDataList().size() > 0) {
                index =  getDataList().size() - 1;
            }
        }
        return index;
    }

    public void addData(List<MultipleItem> list,int footCategory) {
        Log.e("lzx","addData getDataList().size()  " + getDataList().size()
                + " size_first_list " + size_first_list + " size_second_list " + size_second_list);
        if (footCategory == MultipleItem.FOOT_MY_GROUP) {

            //第一次添加
            if (size_first_list == 0) {
                //添加头部标题
                MultipleItem item = new MultipleItem(MultipleItem.TEXT);
                item.setTitle("我的群组");
                getDataList().add(item);

                //添加尾部加载更多view
                item = new MultipleItem(MultipleItem.FOOT);
                item.setFootCategory(MultipleItem.FOOT_MY_GROUP);
                getDataList().add(item);

                int insertIndex = this.mDataList.size() - 1;
                if (this.mDataList.addAll(insertIndex, list)) {
                    notifyItemRangeInserted(insertIndex, list.size());
                    size_first_list += list.size();
                }
            } else {
                int insertIndex = size_first_list + 1;
                if (this.mDataList.addAll(insertIndex, list)) {
                    notifyItemRangeInserted(insertIndex, list.size());
                    size_first_list += list.size();
                }
                if (size_first_list >= totalFirstList) {
                    int footIndex = size_first_list + 1;
                    Log.e("lzx","footIndex " + footIndex);
                    getDataList().remove(footIndex);
                }
            }

            /*if (size_first_list > 0 && size_first_list - 2 < totalFirstList) {
                int footIndex = size_first_list - 1;
                Log.e("lzx","footIndex " + footIndex);
                getDataList().remove(footIndex);
                size_first_list--;
            }
            Log.e("lzx","insert size_first_list " + size_first_list + " new list.size " + list.size());
            getDataList().addAll(this.size_first_list,list);
            this.size_first_list += list.size();
            //减去1是为了减去顶部的标题
            if (size_first_list - 1 < totalFirstList) {
                MultipleItem item = new MultipleItem(MultipleItem.FOOT);
                item.setFootCategory(MultipleItem.FOOT_MY_GROUP);
                getDataList().add(this.size_first_list,item);
            }*/

        } else if (footCategory == MultipleItem.FOOT_PUBLIC_GROUP) {
            //第一次添加
            if (size_second_list == 0) {
                //添加头部标题
                MultipleItem item = new MultipleItem(MultipleItem.TEXT);
                item.setTitle("公开群组");
                getDataList().add(item);

                //添加尾部加载更多view
                item = new MultipleItem(MultipleItem.FOOT);
                item.setFootCategory(MultipleItem.FOOT_PUBLIC_GROUP);
                getDataList().add(item);

                int insertIndex = this.mDataList.size() - 1;
                if (this.mDataList.addAll(insertIndex, list)) {
                    notifyItemRangeInserted(insertIndex, list.size());
                    size_second_list += list.size();
                }
            } else {
                int insertIndex = getDataList().size() - 1;
                if (this.mDataList.addAll(insertIndex, list)) {
                    notifyItemRangeInserted(insertIndex, list.size());
                    size_second_list += list.size();
                }
                if (size_second_list >= totalSecondList) {
                    int footIndex =  getDataList().size() - 1;
                    Log.e("lzx","footIndex " + footIndex);
                    getDataList().remove(footIndex);
                }
            }
        }

    }

    /**
     * 真实数据统计（不包括头部和尾部）
     */
    private int size_first_list = 0;
    private int size_second_list = 0;
    private int totalFirstList = 0;

    public void setTotalFirstList(int total) {
        this.totalFirstList = total;
    }

    public void setTotalSecondList(int total) {
        this.totalSecondList = total;
    }

    private int totalSecondList = 0;

    public interface LoadMoreListener {
        void loadMore(int footCategory);
    }

    private LoadMoreListener loadMoreListener;

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

}
