package com.lzx.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.lzx.demo.R;
import com.lzx.demo.base.CommentItem;

import java.util.ArrayList;

public class CommentExpandAdapter extends ExpandableRecyclerAdapter<CommentItem> {
    public static final int TYPE_PERSON = 1001;

    private LRecyclerView recyclerView;
    public CommentExpandAdapter(Context context, LRecyclerView recyclerView) {
        super(context);
        this.recyclerView = recyclerView;
        //setItems(getSampleItems());
    }

    /*public static class CommentItem extends ExpandableRecyclerAdapter.ListItem {
        public String Text;

        public CommentItem(String group) {
            super(TYPE_HEADER);
            Text = group;
        }

        public CommentItem(String first, String last) {
            super(TYPE_PERSON);
            Text = first + " " + last;
        }
    }*/

    public class CommentViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView tvName, tvComment, tvTime, tvReply;
        ImageView imgAvatar;


        public CommentViewHolder(View view, LRecyclerView recyclerView) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow),recyclerView);

            tvName = (TextView) view.findViewById(R.id.tvname);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvReply = (TextView) view.findViewById(R.id.tvReply);
            imgAvatar = (ImageView) view.findViewById(R.id.avatacomment);

        }

        public void bind(int position) {
            super.bind(position);

            tvName.setText(visibleItems.get(position).Text);
        }
    }

    public class CommentChildViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView tvName, tvComment, tvTime;
        ImageView imgAvatar;
        public CommentChildViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvname);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            imgAvatar = (ImageView) view.findViewById(R.id.avatacomment);
        }

        public void bind(int position) {
            tvComment.setText(visibleItems.get(position).Text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                //header中的箭头默认隐藏，如有需要，item_arrow设置为visible即可
                return new CommentViewHolder(inflate(R.layout.item_comment, parent), recyclerView);
            case TYPE_PERSON:
            default:
                return new CommentChildViewHolder(inflate(R.layout.item_child_comment, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((CommentViewHolder) holder).bind(position);
                break;
            case TYPE_PERSON:
            default:
                ((CommentChildViewHolder) holder).bind(position);
                break;
        }


    }

    public ArrayList<CommentItem> getSampleItems() {
        ArrayList<CommentItem> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            items.add(new CommentItem("Friends"));
            items.add(new CommentItem("有心课堂的创始人...", "Stay"));
            items.add(new CommentItem("听说他自定义view本事强", "谷歌的小弟"));
            items.add(new CommentItem("听说他优化性能本事强", "Star"));
            items.add(new CommentItem("踏实、谦虚、勤奋、上进...", "will"));
        }

        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Smith"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Doe"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Hall"));
        items.add(new CommentItem("Associates"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Jones"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Smith"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Hall"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Lake"));
        items.add(new CommentItem("Colleagues"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Jones"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Smith"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Hall"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Lake"));

        return items;
    }
}
