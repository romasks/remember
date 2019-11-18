package com.remember.app.ui.utils;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.GlideApp;
import com.remember.app.R;

public class ImageUtils {

    private static ColorMatrixColorFilter blackWhiteFilter;

    static {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        blackWhiteFilter = new ColorMatrixColorFilter(matrix);
    }

    public static void setGlideImage(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .load(imageObj)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void setGlideImageWithError(Context context, Object imageUrl, ImageView targetView) {
        Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.darth_vader);
        GlideApp.with(context)
                .load(imageUrl)
                .error(mDefaultBackground)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void glideLoadInto(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .load(imageObj)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void glideLoadIntoAsBitmap(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .asBitmap()
                .load(imageObj)
                .apply(RequestOptions.circleCropTransform())
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void glideLoadIntoCenterInside(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .load(imageObj)
                .centerInside()
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void glideLoadIntoWithError(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .load(imageObj)
                .error(R.drawable.darth_vader)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void glideLoadIntoGrid(Context context, Object imageObj, ImageView targetView) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        targetView.setMinimumHeight(size.x / 3);
        targetView.setMaxHeight(size.x / 3);
        GlideApp.with(context)
                .load(imageObj)
                .error(R.drawable.darth_vader)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static ColorMatrixColorFilter getBlackWhiteFilter() {
        return blackWhiteFilter;
    }

}
