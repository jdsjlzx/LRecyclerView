package com.lzx.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzx.demo.R;

import java.util.ArrayList;
import java.util.List;

public class CommentExpandAdapter extends ExpandableRecyclerAdapter<CommentExpandAdapter.CommentItem> {
    public static final int TYPE_PERSON = 1001;

    public CommentExpandAdapter(Context context) {
        super(context);

        setItems(getSampleItems());
    }

    public static class CommentItem extends ExpandableRecyclerAdapter.ListItem {
        public String Text;

        public CommentItem(String group) {
            super(TYPE_HEADER);
            Text = group;
        }

        public CommentItem(String first, String last) {
            super(TYPE_PERSON);
            Text = first + " " + last;
        }
    }

    public class CommentViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView tvName, tvComment, tvTime, tvReply;
        ImageView imgAvatar;


        public CommentViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));

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
                return new CommentViewHolder(inflate(R.layout.item_comment, parent));
            case TYPE_PERSON:
            default:
                return new CommentChildViewHolder(inflate(R.layout.item_child_comment, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
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

    public List<CommentItem> getSampleItems() {
        List<CommentItem> items = new ArrayList<>();

        items.add(new CommentItem("Friends"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Smith"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Doe"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "Hall"));
        items.add(new CommentItem("Có thể thoải mái mở rộng 1 class, nhưng không được sửa đổi bên trong class đó \n" +
                "(open for extension but closed for modification)", "West"));
        items.add(new CommentItem("Family"));
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
