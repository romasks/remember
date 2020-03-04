package com.remember.app.ui.utils;

public class StringUtils {

    public static String getStringFromField(String field) {
        return field.isEmpty() || field.equals("null") ? "-" : field;
    }

}
