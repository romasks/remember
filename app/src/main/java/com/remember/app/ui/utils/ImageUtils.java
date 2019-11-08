package com.remember.app.ui.utils;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.R;

public class ImageUtils {

    public static void setGlideSquareImage(Context context, Object imageUrl, ImageView targetView) {
        Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.darth_vader);
        Glide.with(context)
                .load(imageUrl)
                .error(mDefaultBackground)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(targetView);
    }

    public static void setGlideImage(Context context, Object imageUrl, ImageView targetView) {
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.ic_unknown)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(targetView);
    }

    public static void setBlackWhite(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

}
