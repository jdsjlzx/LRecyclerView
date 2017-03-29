# LRecyclerView 

LRecyclerView是支持addHeaderView、 addFooterView、下拉刷新、分页加载数据的RecyclerView。


**它对 RecyclerView 控件进行了拓展，给RecyclerView增加HeaderView、FooterView，并且不需要对你的Adapter做任何修改。**

推荐
----------
[RxJava经典视频教程已经上线，戳我就可以看啦......](http://www.stay4it.com/course/27)

## 效果图

![这里写图片描述](https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art1.png)


## Gradle


Step 1. 在你的根build.gradle文件中增加JitPack仓库依赖。

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```	

Step 2. 在你的model的build.gradle文件中增加LRecyclerView依赖。

```groovy
compile 'com.github.jdsjlzx:LRecyclerView:1.4.1'
```

LRecyclerView requires at minimum Java 7 or Android 4.0.

## JavaDoc

https://jitpack.io/com/github/jdsjlzx/LRecyclerView/1.4.1/javadoc/

##项目简述
1. 下拉刷新、滑动到底部自动加载下页数据； 
2. 可以方便添加Header和Footer；
3. 头部下拉样式可以自定义；
4. 具备item点击和长按事件；
5. 网络错误加载失败点击Footer重新请求数据；
6. 可以动态为FooterView赋予不同状态（加载中、加载失败、滑到最底等）；
7. 可以根据不同的viewtype自定义item视图；
8. 具备类似IOS侧滑删除菜单功能；
9. 完善的局部刷新效果；

### 注意：
1. EndlessLinearLayoutActivity.java类里面有标准完整的使用方法，请尽量在这个界面看效果；
2. 本着解耦的原则，能在demo中实现的就尽量不在libray中实现。
3. libray中的sdk版本都是最新版本，如果你不想处理申请权限的问题，可以在你本地的app的build.gradle中如下设置：
```groovy
compileSdkVersion 25
buildToolsVersion '25.0.2'
    
defaultConfig {
        applicationId "com.github.jdsjlzx"
        minSdkVersion 14
        targetSdkVersion 22
        versionCode 4
        versionName "0.5.3"
}
```
targetSdkVersion设置为22即可。


## Demo下载
[点我下载](https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/app/app-release.apk)

## 功能介绍


### 填充数据

```java
mDataAdapter = new DataAdapter(this);
mDataAdapter.setData(dataList);

mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
mRecyclerView.setAdapter(mLRecyclerViewAdapter);
```

> 
1. DataAdapter是用户自己真正的adapter，用户自己定义；
2. LRecyclerViewAdapter提供了一些实用的功能，使用者不用关心它的实现，只需构造的时候把自己的mDataAdapter以参数形式传进去即可。

###添加HeaderView、FooterView

```java
//add a HeaderView
mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

//add a FooterView
mLRecyclerViewAdapter.addFooterView(new SampleFooter(this));
```
添加HeaderView还可以使用下面两种方式：

```java
View header = LayoutInflater.from(this).inflate(R.layout.sample_header,(ViewGroup)findViewById(android.R.id.content), false);
mLRecyclerViewAdapter.addHeaderView(header);


CommonHeader headerView = new CommonHeader(getActivity(), R.layout.layout_home_header);
mLRecyclerViewAdapter.addHeaderView(headerView);
```

上面的方式同样适用于FooterView。

### 移除HeaderView、FooterView

```java
//remove a HeaderView
mLRecyclerViewAdapter.removeHeaderView();

//remove a FooterView
mLRecyclerViewAdapter.removeFooterView();
```
注意：
> 
1.如果有两个以上的HeaderView，连续调用mLRecyclerViewAdapter.removeHeaderView()即可。


### LScrollListener-滑动监听事件接口

LScrollListener实现了onScrollUp()、onScrollDown()、onScrolled、onScrollStateChanged四个事件，如下所示：


```java
void onScrollUp();//scroll down to up

void onScrollDown();//scroll from up to down

void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance

void onScrollStateChanged(int state)；

```

 - onScrollUp()——RecyclerView向上滑动的监听事件；
 - onScrollDown()——RecyclerView向下滑动的监听事件；
 - onScrolled()——RecyclerView正在滚动的监听事件；
 - onScrollStateChanged(int state)——RecyclerView正在滚动的监听事件；
 
使用：

```java
mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }
            @Override
            public void onScrollStateChanged(int state) {

            }

        });
 
