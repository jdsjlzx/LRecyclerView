package com.lzx.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.github.jdsjlzx.interfaces.IRefreshHeader;
import com.github.jdsjlzx.util.WeakHandler;
import com.lzx.demo.R;

public class MomentsRefreshHeader extends FrameLayout implements IRefreshHeader {

  private int mHeaderViewHeight; // 头部高度
  private ImageView mRefreshView; // 旋转刷新的图片
  private float mRefreshHideTranslationY; // 刷新图片上移的最大距离
  private float mRefreshShowTranslationY; // 刷新图片下拉的最大移动距离
  private float mRotateAngle; // 旋转角度
  private int mState = STATE_NORMAL;

  private WeakHandler mHandler = new WeakHandler();

  public MomentsRefreshHeader(Context context) {
    super(context);
    initView();
  }

  public MomentsRefreshHeader(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  private void initView() {
    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    lp.setMargins(0, 0, 0, 0);
    this.setLayoutParams(lp);
    this.setPadding(0, 0, 0, 0);
    inflate(getContext(), R.layout.moments_header, this);
    mRefreshView = findViewById(R.id.iv_refresh);
    measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    mHeaderViewHeight = getMeasuredHeight();
    mRefreshHideTranslationY = -mRefreshView.getMeasuredHeight() - 20;
    mRefreshShowTranslationY = mRefreshView.getMeasuredHeight();
  }

  public void setState(int state) {
    if (state == mState) return;

    if (state == STATE_REFRESHING) {  // 显示进度
      mRefreshView.setTranslationY(mRefreshShowTranslationY);
      refreshing();
    } else if (state == STATE_DONE) {
      reset();
    }

    mState = state;
  }

  @Override public void refreshComplete() {
    mHandler.postDelayed(new Runnable() {
      public void run() {
        setState(STATE_DONE);
      }
    }, 200);
  }

  @Override public View getHeaderView() {
    return this;
  }

  @Override public int getVisibleHeight() {
    return getHeight() - mHeaderViewHeight;
  }

  private int getHeaderViewHeight() {
    return getLayoutParams().height;
  }

  private void setHeaderViewHeight(int height) {
    if (height < mHeaderViewHeight) height = mHeaderViewHeight;
    getLayoutParams().height = height;
    requestLayout();
  }

  //垂直滑动时该方法不实现
  @Override public int getVisibleWidth() {
    return 0;
  }

  @Override public int getType() {
    return TYPE_HEADER_MATERIAL;
  }

  @Override public void onReset() {
    setState(STATE_NORMAL);
  }

  @Override public void onPrepare() {
    setState(STATE_RELEASE_TO_REFRESH);
  }

  @Override public void onRefreshing() {
    setState(STATE_REFRESHING);
  }

  @Override public void onMove(float offSet, float sumOffSet) {
    int top = getTop();// 相对父容器recyclerview的顶部位置 负数表示向上划出父容器的距离
    int currentHeight = getHeaderViewHeight();
    int targetHeight = currentHeight - (int) offSet;
    if (offSet < 0 && top == 0) {
      setHeaderViewHeight(targetHeight);
      refreshTranslation(currentHeight, offSet);
    } else if (offSet > 0 && currentHeight > mHeaderViewHeight) {
      layout(getLeft(), 0, getRight(), targetHeight); //重新布局让header显示在顶端，直到不再缩小图片
      setHeaderViewHeight(targetHeight);
      refreshTranslation(currentHeight, offSet);
    }
  }

  /**
   * refreshView在刷新区间内相对位移并跟随位移速度旋转
   */
  private void refreshTranslation(int currentHeight, float offSet) {
    if ((currentHeight - mHeaderViewHeight) / 2 < mRefreshShowTranslationY - mRefreshHideTranslationY) { // 判断是否在非刷新区间
      float translationY = mRefreshView.getTranslationY() - offSet / 2; // 布局高度增加offset 相当于距离上边距offSet / 2
      if (translationY > mRefreshShowTranslationY) {
        translationY = mRefreshShowTranslationY;
      } else if (translationY < mRefreshHideTranslationY) {
        translationY = mRefreshHideTranslationY;
      }
      if (Math.abs(translationY) != mRefreshView.getTranslationY()) {
        mRefreshView.setTranslationY(translationY);
      }
    }
    mRefreshView.setRotation(mRotateAngle -= offSet);//旋转，角度大小跟随偏移量
  }

  @Override public boolean onRelease() {
    boolean isOnRefresh = false;
    int currentHeight = getLayoutParams().height;// 使用 mHeaderView.getLayoutParams().height 可以防止快速快速下拉的时候图片不回弹
    if (currentHeight > mHeaderViewHeight) {
      if ((currentHeight - mHeaderViewHeight) / 2 > mRefreshShowTranslationY - mRefreshHideTranslationY && mState < STATE_REFRESHING) {
        setState(STATE_REFRESHING);
        isOnRefresh = true;
      }
      headerRest();
    }
    if (!isOnRefresh && mRefreshView.getTranslationY() != mRefreshHideTranslationY) {
      refreshRest();
    }
    return isOnRefresh;
  }

  public void reset() {
    refreshRest();
    mHandler.postDelayed(new Runnable() {
      public void run() {
        setState(STATE_NORMAL);
      }
    }, 500);
  }

  private void headerRest() {
    ValueAnimator animator = ValueAnimator.ofInt(getLayoutParams().height, mHeaderViewHeight);
    //animator.setStartDelay(60);
    animator.setDuration(300).start();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        if (getLayoutParams().height == mHeaderViewHeight) { // 停止动画，防止快速上划松手后动画产生抖动
          animation.cancel();
        } else {
          setHeaderViewHeight((Integer) animation.getAnimatedValue());
        }
      }
    });
  }

  private void refreshing() {
    mHandler.postDelayed(new Runnable() {
      @Override public void run() {
        if (mState == STATE_REFRESHING) {
          mRefreshView.setRotation(mRotateAngle += 8);
          mHandler.post(this);
        }
      }
    }, 50);
  }

  private void refreshRest() {
    ValueAnimator animator = ValueAnimator.ofFloat(mRefreshView.getTranslationY(), mRefreshHideTranslationY);
    animator.setStartDelay(60);
    animator.setDuration(300).start();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        if (mRefreshView.getTranslationY() == mRefreshHideTranslationY) {
          animation.cancel();
        } else {
          mRefreshView.setTranslationY((Float) animation.getAnimatedValue());
        }
      }
    });
  }
}