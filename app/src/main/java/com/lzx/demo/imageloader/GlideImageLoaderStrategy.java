package com.lzx.demo.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Lzx on 2016/12/8.
 *
 * using {@link Glide} to load image
 * 参考：https://github.com/CameloeAnthony/Ant
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {
    @Override
    public void loadImage(Context context, ImageLoader imageLoader) {
        //if currently not under wifi
        if(!ImageLoaderUtil.wifiFlag) {
            loadNormal(context,imageLoader);
        }

        int strategy = imageLoader.getWifiStrategy();

        if (strategy == ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI) {
            int netType = ImageLoaderUtil.getNetWorkType(context);
            //if wifi ,load pic
            if (netType == ImageLoaderUtil.NETWORKTYPE_WIFI) {
                loadNormal(context,imageLoader);
            } else {
                //if not wifi ,load cache
                loadCache(context,imageLoader);
            }
        } else {
            //如果不是在wifi下才加载图片
            loadNormal(context,imageLoader);
        }
    }

    /**
     * load image with Glide
     */
    private void loadNormal(Context context, ImageLoader imageLoader) {
        Glide.with(context).load(imageLoader.getUrl()).placeholder(imageLoader.getPlaceHolder()).into(imageLoader.getImgView());
    }

    /**
     * load cache image with Glide
     */
    private void loadCache(Context context, ImageLoader imageLoader) {

        //缓存策略解说：
        //all:缓存源资源和转换后的资源
        //none:不作任何磁盘缓存
        //source:缓存源资源
        //result：缓存转换后的资源
        Glide.with(context).load(imageLoader.getUrl()).placeholder(imageLoader.getPlaceHolder()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageLoader.getImgView());
    }
}
