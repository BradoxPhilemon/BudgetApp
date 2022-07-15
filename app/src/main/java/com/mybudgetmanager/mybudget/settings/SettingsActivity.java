package com.mybudgetmanager.mybudget.settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.authentication.GoogleAuthentication;
import com.mybudgetmanager.mybudget.main.MainActivity;
import com.mybudgetmanager.mybudget.main.SplashActivity;
import com.mybudgetmanager.mybudget.model.Account;
import com.mybudgetmanager.mybudget.settings.balances.BalancesActivity;
import com.mybudgetmanager.mybudget.settings.categories.CategoriesActivity;
import com.mybudgetmanager.mybudget.settings.language.LanguageManager;
import com.mybudgetmanager.mybudget.settings.language.Languages;
import com.mybudgetmanager.mybudget.settings.notifications.Notifications;
import com.mybudgetmanager.mybudget.settings.target.TargetActivity;
import com.mybudgetmanager.mybudget.settings.theme.ThemeManager;
import com.mybudgetmanager.mybudget.util.Streak;


public class SettingsActivity extends AppCompatActivity {

    private TextView tv_user_name;
    private TextView tv_user_email;
    private TextView tv_user_currency;
    private TextView tv_streak;
    private ImageButton btn_back;
    private Button btn_currency;
    private Button btn_converter;
    private Button btn_balances;
    private Button btn_categories;
    private Button btn_target;
    private Button btn_templates;
    private Button btn_theme;
    private Button btn_language;
    private SwitchCompat switch_reminder;
    private Button btn_contact;
    private Button btn_sign_out;

    private AlertDialog themeDialog;
    private AlertDialog languageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.e("test", "SettingsActivity - onCreate");

        initComponents();
        if (Account.getInstance() != null) {
            setAccount();
        }
        initEventListeners();
    }

    private void initComponents() {
        tv_user_name = findViewById(R.id.account_name);
        tv_user_email = findViewById(R.id.account_email);
        tv_user_currency = findViewById(R.id.account_currency);
        tv_streak = findViewById(R.id.account_streak);
        btn_back = findViewById(R.id.settings_back);
        btn_currency = findViewById(R.id.settings_currency);
        btn_converter = findViewById(R.id.settings_converter);
        btn_balances = findViewById(R.id.settings_balances);
        btn_categories = findViewById(R.id.settings_categories);
        btn_target = findViewById(R.id.settings_target);
        btn_templates = findViewById(R.id.settings_templates);
        btn_theme = findViewById(R.id.settings_theme);
        btn_language = findViewById(R.id.settings_language);
        switch_reminder = findViewById(R.id.settings_switch_reminders);
        btn_contact = findViewById(R.id.settings_contact);
        btn_sign_out = findViewById(R.id.settings_sign_out);
    }

    private void setAccount() {
        tv_user_name.setText(Account.getInstance().getName());
        tv_user_email.setText(Account.getInstance().getEmail());
        if (Account.getInstance().getCurrency() != null) {
            String account_currency = getResources().getString(R.string.settings_account_currency,
                    Account.getInstance().getCurrency().toString());
            tv_user_currency.setText(account_currency);
        } else {
            tv_user_currency.setText("");
        }
        String account_streak = getResources().getString(R.string.settings_account_streak, Streak.days);
        tv_streak.setText(account_streak);
    }

    private void initEventListeners() {
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        btn_balances.setOnClickListener(balancesEventListener());
        btn_categories.setOnClickListener(categoriesEventListener());
        btn_currency.setOnClickListener(currencyEventListener());
        btn_converter.setOnClickListener(converterEventListener());
        btn_target.setOnClickListener(targetEventListener());
        btn_templates.setOnClickListener(templatesEventListener());
        btn_theme.setOnClickListener(themeEventListener());
        btn_language.setOnClickListener(languageEventListener());

        switch_reminder.setChecked(LanguageManager.getSelectedReminders(getApplicationContext()));
        switch_reminder.setOnCheckedChangeListener(remindersEventListener());

        btn_contact.setOnClickListener(contactEventListener());
        btn_sign_out.setOnClickListener(signOutClickListener());
    }

    private View.OnClickListener targetEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
            startActivity(intent);
        };
    }

    private View.OnClickListener signOutClickListener() {
        return v -> {
            Account.signOut(getApplicationContext());
            GoogleAuthentication googleAuthentication = new GoogleAuthentication(getApplicationContext());
            googleAuthentication.signOut();

            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
        };
    }


    private View.OnClickListener converterEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), ConverterActivity.class);
            startActivity(intent);
        };
    }

    private View.OnClickListener currencyEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), CurrencyActivity.class);
            startActivity(intent);
        };
    }


    private View.OnClickListener balancesEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), BalancesActivity.class);
            startActivity(intent);
        };
    }

    private View.OnClickListener categoriesEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
            startActivity(intent);
        };
    }

    private View.OnClickListener templatesEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), TemplatesActivity.class);
            startActivity(intent);
        };
    }


    private View.OnClickListener themeEventListener() {
        return v -> {
            CharSequence[] themes = {getResources().getString(R.string.theme_light), getResources().getString(R.string.theme_dark)};
            int checkedTheme = ThemeManager.getTheme(getApplicationContext()) ? 1 : 0;
            themeDialog = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(themes,
                            checkedTheme, (dialog, which) -> {

                                boolean isNightTheme = true;
                                if (which == 0) {
                                    isNightTheme = false;
                                }

                                ThemeManager.setSelectedTheme(getBaseContext(), isNightTheme);
                            })
                    .show();
        };
    }

    private View.OnClickListener languageEventListener() {
        return v -> {
            CharSequence[] languages = {getResources().getString(R.string.language_en),
                    getResources().getString(R.string.language_ro)};
            int checkedLanguage = LanguageManager.getSelectedLanguage(getApplicationContext()).equals(Languages.ENGLISH.toString()) ? 0 : 1;
            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(languages,
                            checkedLanguage, (dialog, which) -> {

                                String selectedLanguage = Languages.ENGLISH.toString();
                                if (which == 1) {
                                    selectedLanguage = Languages.ROMANIAN.toString();
                                }

                                LanguageManager.setLanguage(getBaseContext(), selectedLanguage);
                                LanguageManager.apply(getBaseContext());

                                Intent refresh = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(refresh);
                            })
                    .show();
        };
    }

    private CompoundButton.OnCheckedChangeListener remindersEventListener() {
        return (buttonView, isChecked) -> {
            Notifications notifications;
            if (isChecked) {
                notifications = new Notifications(getApplicationContext());
            } else {
                notifications = null;
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(LanguageManager.SELECTED_REMINDERS, isChecked);
            editor.apply();
        };
    }

    private View.OnClickListener contactEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (themeDialog != null && themeDialog.isShowing()) {
            themeDialog.cancel();
        }
        if (languageDialog != null && languageDialog.isShowing()) {
            languageDialog.cancel();
        }
    }

}