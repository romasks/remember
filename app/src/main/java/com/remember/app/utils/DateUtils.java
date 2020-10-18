package com.remember.app.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static DateFormat dfLocal = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    /*public static long startDate = 0;
    public static long endDate = 0;
    public static Calendar dateAndTime = Calendar.getInstance();*/

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
    public static Date parseRemoteFormatWithException(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
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

    @SuppressLint("SimpleDateFormat")
    public static Date parseLocalFormatWithException(String dateStr) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
    }

    public static String convertReligiousEventServerFormat(String dateStr) {
        return convertFormats(dateStr, "MM.dd", "dd.MM");
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

    public static long getDifferenceDays(String date, String format) throws ParseException {
        Calendar past = Calendar.getInstance();
        if (format.equals("LOCAL")) {
            past.setTime(parseLocalFormatWithException(date));
        } else if (format.equals("REMOTE")) {
            past.setTime(parseRemoteFormatWithException(date));
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, past.get(Calendar.YEAR));

        long diff;
        if (!today.after(past)) {
            diff = today.getTime().getTime() - past.getTime().getTime();
        } else {
            today.set(Calendar.YEAR, past.get(Calendar.YEAR) - 1);
            diff = past.getTime().getTime() - today.getTime().getTime();
        }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) - 1;
    }

    /*public static void compareDates(int year, int monthOfYear, int dayOfMonth, String identificator) {
        switch (identificator) {
            case "start":
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate = dateAndTime.getTimeInMillis();
                Date end = parseLocalFormat(endDate.getText().toString());
                if (endDate != null && dateAndTime.getTime().after(endDate)) {
                    Utils.showSnack(dateBegin, getResources().getString(R.string.events_error_date_death_before_date_birth));
                } else {
                    setInitialDateBegin();
                }
                break;
            case "end":
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate = dateAndTime.getTimeInMillis();
                break;
        }
    }*/
}
