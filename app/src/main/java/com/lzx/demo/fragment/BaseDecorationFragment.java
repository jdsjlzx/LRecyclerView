package com.lzx.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.ItemDecoration.DividerDecoration;
import com.lzx.demo.R;

public abstract class BaseDecorationFragment extends Fragment {

    private LRecyclerView mList;
    protected LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        mList = (LRecyclerView) view.findViewById(R.id.list);
        mList.setPullRefreshEnabled(false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.default_header_color)
                .build();

        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mList.addItemDecoration(divider);

        setAdapterAndDecor(mList);
    }

    protected abstract void setAdapterAndDecor(RecyclerView list);
}
