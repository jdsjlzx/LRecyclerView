# LRecyclerView 

LRecyclerView是支持addHeaderView、 addFooterView、下拉刷新、分页加载数据的RecyclerView。


**它对 RecyclerView 控件进行了拓展，给RecyclerView增加HeaderView、FooterView，并且不需要对你的Adapter做任何修改。**


##效果图
![这里写图片描述](https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art1.png)

##Gradle
--

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
compile 'com.github.jdsjlzx:LRecyclerView:1.2.9'
```

LRecyclerView requires at minimum Java 7 or Android 4.0.

##JavaDoc
https://jitpack.io/com/github/jdsjlzx/LRecyclerView/1.2.9/javadoc/

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



##Demo下载
[点我下载](https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/app/app-release.apk)

##功能介绍


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

### 添加HeaderView、FooterView

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
 - onScrollDown()——RecyclerView正在滚动的监听事件；
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

###下拉刷新

```java
mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                
            }
        });
```
###加载更多
```java
mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                
            }
        });
```
###设置下拉刷新样式

```java
mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader); //设置下拉刷新Progress的样式
mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头
```

AVLoadingIndicatorView库有多少效果，LRecyclerView就支持多少下拉刷新效果，当然你也可以自定义下拉刷新效果。

效果图：

![这里写图片描述](http://img.blog.csdn.net/20160701173404897)

###设置加载更多样式

拷贝LRecyclerview_library中的文件文件layout_recyclerview_list_footer_loading.xml到你的工程中，修改后即可。


###开启和禁止下拉刷新功能

```java
mRecyclerView.setPullRefreshEnabled(true);
```

or

```java
mRecyclerView.setPullRefreshEnabled(false);
```

默认是开启。


###强制刷新

根据大家的反馈，增加了一个强制刷新的方法，使用如下：

```java
mRecyclerView.forceToRefresh();
```

**无论是下拉刷新还是强制刷新，刷新完成后调用下面代码：**

```java
mRecyclerView.refreshComplete();
mLRecyclerViewAdapter.notifyDataSetChanged();
```

###下拉刷新清空数据
有的时候，需要下拉的时候情况数据并更新UI，可以这么做：

```java
@Override
public void onRefresh() {
    RecyclerViewStateUtils.setFooterViewState(mRecyclerView,LoadingFooter.State.Normal);
    mDataAdapter.clear();
    mCurrentCounter = 0;
    isRefresh = true;
    requestData();
}
```
如果不需要下拉的时候情况数据并更新UI，如下即可：

```java
@Override
public void onRefresh() {
    isRefresh = true;
    requestData();
}
```
###开启和禁止自动加载更多功能

```java
mRecyclerView.setLoadMoreEnabled(true);
```

or

```java
mRecyclerView.setLoadMoreEnabled(false);;
```

默认是开启。如果不需要自动加载更多功能（也就是不需要分页）手动设置为false即可。

### 加载网络异常处理
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

###设置空白View（setEmptyView）

```java
mRecyclerView.setEmptyView(view);
```

##关于添加分割线

尽量不要使用Decoration,可以在Item布局的底部添加一条线，如下所示：
```groovy
<View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#dddddd" />
```

##滑动删除

效果图：

<img src="https://camo.githubusercontent.com/d2257cad7e4b932cd57969e1ce65181b7b7b1f70/687474703a2f2f6e6f74652e796f7564616f2e636f6d2f7977732f7075626c69632f7265736f757263652f62626336396331653035356362333335643031633737326531646164623063312f786d6c6e6f74652f44463646324336353038433234364330393331383037464333313332353737452f3130323635" width=268 height=457 />

##分组

效果图：

![这里写图片描述](https://github.com/jdsjlzx/LRecyclerView/blob/master/art/art6.gif)

功能还在完善中....




##代码混淆

```java
#LRecyclerview_library
-dontwarn com.github.jdsjlzx.**
-keep class com.github.jdsjlzx.**{*;}
```

如果你想了解更多混淆配置，参考：http://blog.csdn.net/jdsjlzx/article/details/51861460

##注意事项

1.如果添加了footerview，不要再使用setLScrollListener方法，如有需要，自定义实现即可。如下面代码不要同时使用：

```java
mRecyclerView.setLScrollListener(LScrollListener); 
mLRecyclerViewAdapter.addFooterView(new SampleFooter(this));

```

2.不要SwipeRefreshLayout与LRecyclerView一起使用，会有冲突，如果你实在想用，请参考SwipeRefreshLayoutActivity类的实现。

##Thanks

1.[HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)

2.[SwipeDelMenuViewGroup](https://github.com/mcxtzhang/SwipeDelMenuViewGroup)



##打赏

觉得本框架对你有帮助，不妨打赏赞助我一下，让我有动力走的更远。

<img src="http://img.blog.csdn.net/20160812223409875" width=280 height=280 />
