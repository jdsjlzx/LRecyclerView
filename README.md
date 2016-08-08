# LRecyclerView

简介
--

LRecyclerView是支持addHeaderView、 addFooterView、下拉刷新、分页加载数据的RecyclerView。

新增功能：SwipeMenu系列功能，包括Item侧滑菜单、长按拖拽Item，滑动删除Item等。

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


效果图
---

![这里写图片描述](https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art1.png)

Gradle
--
Step 1. 在你的根build.gradle文件中增加JitPack仓库依赖。

```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```	

Step 2. 在你的model的build.gradle文件中增加LRecyclerView依赖。
```
compile 'com.github.jdsjlzx:LRecyclerView:1.0.3'
```

LRecyclerView requires at minimum Java 7 or Android 4.0.

Demo演示
--

[点我下载](https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/app/app-release.apk)

使用
--

添加HeaderView、FooterView
-----------------------

```
mDataAdapter = new DataAdapter(this);
mDataAdapter.setData(dataList);

mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mDataAdapter);
mRecyclerView.setAdapter(mLRecyclerViewAdapter);

mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//add a HeaderView
RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

//add a FooterView
RecyclerViewUtils.setFooterView(mRecyclerView, new SampleFooter(this));
```

下拉刷新和加载更多
---------

为了大家使用方便，将需要用的方法统一封装到接口LScrollListener中。

```
mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                
            }

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onBottom() {
                
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

        });
        

        
```

LScrollListener实现了nRefresh()、onScrollUp()、onScrollDown()、onBottom()、onScrolled五个事件，如下所示：

```
void onRefresh();//pull down to refresh

void onScrollUp();//scroll down to up

void onScrollDown();//scroll from up to down

void onBottom();//load next page

void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance
```
 - onRefresh()——RecyclerView下拉刷新事件；
 - onScrollUp()——RecyclerView向上滑动的监听事件；
 - onScrollDown()——RecyclerView向下滑动的监听事件；
 - onBottom()——RecyclerView滑动到底部的监听事件；
 - onScrollDown()——RecyclerView正在滚动的监听事件；

加载更多（加载下页数据）
------------

从上面的LScrollListener介绍中就可以看出，实现加载更多只要在onBottom()接口中处理即可。

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

从上面的LScrollListener介绍中就可以看出，实现下拉刷新只要在onRefresh()接口中处理即可。

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


强制刷新
--------
根据大家的反馈，增加了一个强制刷新的方法，使用如下：

```
mRecyclerView.forceToRefresh();
```

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

原理就是在adapter中实现点击事件。

先看下怎么使用：

```
mLRecyclerViewAdapter.setOnItemClickLitener(new OnItemClickLitener() {
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

SwipeMenu
---------
本次新增SwipeMenu系列功能描述如下：

1. 左右两侧都有菜单；
2. 根据ViewType显示菜单；
3. 长按拖拽Item(List)，与菜单结合；
4. 长按拖拽Item(Grid)；
5. 滑动删除Item；
6. 指定某个Item不能拖拽或者不能滑动删除；
7. 用SwipeMenuLayout实现你自己的侧滑。

项目地址：https://github.com/jdsjlzx/LRecyclerView


###SwipeMenuAdapter

为了实现SwipeMenu的功能，此次新增了一个[SwipeMenuAdapter](https://github.com/jdsjlzx/LRecyclerView/blob/master/LRecyclerview_library/src/main/java/com/github/jdsjlzx/swipe/SwipeMenuAdapter.java)类。

SwipeMenuAdapter与library中已经存在的LRecyclerViewAdapter会不会冲突呢？答案是不会。SwipeMenuAdapter是用户级别的基类adapter，也就是用户需要继承SwipeMenuAdapter去实现自己的adapter，具体使用同以前。

SwipeMenuAdapter类的定义：
```
public abstract class SwipeMenuAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> 
```

实现自己的MenuAdapter：

```
public class MenuAdapter extends SwipeMenuAdapter<MenuAdapter.DefaultViewHolder> {

    protected List<ItemModel> mDataList = new ArrayList<>();

    public MenuAdapter() {
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text_swipe, parent, false);
    }

    @Override
    public MenuAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {

        String item = mDataList.get(position).title;

        DefaultViewHolder viewHolder = holder;
        viewHolder.tvTitle.setText(item);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }

    }

}
```

是不是很方便？MenuAdapter基本的功能都满足了，直接拷贝到项目中即可使用。

###左右两侧都有菜单

效果图：

<img src="https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/art/swipe_1.gif" width=320 height=560 />

具体使用步骤如下。

- 为SwipeRecyclerView的Item创建菜单
```
// 设置菜单创建器。
mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
//设置菜单Item点击监听事件        
mRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
```

swipeMenuCreator完成了左右菜单的创建，menuItemClickListener实现了菜单的点击事件。

需要注意的是，LRecyclerView提供了下面两个方法，具体使用请详见demo。

```
public void openLeftMenu(int position, int duration) {
        openMenu(position, LEFT_DIRECTION, duration);
    }

    public void openRightMenu(int position) {
        openMenu(position, RIGHT_DIRECTION, SwipeMenuLayout.DEFAULT_SCROLLER_DURATION);
    }
