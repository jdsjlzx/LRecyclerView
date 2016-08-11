# LRecyclerView 

LRecyclerView是支持addHeaderView、 addFooterView、下拉刷新、分页加载数据的RecyclerView。

新增功能：SwipeMenu系列功能，包括Item侧滑菜单、长按拖拽Item，滑动删除Item等。

**它对 RecyclerView 控件进行了拓展，给RecyclerView增加HeaderView、FooterView，并且不需要对你的Adapter做任何修改。**


##效果图
![这里写图片描述](https://raw.githubusercontent.com/cundong/HeaderAndFooterRecyclerView/master/art/art1.png)

##Gradle
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


##项目简述
1. 下拉刷新、滑动到底部自动加载下页数据； 
2. 可以方便添加Header和Footer；
3. 头部下拉样式可以自定义；
4. 具备item点击和长按事件。
5. 网络错误加载失败点击Footer重新请求数据；
6. 可以动态为FooterView赋予不同状态（加载中、加载失败、滑到最底等）。

<br>注意：EndlessLinearLayoutActivity.java类里面有标准完整的使用方法，请尽量在这个界面看效果。</b>



##Demo下载
[点我下载](https://raw.githubusercontent.com/jdsjlzx/LRecyclerView/master/app/app-release.apk)

##打赏

觉得本框架对你有帮助，不妨打赏赞助我一下，让我有动力走的更远。

![微信](http://img.blog.csdn.net/20160524104553306?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

