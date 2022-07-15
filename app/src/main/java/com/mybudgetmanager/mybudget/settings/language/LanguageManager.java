package com.mybudgetmanager.mybudget.settings.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;


import java.util.Locale;

public class LanguageManager {

    public static Configuration configuration;

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    public static final String SELECTED_REMINDERS = "Locale.Helper.Selected.Reminders";

    public static String getSelectedLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(SELECTED_LANGUAGE, null);
        if (language == null) {
            language = Languages.ENGLISH.toString();
        }
        return language;
    }

    public static boolean getSelectedReminders(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SELECTED_REMINDERS, false);
    }

    public static void getSettings(Context context) {
        context = setLanguage(context, getSelectedLanguage(context));
        apply(context);
    }

    public static void apply(Context context) {
        context.getResources().updateConfiguration(configuration,
                context.getResources().getDisplayMetrics());
    }


    public static Context setLanguage(Context context, String language) {
        persist(context, SELECTED_LANGUAGE, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context = updateResources(context, language);
        } else {
            context = updateResourcesLegacy(context, language);
        }

        return context;
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static void persist(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
