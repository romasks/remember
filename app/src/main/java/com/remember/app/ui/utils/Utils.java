package com.remember.app.ui.utils;

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

}
