package com.mybudgetmanager.mybudget.main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;


import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.authentication.LoginActivity;
import com.mybudgetmanager.mybudget.model.Account;
import com.mybudgetmanager.mybudget.settings.theme.ThemeManager;
import com.mybudgetmanager.mybudget.util.Streak;
import com.rbddevs.splashy.Splashy;



public class SplashActivity extends AppCompatActivity {

    private final int DISPLAY_DURATION = 4000;
    private final int ANIMATION_DURATION = 3000;
    public static String KEY;
    Intent intent;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ThemeManager.getTheme(getApplicationContext());

        KEY = getSharedPreferences(Account.USER_PREFS, MODE_PRIVATE)
                .getString(Account.USER_KEY, null);

        new Splashy(this)
                .setLogo(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setSubTitle(R.string.app_motto)
                .setDuration(DISPLAY_DURATION)
                .setAnimation(Splashy.Animation.GROW_LOGO_FROM_CENTER, ANIMATION_DURATION)
                .show();

        new Handler().postDelayed(() -> {
            if (KEY == null) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            } else {
                Account.retrieveAccount(getApplicationContext());

                intent = new Intent(getApplicationContext(), MainActivity.class);
                Streak.handleStreak(getApplicationContext());
            }
            finish();
            startActivity(intent);
        }, DISPLAY_DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}