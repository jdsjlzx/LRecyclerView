package com.lzx.demo.base;

import com.lzx.demo.adapter.ExpandableRecyclerAdapter;

import static com.lzx.demo.adapter.CommentExpandAdapter.TYPE_PERSON;
import static com.lzx.demo.adapter.ExpandableRecyclerAdapter.TYPE_HEADER;

/**
 * Created by Lzx on 2016/9/30.
 */

public class CommentItem extends ExpandableRecyclerAdapter.ListItem {

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
