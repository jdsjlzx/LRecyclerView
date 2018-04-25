package com.github.jdsjlzx.interfaces;

import android.view.View;

public interface IRefreshHeader {

	int STATE_NORMAL = 0;
	int STATE_RELEASE_TO_REFRESH = 1;
	int STATE_REFRESHING = 2;
	int STATE_DONE = 3;

	int TYPE_HEADER_NORMAL = 0;
	int TYPE_HEADER_MATERIAL = 1;

	void onReset();

	/**
	 * 处于可以刷新的状态，已经过了指定距离
	 */
	void onPrepare();

	/**
	 * 正在刷新
	 */
	void onRefreshing();

	/**
	 * 下拉移动
	 */
	void onMove(float offSet, float sumOffSet);

	/**
	 * 下拉松开
	 */
	boolean onRelease();

	/**
	 * 下拉刷新完成
	 */
	void refreshComplete();

	/**
	 * 获取HeaderView
	 */
	View getHeaderView();

	/**
	 * 获取Header的显示高度
	 */
	int getVisibleHeight();

	/**
	 * 获取Header的显示宽度,横向滑动时使用
	 */
	int getVisibleWidth();

	/**
	 * 获取Header的类型
	 */
	int getType();
}