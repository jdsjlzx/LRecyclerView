package com.lzx.demo.util;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>
 * 绘制分割线工具类
 */
public class DividerHelper {

    // 将分割线画在view的顶部，并且左右会多出分割线的宽度
    public static void drawTop(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int left = child.getLeft() - params.leftMargin - drawable.getIntrinsicWidth();
        final int right = child.getRight() + params.rightMargin + drawable.getIntrinsicWidth();
        final int top = child.getTop() - params.topMargin - drawable.getIntrinsicHeight();
        final int bottom = top + drawable.getIntrinsicHeight();

        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    // 将分割线画在view的底部，并且左右会多出分割线的宽度
    public static void drawBottom(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int left = child.getLeft() - params.leftMargin - drawable.getIntrinsicWidth();
        final int right = child.getRight() + params.rightMargin + drawable.getIntrinsicWidth();
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + drawable.getIntrinsicHeight();

        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    // 将分割线画在view的左边，并且上下会多出分割线的高度
    public static void drawLeft(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int top = child.getTop() - params.topMargin - drawable.getIntrinsicHeight();
        final int bottom = child.getBottom() + params.bottomMargin + drawable.getIntrinsicHeight();
        final int left = child.getLeft() - params.leftMargin - drawable.getIntrinsicWidth();
        final int right = left + drawable.getIntrinsicWidth();

        drawable.setBounds(left, top, right, bottom);

        drawable.draw(canvas);
    }

    // 将分割线画在view的右边，并且上下会多出分割线的高度
    public static void drawRight(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int top = child.getTop() - params.topMargin - drawable.getIntrinsicHeight();
        final int bottom = child.getBottom() + params.bottomMargin + drawable.getIntrinsicHeight();
        final int left = child.getRight() + params.rightMargin;
        final int right = left + drawable.getIntrinsicWidth();

        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }


    // 将分割线画在view的顶部，与view左右对齐，考虑margin值
    public static void drawTopAlignItem(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int left = child.getLeft() - params.leftMargin;
        final int right = child.getRight() + params.rightMargin;
        final int top = child.getTop() - params.topMargin - drawable.getIntrinsicHeight();
        final int bottom = top + drawable.getIntrinsicHeight();

        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    // 将分割线画在view的底部，与view左右对齐，考虑margin值
    public static void drawBottomAlignItem(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int left = child.getLeft() - params.leftMargin;
        final int right = child.getRight() + params.rightMargin;
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + drawable.getIntrinsicHeight();

        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    // 将分割线画在view的左边，与Item上下对齐，考虑margin值
    public static void drawLeftAlignItem(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int top = child.getTop() - params.topMargin;
        final int bottom = child.getBottom() + params.bottomMargin;
        final int left = child.getLeft() - params.leftMargin - drawable.getIntrinsicWidth();
        final int right = left + drawable.getIntrinsicWidth();

        drawable.setBounds(left, top, right, bottom);

        drawable.draw(canvas);
    }

    // 将分割线画在view的右边，与Item上下对齐，考虑margin值
    public static void drawRightAlignItem(Canvas canvas, Drawable drawable, View child, ViewGroup.MarginLayoutParams params) {

        final int top = child.getTop() - params.topMargin;
        final int bottom = child.getBottom() + params.bottomMargin;
        final int left = child.getRight() + params.rightMargin;
        final int right = left + drawable.getIntrinsicWidth();

        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }


}
