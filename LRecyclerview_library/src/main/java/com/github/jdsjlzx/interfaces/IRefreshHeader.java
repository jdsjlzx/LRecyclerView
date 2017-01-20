package com.github.jdsjlzx.interfaces;

import android.view.View;

public interface IRefreshHeader {

	int STATE_NORMAL = 0;
	int STATE_RELEASE_TO_REFRESH = 1;
	int STATE_REFRESHING = 2;
	int STATE_DONE = 3;

	/**
	 * 下拉移动
	 */
	void onMove(float delta);

	/**
	 * 下拉松开
	 */
	boolean releaseAction();

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
}