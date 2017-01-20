package com.lzx.demo.multitype;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.MainAdapter;
import com.lzx.demo.bean.Category;
import com.lzx.demo.bean.HotList;
import com.lzx.demo.bean.Product;
import com.lzx.demo.bean.ProductList;
import com.lzx.demo.bean.Visitable;
import com.lzx.demo.type.ListTypeFactory;
import com.lzx.demo.view.SampleHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * BetterAdapter的运用
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/1125/6806.html
 */
public class BilibiliActivity extends AppCompatActivity {

    private LRecyclerView mRecyclerView = null;

    private MainAdapter mMainAdapter = null;
    List<Visitable> mVisitables;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mVisitables = getData();
        mMainAdapter = new MainAdapter(new ListTypeFactory(), mVisitables);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMainAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mLRecyclerViewAdapter.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                Object item = mVisitables.get(position);
                return (item instanceof HotList || item instanceof ProductList || item instanceof Category) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                    }
                },1000);
            }
        });

        mRecyclerView.refresh();
        mRecyclerView.setLoadMoreEnabled(false);
        //是否允许嵌套滑动
        mRecyclerView.setNestedScrollingEnabled(false);

    }

    private List<Visitable> getData(){
        List<Visitable> visitables = new ArrayList<>();
        visitables.add(new Category("热销女装"));
        visitables.add(new ProductList(getProducts()));
        visitables.add(new HotList(getProducts()));

        visitables.add(new Category("推荐商品"));
        visitables.add(new ProductList(getProducts()));
        visitables.add(new HotList(getProducts()));

        visitables.add(new Category("抢购商品"));
        visitables.add(new ProductList(getProducts()));
        visitables.add(new HotList(getProducts()));

        return visitables;
    }

    private List<Product> getProducts(){
        List<Product> products = new ArrayList<>();
        products.add(new Product("三杆火枪干掉自定义View",R.mipmap.img_00));
        products.add(new Product("技能必备－快速review代码",R.mipmap.img_01));
        products.add(new Product("Retrofit分析-漂亮的解耦套路",R.mipmap.img_10));
        products.add(new Product("自己动手写多任务下载框架",R.mipmap.img_11));
        products.add(new Product("自己动手写HTTP框架",R.mipmap.img_00));
        products.add(new Product("自己动手写DB框架",R.mipmap.img_00));

        return products;
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}