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
    public static String convertToLocalFormat(String dateStr) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(date);
    }

}
