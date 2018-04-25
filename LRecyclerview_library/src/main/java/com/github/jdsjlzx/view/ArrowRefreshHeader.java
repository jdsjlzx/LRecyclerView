package com.github.jdsjlzx.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.jdsjlzx.R;
import com.github.jdsjlzx.interfaces.IRefreshHeader;
import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.WeakHandler;

import java.util.Date;


public class ArrowRefreshHeader extends LinearLayout implements IRefreshHeader {

    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private SimpleViewSwitcher mProgressBar;
    private TextView mStatusTextView;
    private TextView mHeaderTimeView;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private static final int ROTATE_ANIM_DURATION = 180;

    public int mMeasuredHeight;
    private int hintColor;
    private int mState = STATE_NORMAL;

    private WeakHandler mHandler = new WeakHandler();

    public ArrowRefreshHeader(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public ArrowRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_recyclerview_refresh_header, null);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ImageView)findViewById(R.id.listview_header_arrow);
        mStatusTextView = (TextView)findViewById(R.id.refresh_status_textview);

        //init the progress view
        mProgressBar = (SimpleViewSwitcher)findViewById(R.id.listview_header_progressbar);
        mProgressBar.setView(initIndicatorView(ProgressStyle.BallSpinFadeLoader));

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        mHeaderTimeView = (TextView)findViewById(R.id.last_refresh_time);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
        hintColor = android.R.color.darker_gray;
    }

    public void setProgressStyle(int style) {
        if (style == ProgressStyle.SysProgress) {
            ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
            mProgressBar.setView(progressBar);
        } else {
            mProgressBar.setView(initIndicatorView(style));
        }
    }

    private View initIndicatorView(int style) {
        AVLoadingIndicatorView progressView = (AVLoadingIndicatorView) LayoutInflater.from(getContext()).inflate(R.layout.layout_indicator_view, null);
        progressView.setIndicatorId(style);
        progressView.setIndicatorColor(Color.GRAY);
        return progressView;
    }

    public void setIndicatorColor(int color) {
        if(mProgressBar.getChildAt(0) instanceof AVLoadingIndicatorView){
            AVLoadingIndicatorView progressView = (AVLoadingIndicatorView) mProgressBar.getChildAt(0);
            progressView.setIndicatorColor(color);
        }
    }

    public void setHintTextColor(int color) {
        this.hintColor = color;
    }

    public void setViewBackgroundColor(int color) {
        this.setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    public void setArrowImageView(int resid){
        mArrowImageView.setImageResource(resid);
    }

    public void setState(int state) {
        if (state == mState) return ;

        if (state == STATE_REFRESHING) {	// 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            smoothScrollTo(mMeasuredHeight);
        } else if(state == STATE_DONE) {
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {	// 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        mStatusTextView.setTextColor(ContextCompat.getColor(getContext(), hintColor));

        switch(state){
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mStatusTextView.setText(R.string.listview_header_hint_normal);
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mStatusTextView.setText(R.string.listview_header_hint_release);
                }
                break;
            case STATE_REFRESHING:
                mStatusTextView.setText(R.string.refreshing);
                break;
            case STATE_DONE:
                mStatusTextView.setText(R.string.refresh_done);
                break;
            default:
        }

        mState = state;
    }

    public int getState() {
        return mState;
    }

    @Override
    public void refreshComplete(){
        mHeaderTimeView.setText(friendlyTime(new Date()));
        setState(STATE_DONE);
        mHandler.postDelayed(new Runnable(){
            public void run() {
                reset();
            }
        }, 200);
    }

    @Override
    public View getHeaderView() {
        return this;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    //垂直滑动时该方法不实现
    @Override
    public int getVisibleWidth() {
        return 0;
    }

    @Override public int getType() {
        return TYPE_HEADER_NORMAL;
    }

    @Override
    public void onReset() {
        setState(STATE_NORMAL);
    }

    @Override
    public void onPrepare() {
        setState(STATE_RELEASE_TO_REFRESH);
    }

    @Override
    public void onRefreshing() {
        setState(STATE_REFRESHING);
    }

    @Override
    public void onMove(float offSet, float sumOffSet) {
        int top = getTop();
        if (offSet > 0 && top == 0 ){
            setVisibleHeight((int) offSet + getVisibleHeight());
        }else if (offSet < 0 && getVisibleHeight() > 0){
            layout(getLeft(), 0, getRight(), getHeight()); //重新布局让header显示在顶端
            setVisibleHeight((int) offSet + getVisibleHeight());
        }
        if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
            if (getVisibleHeight() > mMeasuredHeight) {
                onPrepare();
            } else {
                onReset();
            }
        }
    }

    @Override
    public boolean onRelease() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) {// not visible.
            isOnRefresh = false;
        }

        if(getVisibleHeight() > mMeasuredHeight &&  mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height > mMeasuredHeight) {
            smoothScrollTo(mMeasuredHeight);
        }
        if (mState != STATE_REFRESHING) {
            smoothScrollTo(0);
        }

        if (mState == STATE_REFRESHING) {
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
        }

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        mHandler.postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public String friendlyTime(Date time) {
        int ct = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);

        if (ct == 0) {
            return getContext().getResources().getString(R.string.text_just);
        }

        if (ct > 0 && ct < 60) {
            return ct + getContext().getResources().getString(R.string.text_seconds_ago);
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + getContext().getResources().getString(R.string.text_minute_ago);
        }
        if (ct >= 3600 && ct < 86400)
            return ct / 3600 + getContext().getResources().getString(R.string.text_hour_ago);
        if (ct >= 86400 && ct < 2592000) { //86400 * 30
            int day = ct / 86400;
            return day + getContext().getResources().getString(R.string.text_day_ago);
        }
        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + getContext().getResources().getString(R.string.text_month_ago);
        }
        return ct / 31104000 + getContext().getResources().getString(R.string.text_year_ago);
    }

}