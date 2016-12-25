package com.lzx.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lzx.demo.bean.Visitable;
import com.lzx.demo.holder.BetterViewHolder;
import com.lzx.demo.type.TypeFactory;

import java.util.List;


/**
 * Created by qiu on 11/18/16.
 */
public class MainAdapter extends RecyclerView.Adapter<BetterViewHolder> {

    private TypeFactory mTypeFactory;
    private List<Visitable> mVisitables;

    public MainAdapter(TypeFactory typeFactory, List<Visitable> visitables) {
        mTypeFactory = typeFactory;
        mVisitables = visitables;
    }

    @Override
    public int getItemViewType(int position) {
        return mVisitables.get(position).type(mTypeFactory);
    }

    @Override
    public BetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mTypeFactory.onCreateViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(BetterViewHolder holder, int position) {

        holder.bindItem(mVisitables.get(position));

    }

    @Override
    public int getItemCount() {
        return mVisitables.size();
    }
}
