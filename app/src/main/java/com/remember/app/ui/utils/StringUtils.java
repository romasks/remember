package com.remember.app.ui.utils;

public class StringUtils {

    public static String getStringFromField(String field) {
        return field.isEmpty() || field.equals("null") ? "-" : field;
    }

    public static String getVideoIdFromUrl(String url){
        return url.replace("https://www.youtube.com/embed/", "").replace("https://www.youtube.com/watch?v=","").replace("https://m.youtube.com/watch?v=","");
    }
}
