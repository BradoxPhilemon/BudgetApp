package com.mybudgetmanager.mybudget.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.main.MainActivity;
import com.mybudgetmanager.mybudget.main.SplashActivity;
import com.mybudgetmanager.mybudget.util.InputValidation;

public class LoginActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SIGN_IN = 201;
    private TextInputEditText tiet_email;
    private TextInputEditText tiet_password;
    private TextView tv_reset_password;
    private TextView tv_redirect_to_register;
    private Button btn_login;
    private SignInButton btn_login_google;

    private EmailAuthentication emailAuthentication;
    private GoogleAuthentication googleAuthentication;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        emailAuthentication = new EmailAuthentication(getApplicationContext());
        googleAuthentication = new GoogleAuthentication(getApplicationContext());

    }

    private void initComponents() {
        tiet_email = findViewById(R.id.login_email);
        tiet_password = findViewById(R.id.login_password);
        tv_reset_password = findViewById(R.id.login_forgot_password);
        tv_redirect_to_register = findViewById(R.id.login_redirect_to_register);
        btn_login = findViewById(R.id.login_login_btn);
        btn_login_google = findViewById(R.id.login_google_btn);

        tv_reset_password.setOnClickListener(resetPasswordEventListener());
        tv_redirect_to_register.setOnClickListener(redirectToRegisterEvent());
        btn_login.setOnClickListener(emailAuthEventListener());
        btn_login_google.setOnClickListener(googleAuthEventListener());
    }

    private View.OnClickListener resetPasswordEventListener() {
        return v -> {
            if (InputValidation.emailValidation(getBaseContext(), tiet_email)) {
                emailAuthentication.resetPassword(tiet_email.getText().toString().trim());
            }
        };
    }

    private View.OnClickListener googleAuthEventListener() {
        return v -> {
            Intent intent = googleAuthentication.getSignInIntent();
            startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN
                && data != null) {
            googleAuthentication.handleSignInResult(data);
        }
    }


    private View.OnClickListener emailAuthEventListener() {
        return v -> {
            intent = new Intent(getApplicationContext(), MainActivity.class);

            if (validate()) {
                emailAuthentication.loginAccount(tiet_email, tiet_password);
            }
        };
    }


    private View.OnClickListener redirectToRegisterEvent() {
        return v -> {
            intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        };
    }


    private boolean validate() {
        return InputValidation.loginValidation(getBaseContext(), tiet_email, tiet_password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SplashActivity.KEY != null) {
            finish();
        }
    }
}