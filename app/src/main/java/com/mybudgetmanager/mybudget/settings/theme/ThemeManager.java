package com.mybudgetmanager.mybudget.settings.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String SELECTED_THEME = "Locale.Helper.Selected.Theme";
    public static boolean isNight;


    public static void getSettings(Context context) {
        setSelectedTheme(context, getTheme(context));
    }

    public static void setSelectedTheme(Context context, boolean on) {
        isNight = on;
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
            );
            context.setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
            );
        }

        persist(context);
    }

    public static boolean getTheme(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        isNight = preferences.getBoolean(SELECTED_THEME, false);
        return isNight;
    }

    public static void persist(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SELECTED_THEME, isNight);
        editor.apply();
    }
}
