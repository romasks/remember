package com.remember.app.ui.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtils {

    public static void setGlideImage(Context context, Object imageUrl, ImageView targetView) {
        Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(targetView);
    }

}
