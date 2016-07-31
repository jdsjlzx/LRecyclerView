package com.lzx.demo.util;

import android.content.Context;

/**
 * Created by lizhixian on 16/7/30.
 */

public class AppUtil {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
