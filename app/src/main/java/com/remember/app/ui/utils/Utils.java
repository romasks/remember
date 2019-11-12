package com.remember.app.ui.utils;

import com.pixplicity.easyprefs.library.Prefs;

public class Utils {

    public static boolean isEmptyPrefsKey(String key) {
        return Prefs.getString(key, "").isEmpty();
    }

}
