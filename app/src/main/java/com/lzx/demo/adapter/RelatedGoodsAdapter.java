package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzx.demo.R;
import com.lzx.demo.bean.Goods;

import java.util.List;

public class RelatedGoodsAdapter extends RecyclerView.Adapter<RelatedGoodsAdapter.RelatedGoodsViewHolder> {

    private List<Goods> mMovieItems;
    private Context mContext;
    public RelatedGoodsAdapter(Context context, List<Goods> movieItems) {
        mMovieItems = movieItems;
        mContext = context;
    }

    @Override
    public RelatedGoodsViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_goods_related_item, null, false);
        return new RelatedGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RelatedGoodsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMovieItems.size();
    }

    static class RelatedGoodsViewHolder extends RecyclerView.ViewHolder {


        protected ImageView thumbnail;

        public RelatedGoodsViewHolder(View view) {
            super(view);
        }

    }
}
