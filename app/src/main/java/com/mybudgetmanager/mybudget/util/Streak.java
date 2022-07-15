package com.mybudgetmanager.mybudget.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

public class Streak {


    private static final String FIRST_DAY = "first_day";
    private static final String LAST_DAY = "last_day";
    private static final String STREAK = "streak";

    public static int days;

    public static Date first_day;
    public static Date last_day;

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return DateConverter.toDate(day, month, year);
    }

    public static boolean validateStreak() {
        int days = daysBetween(last_day, getToday());
        return days <= 1;
    }

    private static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static void getDays() {
        days = daysBetween(first_day, last_day);
    }

    public static void handleStreak(Context context) {
        getStreak(context);
        if (validateStreak()) {
            last_day = getToday();
            getDays();
        } else {
            first_day = getToday();
            last_day = getToday();
        }
        saveStreak(context);
    }


    private static void getStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(STREAK, Context.MODE_PRIVATE);

        String first = prefs.getString(FIRST_DAY, null);
        String last = prefs.getString(LAST_DAY, null);

        if (first != null) {
            Streak.first_day = DateConverter.toDate(first);
        } else {
            Streak.first_day = Streak.getToday();
        }

        if (last != null) {
            Streak.last_day = DateConverter.toDate(last);
        } else {
            Streak.last_day = Streak.getToday();
        }
    }

    private static void saveStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(STREAK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FIRST_DAY, DateConverter.toString(Streak.first_day));
        editor.putString(LAST_DAY, DateConverter.toString(Streak.last_day));
        editor.apply();
    }

}
