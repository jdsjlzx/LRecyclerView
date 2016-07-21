# LRecyclerView

简介
--

LRecyclerView是支持addHeaderView、 addFooterView、下拉刷新、分页加载数据的RecyclerView。

**它对 RecyclerView 控件进行了拓展，给RecyclerView增加HeaderView、FooterView，并且不需要对你的Adapter做任何修改。**

主要功能
----

1. 下拉刷新、滑动到底部自动加载下页数据；
2. 可以方便添加Header和Footer；
3. 头部下拉样式可以自定义；
4. 具备item点击和长按事件。
5. 网络错误加载失败点击Footer重新请求数据；
6. 可以动态为FooterView赋予不同状态（加载中、加载失败、滑到最底等）。

<br>注意：EndlessLinearLayoutActivity.java类里面有标准完整的使用方法，请尽量在这个界面看效果。</b>

感谢
--

> 如果我比别人看得远些，那是因为我站在巨人们的肩上。  （牛顿）

本开源控件是基于 [HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView) 开源项目而来，在原基础上进行了扩充。在此感谢cundong作者（github地址：https://github.com/cundong）。

效果图
---

![这里写图片描述](https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art1.png)

使用
--

添加HeaderView、FooterView
-----------------------

```
		mDataAdapter = new DataAdapter(this);
        mDataAdapter.setData(dataList);

        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(this, mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //add a HeaderView
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

        //add a FooterView
        RecyclerViewUtils.setFooterView(mRecyclerView, new SampleFooter(this));
```

添加滚动监听事件
--------

```
mRecyclerView.addOnScrollListener(mOnScrollListener);

RecyclerOnScrollListener mOnScrollListener = new RecyclerOnScrollListener() {

        @Override
        public void onBottom() {

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading) {
                Log.d(TAG, "the state is Loading, just wait..");
                return;
            }

            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(EndlessLinearLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(EndlessLinearLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };
```

RecyclerOnScrollListener实现了onScrollUp()、onScrollDown()、onBottom()、onScrolled四个事件，如下所示：

```
public abstract void onScrollUp();//scroll down to up

public abstract void onScrollDown();//scroll from up to down

public abstract void onBottom();//load next page

public abstract void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance
```

 - onScrollUp()——RecyclerView向上滑动的监听事件；
 - onScrollDown()——RecyclerView向下滑动的监听事件；
 - onBottom()——RecyclerView滑动到底部的监听事件；
 - onScrollDown()——RecyclerView正在滚动的监听事件；

加载更多（加载下页数据）
------------

从上面的RecyclerOnScrollListener类的介绍中就可以看出，实现加载更多只要在onBottom()接口中处理即可。

![这里写图片描述](http://static.oschina.net/uploads/img/201511/09175034_0mZ0.png)

下拉刷新
----

为了达到和Listview的下拉刷新效果，本项目没有借助SwipeRefreshLayout控件，而是在自定义RecyclerView头部实现的刷新效果。

这里的下拉刷新效果借鉴了开源库：[AVLoadingIndicatorView](https://github.com/81813780/AVLoadingIndicatorView)

设置加载样式：

```
mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
```

AVLoadingIndicatorView库有多少效果，LRecyclerView就支持多少下拉刷新效果，当然你也可以自定义下拉效果。

效果图：

![这里写图片描述](http://img.blog.csdn.net/20160701173404897)


下拉刷新逻辑处理：

```
mRecyclerView.setLoadingListener(new CustRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                requestData();
            }

        });
```
这里自定义了一个接口LoadingListener，如下所示：
```
public interface LoadingListener {

        void onRefresh();

    }
```

下拉刷新只要在onRefresh()方法中处理即可。

开启和禁止下拉刷新功能
--------
```
mRecyclerView.setPullRefreshEnabled(true);
```

or
```
mRecyclerView.setPullRefreshEnabled(false);
```

默认是开启。


加载网络异常处理
--------
加载数据时如果网络异常或者断网，LRecyclerView为你提供了重新加载的机制。

效果图：

![这里写图片描述](https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art5.png)

网络异常出错代码处理如下：

```
RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, getPageSize(), LoadingFooter.State.NetWorkError, mFooterClick);

private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, getPageSize(), LoadingFooter.State.Loading, null);
            requestData();
        }
    };
```

上面的mFooterClick就是我们点击底部的Footer时的逻辑处理事件，很显然我们还是在这里做重新请求数据操作。

点击事件和长按事件处理
---------

经过实践总结，在adapter中实现点击事件会好点。

先看下怎么使用：

```
mHeaderAndFooterRecyclerViewAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                Toast.makeText(EndlessLinearLayoutActivity.this, item.title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                Toast.makeText(EndlessLinearLayoutActivity.this, "onItemLongClick - " + item.title, Toast.LENGTH_SHORT).show();
            }
        });
```

原理就是实现viewHolder.itemView的点击和长按事件。由于代码过多就不贴出来了。

viewHolder源码如下：

```
public static abstract class ViewHolder {
        public final View itemView;
        int mPosition = NO_POSITION;
        int mOldPosition = NO_POSITION;
        long mItemId = NO_ID;
        int mItemViewType = INVALID_TYPE;
        int mPreLayoutPosition = NO_POSITION;
```

###设置空白View（setEmptyView）

```
mRecyclerView.setEmptyView(view);
```

分享
--
介绍完了LRecyclerView，似乎还少些什么，对了，那就是adapter了。

为了方便大家使用，分享个封装过的adapter。

```
public class ListBaseAdapter<T extends Entity> extends RecyclerView.Adapter {
    protected Context mContext;
    protected int mScreenWidth;

    public void setScreenWidth(int width) {
        mScreenWidth = width;
    }

    protected ArrayList<T> mDataList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(Collection<T> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }
}

```

ListBaseAdapter使用了泛型，简单方便，消除了强制类型转换。

使用如下：

```
private class DataAdapter extends ListBaseAdapter<ItemModel>{

        private LayoutInflater mLayoutInflater;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_text, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(item.title);
        }


        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);
            }
        }
    }
```

ListBaseAdapter虽然功能不强大，但是使用很方便。


LRecyclerView使用方便简单，无论你添加多少Header和Footer，你都不用担心position的问题，除了方便还是方便。

EndlessLinearLayoutActivity.java类里面有标准完整的使用方法，供参考。

代码混淆
--
```
#pull_recyclerview_library
-dontwarn com.cundong.recyclerview.**
-keep class com.cundong.recyclerview.**{*;}
```

如果你想了解更多混淆配置，参考：http://blog.csdn.net/jdsjlzx/article/details/51861460

注意事项
--
1. 如果添加了footerview，不要再使用addOnScrollListener方法，如有需要，自定义实现即可。如下面代码不要同时使用：
```
mRecyclerView.addOnScrollListener(mOnScrollListener); 
RecyclerViewUtils.setFooterView(mRecyclerView, new SampleFooter(this));
```