```

### 下拉刷新

```java
mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                
            }
        });
```
### 加载更多
```java
mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                
            }
        });
```
### 设置下拉刷新样式

```java
mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader); //设置下拉刷新Progress的样式
mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头
```

AVLoadingIndicatorView库有多少效果，LRecyclerView就支持多少下拉刷新效果，当然你也可以自定义下拉刷新效果。

效果图：

![这里写图片描述](http://img.blog.csdn.net/20160701173404897)


### 自定义下拉刷新View

1. 自定义view实现IRefreshHeader接口；
2. 调用LRecyclerView提供的setRefreshHeader(IRefreshHeader refreshHeader)即可。

```java
/**
 * 设置自定义的RefreshHeader
 */
public void setRefreshHeader(IRefreshHeader refreshHeader) {
    this.mRefreshHeader = refreshHeader;
}
```

### 设置下拉刷新Header和Footer文字内容和颜色

```java
//设置头部加载颜色
mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
//设置底部加载颜色
mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
//设置底部加载文字提示
mRecyclerView.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");
```

记得设置ProgressStyle：
```java
mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
```

### 开启和禁止下拉刷新功能

```java
mRecyclerView.setPullRefreshEnabled(true);
```

or

```java
mRecyclerView.setPullRefreshEnabled(false);
```

默认是开启。


### 强制刷新

根据大家的反馈，增加了一个强制刷新的方法，使用如下：

```java
mRecyclerView.forceToRefresh();
```

**无论是下拉刷新还是强制刷新，刷新完成后调用下面代码：**

```java
mRecyclerView.refreshComplete();
mLRecyclerViewAdapter.notifyDataSetChanged();
```

### 下拉刷新清空数据
有的时候，需要下拉的时候清空数据并更新UI，可以这么做：

```java
@Override
public void onRefresh() {
    mDataAdapter.clear();
    mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
    mCurrentCounter = 0;
    requestData();
}
```
如果不需要下拉的时候清空数据并更新UI，如下即可：

```java
@Override
public void onRefresh() {
    requestData();
}
```
### 开启和禁止自动加载更多功能

```java
mRecyclerView.setLoadMoreEnabled(true);
```

or

```java
mRecyclerView.setLoadMoreEnabled(false);;
```

默认是开启。如果不需要自动加载更多功能（也就是不需要分页）手动设置为false即可。

### 加载数据完成处理

```java
mDataAdapter.addAll(list);
mRecyclerView.refreshComplete(REQUEST_COUNT);// REQUEST_COUNT为每页加载数量
```
如果没有更多数据（也就是全部加载完成），判断逻辑如下：

```java
mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mCurrentPage < totalPage) {
                    // loading data
                    requestData();
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });
```

### 加载数据网络异常处理

加载数据时如果网络异常或者断网，LRecyclerView为你提供了重新加载的机制。

效果图：

![这里写图片描述](https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art5.png)

网络异常出错代码处理如下：

```
mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                @Override
                public void reload() {
                    requestData();
                }
            });
```

上面的mFooterClick就是我们点击底部的Footer时的逻辑处理事件，很显然我们还是在这里做重新请求数据操作。


### 点击事件和长按事件处理

先看下怎么使用：

```java 
mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
            }

        });

mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                
            }
        });
```

原理就是实现viewHolder.itemView的点击和长按事件。由于代码过多就不贴出来了。

viewHolder源码如下：

```java
public static abstract class ViewHolder {
        public final View itemView;
        int mPosition = NO_POSITION;
        int mOldPosition = NO_POSITION;
        long mItemId = NO_ID;
        int mItemViewType = INVALID_TYPE;
        int mPreLayoutPosition = NO_POSITION;
```

### 设置空白View（setEmptyView）

```java
mRecyclerView.setEmptyView(view);
```
需要注意的是布局文件，如下所示：

```groovy
<android.support.design.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".MainActivity">

