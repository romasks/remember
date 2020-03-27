package com.remember.app.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

import static com.remember.app.ui.utils.Utils.getScreenWidth;

public class GrayColorTransformation extends BitmapTransformation {

    private Point screenSize;

    GrayColorTransformation(Context context) {
        super();
        screenSize = getScreenWidth(context);
    }

    @Override
    protected Bitmap transform(@NotNull BitmapPool pool, @NotNull Bitmap toTransform,
                               int outWidth, int outHeight) {
        Bitmap bmpGrayScale = Bitmap.createBitmap(screenSize.x / 3, screenSize.x / 3, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(toTransform, 0, 0, paint);
        return bmpGrayScale;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
