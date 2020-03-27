package com.remember.app.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.GlideApp;
import com.remember.app.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.CROP_IMAGE_RECT;
import static com.remember.app.ui.utils.Utils.convertDpToPixels;

public class ImageUtils {

    private static ColorMatrixColorFilter blackWhiteFilter;

    static {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        blackWhiteFilter = new ColorMatrixColorFilter(matrix);
    }

    public static void setGlideImage(Object imageObj, ImageView targetView) {
        if (isUploadedImage(imageObj)) {
            imageObj = BASE_SERVICE_URL + imageObj;
        }
        setGlideImage(targetView.getContext(), imageObj, targetView);
    }

    public static void setGlideImage(Context context, Object imageObj, ImageView targetView) {
        if (imageObj instanceof String) {
            try {
                imageObj = encodeImageUrl((String) imageObj);
            } catch (MalformedURLException | URISyntaxException ignored) {
            }
        }
        GlideApp.with(context)
                .load(imageObj)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void glideLoadInto(Object imageObj, ImageView targetView) {
        glideLoadInto(targetView.getContext(), imageObj, targetView);
    }

    public static void glideLoadInto(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .load(imageObj)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void glideLoadIntoAsBitmap(Object imageObj, ImageView targetView) {
        GlideApp.with(targetView.getContext())
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

    public static void glideLoadIntoWithError(Object imageObj, ImageView targetView) {
        if (isUploadedImage(imageObj)) {
            imageObj = BASE_SERVICE_URL + imageObj;
        }
        glideLoadIntoWithError(targetView.getContext(), imageObj, targetView);
    }

    public static void glideLoadIntoWithError(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .load(imageObj)
                .error(R.drawable.darth_vader)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    public static void setGridImage(Object imageObj, ImageView targetView, Point size) {
        if (isUploadedImage(imageObj)) {
            imageObj = BASE_SERVICE_URL + imageObj;

            if (size != null) {
                targetView.setMinimumHeight(size.x / 3);
                targetView.setMaxHeight(size.x / 3);
                targetView.setMinimumWidth(size.x / 3);
            }
        }
        setGridImage(targetView.getContext(), imageObj, targetView);
    }

    public static ColorMatrixColorFilter getBlackWhiteFilter() {
        return blackWhiteFilter;
    }

    public static Bitmap createBitmapFromView(ImageView sharedImage) {
        View view = sharedImage;
        view.measure(
                View.MeasureSpec.makeMeasureSpec(convertDpToPixels(view.getWidth()), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(convertDpToPixels(view.getHeight()), View.MeasureSpec.EXACTLY)
        );
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable background = view.getBackground();

        if (background != null) {
            background.draw(canvas);
        }
        view.draw(canvas);

        return bitmap;
    }

    public static void cropImage(AppCompatActivity activity) {
        cropImage(activity, CROP_IMAGE_RECT);
    }

    public static void cropImage(AppCompatActivity activity, CropImageView.CropShape cropShape) {
        CropImage.activity()
                .setMinCropResultSize(400, 400)
                .setMaxCropResultSize(7000, 7000)
                .setFixAspectRatio(true)
                .setCropShape(cropShape)
                .start(activity);
    }

    public static void cropImage(Fragment fragment, CropImageView.CropShape cropShape) {
        CropImage.activity()
                .setMinCropResultSize(400, 400)
                .setMaxCropResultSize(7000, 7000)
                .setFixAspectRatio(true)
                .setCropShape(cropShape)
                .start(fragment.getContext(), fragment);
    }

    private static void setGridImage(Context context, Object imageObj, ImageView targetView) {
        GlideApp.with(context)
                .load(imageObj)
                .error(R.drawable.darth_vader)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(targetView);
        targetView.setColorFilter(blackWhiteFilter);
    }

    private static URL encodeImageUrl(String urlStr) throws MalformedURLException, URISyntaxException {
        URL url = new URL(urlStr);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        return uri.toURL();
    }

    private static boolean isUploadedImage(Object imageObj) {
        return imageObj instanceof String && ((String) imageObj).contains("uploads") && !((String) imageObj).contains(BASE_SERVICE_URL);
    }

}
