package com.lzx.demo.imageloader;

import android.content.Context;

/**
 * Created by Lzx on 2016/12/8.
 * abstract class/interface defined to load image
 * (Strategy Pattern used here)
 */

public interface BaseImageLoaderStrategy {
    void loadImage(Context context, ImageLoader imageLoader);
}
