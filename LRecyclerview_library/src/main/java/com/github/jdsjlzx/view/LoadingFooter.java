package com.github.jdsjlzx.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.R;
import com.github.jdsjlzx.interfaces.ILoadMoreFooter;
import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

public class LoadingFooter extends RelativeLayout implements ILoadMoreFooter {

    protected State mState = State.Normal;
    private View mLoadingView;
    private View mNetworkErrorView;
    private View mTheEndView;
    private SimpleViewSwitcher mProgressView;
    private TextView mLoadingText;
    private TextView mNoMoreText;
    private TextView mNoNetWorkText;
    private String loadingHint;
    private String noMoreHint;
    private String noNetWorkHint;
    private int style;
    private int indicatorColor;
    private int hintColor = R.color.colorAccent;

    public LoadingFooter(Context context) {
        super(context);
        init();
    }

    public LoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

        inflate(getContext(), R.layout.layout_recyclerview_footer, this);
        setOnClickListener(null);

        onReset();//初始为隐藏状态

        indicatorColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        style = ProgressStyle.BallPulse;
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
    }

    public void setHintTextColor(int color) {
        this.hintColor = color;
    }

    public void setViewBackgroundColor(int color) {
        this.setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    public void setProgressStyle(int style) {
        this.style = style;
    }

    public State getState() {
        return mState;
    }

    public void setState(State status) {
        setState(status, true);
    }

    private View initIndicatorView(int style) {
        if (style == ProgressStyle.SysProgress) {
            return new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
        } else {
            AVLoadingIndicatorView progressView = (AVLoadingIndicatorView) LayoutInflater.from(getContext()).inflate(R.layout.layout_indicator_view, null);
            progressView.setIndicatorId(style);
            progressView.setIndicatorColor(indicatorColor);
            return progressView;
        }

    }

    @Override
    public void onReset() {
        onComplete();
    }

    @Override
    public void onLoading() {
        setState(State.Loading);
    }

    @Override
    public void onComplete() {
        setState(State.Normal);
    }

    @Override
    public void onNoMore() {
        setState(State.NoMore);
    }

    @Override
    public View getFootView() {
        return this;
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

                    mProgressView = (SimpleViewSwitcher) mLoadingView.findViewById(R.id.loading_progressbar);
                    mLoadingText = (TextView) mLoadingView.findViewById(R.id.loading_text);
                }

                mLoadingView.setVisibility(showView ? VISIBLE : GONE);

                mProgressView.setVisibility(View.VISIBLE);
                mProgressView.removeAllViews();
                mProgressView.addView(initIndicatorView(style));

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
        Normal/**正常*/
        , NoMore/**加载到最底了*/
        , Loading/**加载中..*/
        , NetWorkError/**网络异常*/
    }
}