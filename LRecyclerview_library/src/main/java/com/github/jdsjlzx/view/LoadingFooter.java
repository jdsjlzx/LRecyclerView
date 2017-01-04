package com.github.jdsjlzx.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.R;
import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;

public class LoadingFooter extends RelativeLayout {

    protected State mState = State.Normal;
    private View mLoadingView;
    private View mNetworkErrorView;
    private View mTheEndView;
    private AVLoadingIndicatorView mLoadingProgress;
    private TextView mLoadingText;
    private TextView mNoMoreText;
    private TextView mNoNetWorkText;
    private String loadingHint;
    private String noMoreHint;
    private String noNetWorkHint;
    private int indicatorColor ;
    private int hintColor = R.color.colorAccent;

    public LoadingFooter(Context context) {
        super(context);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.layout_recyclerview_list_footer, this);
        setOnClickListener(null);

        setState(State.Normal, true);

        indicatorColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
    }

    public void setLoadingHint(String hint) {
        this.loadingHint = hint;
    }

    public void setNoMoreHint(String hint) {
        this.noMoreHint = hint;
    }

    public void setNoNetWorkHint(String hint) {
        this.noNetWorkHint = hint;
    }

    public void setIndicatorColor(int color) {
        this.indicatorColor = color;
        if(null != mLoadingProgress){
            mLoadingProgress.setIndicatorColor(indicatorColor);
        }
    }

    public void setHintTextColor(int color) {
        this.hintColor = color;
    }

    public void setViewBackgroundColor(int color) {
        this.setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    public void setProgressStyle(int style) {
        if(null != mLoadingProgress){
            mLoadingProgress.setIndicatorId(style);
        }
    }

    public State getState() {
        return mState;
    }

    public void setState(State status ) {
        setState(status, true);
    }

    /**
     * 设置状态
     *
     * @param status
     * @param showView 是否展示当前View
     */
    public void setState(State status, boolean showView) {
        if (mState == status) {
            return;
        }
        mState = status;

        switch (status) {

            case Normal:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }

                break;
            case Loading:
                setOnClickListener(null);
                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }

                if (mLoadingView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.loading_viewstub);
                    mLoadingView = viewStub.inflate();

                    mLoadingProgress = (AVLoadingIndicatorView) mLoadingView.findViewById(R.id.loading_progress);
                    mLoadingText = (TextView) mLoadingView.findViewById(R.id.loading_text);
                } else {
                    mLoadingView.setVisibility(VISIBLE);
                }

                mLoadingView.setVisibility(showView ? VISIBLE : GONE);

                mLoadingProgress.setVisibility(View.VISIBLE);
                mLoadingProgress.setIndicatorColor(indicatorColor);
                mLoadingText.setText(TextUtils.isEmpty(loadingHint) ? getResources().getString(R.string.list_footer_loading) : loadingHint);
                mLoadingText.setTextColor(ContextCompat.getColor(getContext(), hintColor));

                break;
            case NoMore:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }

                if (mTheEndView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.end_viewstub);
                    mTheEndView = viewStub.inflate();

                    mNoMoreText = (TextView) mTheEndView.findViewById(R.id.loading_end_text);
                } else {
                    mTheEndView.setVisibility(VISIBLE);
                }

                mTheEndView.setVisibility(showView ? VISIBLE : GONE);
                mNoMoreText.setText(TextUtils.isEmpty(noMoreHint) ? getResources().getString(R.string.list_footer_end) : noMoreHint);
                mNoMoreText.setTextColor(ContextCompat.getColor(getContext(), hintColor));
                break;
            case NetWorkError:

                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                if (mNetworkErrorView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.network_error_viewstub);
                    mNetworkErrorView = viewStub.inflate();
                    mNoNetWorkText = (TextView) mNetworkErrorView.findViewById(R.id.network_error_text);
                } else {
                    mNetworkErrorView.setVisibility(VISIBLE);
                }

                mNetworkErrorView.setVisibility(showView ? VISIBLE : GONE);
                mNoNetWorkText.setText(TextUtils.isEmpty(noNetWorkHint) ? getResources().getString(R.string.list_footer_network_error) : noNetWorkHint);
                mNoNetWorkText.setTextColor(ContextCompat.getColor(getContext(), hintColor));
                break;
            default:
                break;
        }
    }

    public enum State {
        Normal/**正常*/, NoMore/**加载到最底了*/, Loading/**加载中..*/, NetWorkError/**网络异常*/
    }
}