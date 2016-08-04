package com.github.jdsjlzx.swipe;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnSwipeMenuItemClickListener;
import com.github.jdsjlzx.interfaces.SwipeSwitch;

import java.util.List;

public class SwipeMenuView extends LinearLayout {

    private SwipeSwitch mSwipeSwitch;

    private int mAdapterPosition;

    private int mDirection;

    private OnSwipeMenuItemClickListener mItemClickListener;

    public SwipeMenuView(Context context) {
        this(context, null);
    }

    public SwipeMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindMenu(SwipeMenu swipeMenu, int direction) {
        removeAllViews();
        this.mDirection = direction;
        List<SwipeMenuItem> items = swipeMenu.getMenuItems();
        int index = 0;
        for (SwipeMenuItem item : items) {
            addItem(item, index++);
        }
    }

    public void bindMenuItemClickListener(OnSwipeMenuItemClickListener swipeMenuItemClickListener, SwipeSwitch swipeSwitch) {
        this.mItemClickListener = swipeMenuItemClickListener;
        this.mSwipeSwitch = swipeSwitch;
    }

    public void bindAdapterPosition(int position) {
        this.mAdapterPosition = position;
    }

    private void addItem(SwipeMenuItem item, int index) {
        LayoutParams params = new LayoutParams(item.getWidth(), item.getHeight());
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(index);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(VERTICAL);
        parent.setLayoutParams(params);
        ResCompat.setBackground(parent, item.getBackground());
        parent.setOnClickListener(mMenuClickListener);
        addView(parent);

        if (item.getImage() != null)
            parent.addView(createIcon(item));

        if (!TextUtils.isEmpty(item.getText()))
            parent.addView(createTitle(item));
    }

    private ImageView createIcon(SwipeMenuItem item) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(item.getImage());
        return imageView;
    }

    private TextView createTitle(SwipeMenuItem item) {
        TextView textView = new TextView(getContext());
        textView.setText(item.getText());
        textView.setGravity(Gravity.CENTER);
        int textSize = item.getTextSize();
        if (textSize > 0)
            textView.setTextSize(textSize);
        int textColor = item.getTitleColor();
        textView.setTextColor(textColor);
        return textView;
    }

    private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null && mSwipeSwitch != null && mSwipeSwitch.isMenuOpen()) {
                mItemClickListener.onItemClick(mSwipeSwitch, mAdapterPosition, v.getId(), mDirection);
            }
        }
    };
}