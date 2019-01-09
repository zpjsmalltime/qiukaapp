package com.mayisports.qca.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * banner 图加载器
 * Created by Zpj on 2017/12/29.
 */

public class ImageLoaderType extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        String url = (String) path;
        url = url.split(",")[0];
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        PictureUtils.show(url,imageView);
    }
}
