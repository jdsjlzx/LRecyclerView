package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzx.demo.R;
import com.lzx.demo.bean.Product;

import java.util.List;


public class HotsAdapter extends RecyclerView.Adapter<HotsAdapter.ProductViewHolder> {

    private List<Product> products;
    private Context context;

    public void setData(List<Product> products) {
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_item_hot, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.cover.setImageResource(product.coverResId);
        holder.title.setText(product.title);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int margin_horizontal = context.getResources().getDimensionPixelSize(R.dimen.padding_size_default);
        if(position % 2 == 0) {
            lp.setMargins(margin_horizontal, margin_horizontal/2, margin_horizontal/2, margin_horizontal/2);
        } else {
            lp.setMargins(margin_horizontal/2, margin_horizontal/2, margin_horizontal, margin_horizontal/2);
        }
        holder.itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title;

        ProductViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
