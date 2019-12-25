package com.remember.app.ui.utils;

import android.content.Context;
import android.graphics.Point;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import static com.remember.app.data.Constants.PREFS_KEY_IS_THEME;
import static com.remember.app.data.Constants.THEME_DARK;

public class Utils {

    public static boolean isThemeDark() {
        return Prefs.getInt(PREFS_KEY_IS_THEME, 0) == THEME_DARK;
    }

    public static void setTheme(AppCompatActivity activity) {
        if (Utils.isThemeDark()) {
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            activity.setTheme(R.style.AppTheme_Dark);
        } else {
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            activity.setTheme(R.style.AppTheme);
        }
    }

    public static boolean isEmptyPrefsKey(String key) {
        return Prefs.getString(key, "").isEmpty();
    }

    public static void showSnack(View view, String message) {
        Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">" + message + "</font>"), Snackbar.LENGTH_SHORT)
                .setActionTextColor(view.getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public static Point getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = null;
        if (wm != null) {
            size = new Point();
            wm.getDefaultDisplay().getSize(size);
        }
        return size;
    }

}