```
openLeftMenu：打开item的左边菜单
openRightMenu：打开item的右边菜单

这里关键的就是这个position（详细请参考demo）。


###根据ViewType显示菜单

效果图：

<img src="https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/art/swipe_2.gif" width=320 height=560 />

根据ViewType决定SwipeMenu在哪一行出现，可以左侧，可以右侧。

自定义MenuViewTypeAdapter，代码如下：
```
public class MenuViewTypeAdapter extends MenuAdapter {

    public static final int VIEW_TYPE_MENU = 1;
    public static final int VIEW_TYPE_NONE = 2;

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? VIEW_TYPE_MENU : VIEW_TYPE_NONE;
    }
}

```

在实现swipeMenuCreator 时，需要根据ItemViewType值来决定是否创建左右菜单。

```
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
        // 根据Adapter的ViewType来决定菜单的样式、颜色等属性、或者是否添加菜单。
            if (viewType == MenuViewTypeAdapter.VIEW_TYPE_NONE) {
            
                // Do nothing.
            } else if (viewType == MenuViewTypeAdapter.VIEW_TYPE_MENU) {
                int size = getResources().getDimensionPixelSize(R.dimen.item_height);

                ......
            }
        }
    };
```

###长按拖拽Item(List)，与菜单结合

效果图：

<img src="https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/art/swipe_3.gif" width=320 height=560 />

关键代码：
```
mRecyclerView.setLongPressDragEnabled(true);// 开启拖拽功能        mRecyclerView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。
```

onItemMoveListener具体如下：
```
    /**
     * 当Item移动的时候。
     */
    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            final int adjFromPosition = mLRecyclerViewAdapter.getAdapterPosition(true, fromPosition);
            final int adjToPosition = mLRecyclerViewAdapter.getAdapterPosition(true, toPosition);
            // 当Item被拖拽的时候。
            Collections.swap(mDataAdapter.getDataList(), adjFromPosition, adjToPosition);
            //Be carefull in here!
            mLRecyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;// 返回true表示处理了，返回false表示你没有处理。
        }

        @Override
        public void onItemDismiss(int position) {
            // 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
            // 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
        }
    };
```

注意下面代码：

```
final int adjFromPosition = mLRecyclerViewAdapter.getAdapterPosition(true, fromPosition);
final int adjToPosition = mLRecyclerViewAdapter.getAdapterPosition(true, toPosition);
```

>关于position的位置，为了大家使用方便，特在LRecyclerViewAdapter中提供了一个方法getAdapterPosition(boolean isCallback, int position)。
>
>- isCallback 含义：position是否接口回调中带来的
> - position 含义：如果不是接口回调，就是用户自己指定的position
> - getAdapterPosition(boolean isCallback, int position)只用于非LRecyclerViewAdapter提供的接口。

举例说明：

- setOnItemMoveListener不是 LRecyclerViewAdapter自带接口（也就是内部方法），需要调用getAdapterPosition方法获得正确的position
- 如setOnItemClickLitener 是 LRecyclerViewAdapter自带接口，接口里面自带了position，用户就不必调用getAdapterPosition方法，直接使用就可以了。
```
mLRecyclerViewAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String text = "Click position = " + position;
               
            }

            @Override
            public void onItemLongClick(View view, int position) {


            }
        });
```

###长按拖拽Item(Grid)

效果图：

<img src="https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/art/swipe_4.gif" width=320 height=560 />

与list功能一样，只是布局不一样。

###滑动直接删除Item

效果图：

<img src="https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/art/swipe_5.gif" width=320 height=560 />

注意：
> 滑动删除和滑动菜单是互相冲突的，两者只能出现一个。

关键代码：

```
mRecyclerView.setLongPressDragEnabled(true);
mRecyclerView.setItemViewSwipeEnabled(true);// 开启滑动删除        mRecyclerView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI
```

按照配置就可以实现滑动删除。

###指定某个Item不能拖拽或者不能滑动删除

效果图：

<img src="https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/art/swipe_6.gif" width=320 height=560 />

关键代码：

```
mRecyclerView.setLongPressDragEnabled(true);
mRecyclerView.setItemViewSwipeEnabled(true);// 开启滑动删除。        mRecyclerView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。        mRecyclerView.setOnItemMovementListener(onItemMovementListener);
```

###用SwipeMenuLayout实现你自己的侧滑

效果图：

<img src="https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/art/swipe_7.gif" width=320 height=560 />

这个与LRecyclerView关系不大，但是与SwipeMenu关系密切。为了实现滑动菜单的功能，定义了SwipeMenuLayout，详细使用见demo。



分组
--
效果图：

![这里写图片描述](https://github.com/jdsjlzx/LRecyclerView/blob/master/art/art6.gif)

功能还在完善中....




代码混淆
--
```
#LRecyclerview_library
-dontwarn com.github.jdsjlzx.**
-keep class com.github.jdsjlzx.**{*;}
```

如果你想了解更多混淆配置，参考：http://blog.csdn.net/jdsjlzx/article/details/51861460

注意事项
--
1. 如果添加了footerview，不要再使用setLScrollListener方法，如有需要，自定义实现即可。如下面代码不要同时使用：
```
mRecyclerView.setLScrollListener(LScrollListener); 
RecyclerViewUtils.setFooterView(mRecyclerView, new SampleFooter(this));
```


Thanks
--

1.[HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)

2.[SwipeRecyclerView](https://github.com/yanzhenjie/SwipeRecyclerView)
