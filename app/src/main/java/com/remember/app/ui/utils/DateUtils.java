package com.remember.app.ui.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date parseDateWithFormat(String dateStr, DateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseRemoteFormat(String dateStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseLocalFormat(String dateStr) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertRemoteToLocalFormat(String dateStr) {
        return convertFormats(dateStr, "yyyy-MM-dd", "dd.MM.yyyy");
    }

    public static String convertLocalToRemoteFormat(String dateStr) {
        return convertFormats(dateStr, "dd.MM.yyyy", "yyyy-MM-dd");
    }

    @SuppressLint("SimpleDateFormat")
    private static String convertFormats(String dateStr, String srcFormat, String destFormat) {
        try {
            Date date = new SimpleDateFormat(srcFormat).parse(dateStr);
            if (date == null) return dateStr;
            DateFormat df = new SimpleDateFormat(destFormat);
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

}
