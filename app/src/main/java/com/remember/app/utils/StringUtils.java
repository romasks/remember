package com.remember.app.utils;

import android.text.Html;

public class StringUtils {

    public static String getStringFromField(String field) {
        return field.isEmpty() || field.equals("null") ? "-" : field;
    }

    public static String getVideoIdFromUrl(String url){
        return url.replace("https://www.youtube.com/embed/", "").replace("https://www.youtube.com/watch?v=","").replace("https://m.youtube.com/watch?v=","");
    }

    public static String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String s = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
            return s.replaceAll("[\\r\\n]+", "\n");
        } else {
            return Html.fromHtml(html).toString();
        }
    }
}