    <android.support.design.widget.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme.AppBarOverlay">

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="?attr/colorPrimary"
            app:popupTheme="@style/AppTheme.PopupOverlay"/>

    </android.support.design.widget.AppBarLayout>

    <com.github.jdsjlzx.recyclerview.LRecyclerView
        android:id="@+id/list"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior"/>

    <include
        android:id="@+id/empty_view"
        layout="@layout/layout_empty"
        android:visibility="gone"/>
</android.support.design.widget.CoordinatorLayout>
```

## 关于添加分割线

经过不断优化，LRecyclerView支持了ItemDecoration，使用如下所示：

LinearLayoutManager布局设置如下：

```java
DividerDecoration divider = new DividerDecoration.Builder(this,mLRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();
mRecyclerView.addItemDecoration(divider);
```

GridLayoutManager布局设置如下：

```java
int spacing = getResources().getDimensionPixelSize(R.dimen.dp_4);
mRecyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.GRAY));

//根据需要选择使用GridItemDecoration还是SpacesItemDecoration
GridItemDecoration divider = new GridItemDecoration.Builder(this)
        .setHorizontal(R.dimen.default_divider_padding)
        .setVertical(R.dimen.default_divider_padding)
        .setColorResource(R.color.split)
        .build();
//mRecyclerView.addItemDecoration(divider);
```
根据需要选择使用GridItemDecoration还是SpacesItemDecoration，SpacesItemDecoration（支持多类型布局）

## 滑动删除

效果图：

<img src="https://camo.githubusercontent.com/d2257cad7e4b932cd57969e1ce65181b7b7b1f70/687474703a2f2f6e6f74652e796f7564616f2e636f6d2f7977732f7075626c69632f7265736f757263652f62626336396331653035356362333335643031633737326531646164623063312f786d6c6e6f74652f44463646324336353038433234364330393331383037464333313332353737452f3130323635" width=268 height=457 />

## 分组

效果图：

![这里写图片描述](https://github.com/jdsjlzx/LRecyclerView/blob/master/art/art6.gif)

功能还在完善中....




## 代码混淆

```java
#LRecyclerview_library
-dontwarn com.github.jdsjlzx.**
-keep class com.github.jdsjlzx.**{*;}
```

如果你想了解更多混淆配置，参考：http://blog.csdn.net/jdsjlzx/article/details/51861460

## 注意事项

1.如果添加了footerview，不要再使用setLScrollListener方法，如有需要，自定义实现即可。如下面代码不要同时使用：

```java
mRecyclerView.setLScrollListener(LScrollListener); 
mLRecyclerViewAdapter.addFooterView(new SampleFooter(this));

```

2.不要SwipeRefreshLayout与LRecyclerView一起使用，会有冲突，为了更好的满足广大用户，新增了LuRecyclerView类，可以与SwipeRefreshLayout搭配使用，详细请参考SwipeRefreshLayoutActivity类的实现。

3.关于RecyclerView自动滑动的问题

这个自动滑动归根结底是焦点问题，子item有焦点，导致RecyclerView自动滑动到了子item，在根布局上加了android:descendantFocusability="blocksDescendants"，根view来处理焦点，不传给子view就能解决问题。

## LRecyclerView的应用

效果图：

<img src="https://github.com/jdsjlzx/Community/blob/master/pic/home.png" width=268 height=457 />

代码详见：https://github.com/jdsjlzx/Community

## Thanks

1.[HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)

2.[SwipeDelMenuViewGroup](https://github.com/mcxtzhang/SwipeDelMenuViewGroup)



## 打赏

觉得本框架对你有帮助，不妨打赏赞助我一下，让我有动力走的更远。

<img src="http://img.blog.csdn.net/20160812223409875" width=280 height=280 />
