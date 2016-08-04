package com.github.jdsjlzx.swipe;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.lang.reflect.Method;

public class ResCompat {

    public static Drawable getDrawable(Context context, int drawableId) {
        return getDrawable(context, drawableId, null);
    }

    public static Drawable getDrawable(Context context, int drawableId, Theme theme) {
        Resources resources = context.getResources();
        Class<?> resourcesClass = resources.getClass();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            try {
                Method getDrawableMethod = resourcesClass.getMethod("getDrawable", int.class, Theme.class);
                getDrawableMethod.setAccessible(true);
                return (Drawable) getDrawableMethod.invoke(resources, drawableId, theme);
            } catch (Throwable e) {
            }
        else
            try {
                Method getDrawableMethod = resourcesClass.getMethod("getDrawable", int.class);
                getDrawableMethod.setAccessible(true);
                return (Drawable) getDrawableMethod.invoke(resources, drawableId);
            } catch (Throwable e) {
            }
        return null;
    }

    public static int getColor(Context context, int colorId) {
        return getColor(context, colorId, null);
    }

    public static int getColor(Context context, int colorId, Theme theme) {
        Resources resources = context.getResources();
        Class<?> resourcesClass = resources.getClass();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            try {
                Method getColorMethod = resourcesClass.getMethod("getColor", int.class, Theme.class);
                getColorMethod.setAccessible(true);
                return (Integer) getColorMethod.invoke(resources, colorId, theme);
            } catch (Throwable e) {
            }
        else
            try {
                Method getColorMethod = resourcesClass.getMethod("getColor", int.class);
                getColorMethod.setAccessible(true);
                return (Integer) getColorMethod.invoke(resources, colorId);
            } catch (Throwable e) {
            }
        return Color.BLACK;
    }

    public static void setBackground(View view, int drawableId) {
        setBackground(view, getDrawable(view.getContext(), drawableId));
    }

    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            setBackground("setBackground", view, background);
        else
            setBackground("setBackgroundDrawable", view, background);
    }

    public static void setBackground(String method, View view, Drawable background) {
        try {
            Method viewMethod = view.getClass().getMethod(method, Drawable.class);
            viewMethod.setAccessible(true);
            viewMethod.invoke(view, background);
        } catch (Throwable e) {
        }
    }
}
