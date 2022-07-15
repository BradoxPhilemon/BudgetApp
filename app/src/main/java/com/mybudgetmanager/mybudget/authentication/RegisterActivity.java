package com.mybudgetmanager.mybudget.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.util.InputValidation;

public class RegisterActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SIGN_IN = 201;

    private TextInputEditText tiet_name;
    private TextInputEditText tiet_email;
    private TextInputEditText tiet_password;
    private TextView tv_redirect_to_login;
    private Button btn_register_email;

    Intent intent;

    private EmailAuthentication emailAuthentication;
    private GoogleAuthentication googleAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        emailAuthentication = new EmailAuthentication(getApplicationContext());
        googleAuthentication = new GoogleAuthentication(getApplicationContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN && data != null) {
            googleAuthentication.handleSignInResult(data);
        }
    }

    private void initComponents() {
        intent = getIntent();

        tiet_name = findViewById(R.id.register_name);
        tiet_email = findViewById(R.id.register_email);
        tiet_password = findViewById(R.id.register_password);
        tv_redirect_to_login = findViewById(R.id.login_redirect_to_login);
        btn_register_email = findViewById(R.id.register_create_account_btn);

        tv_redirect_to_login.setOnClickListener(returnToLoginEventListener());
        btn_register_email.setOnClickListener(emailAuthEventListener());
    }


    private View.OnClickListener returnToLoginEventListener() {
        return v -> {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            setResult(RESULT_CANCELED);
            finish();
        };
    }

    private View.OnClickListener emailAuthEventListener() {
        return v -> {
            if (validate()) {
                emailAuthentication.registerAccount(tiet_name, tiet_email, tiet_password);
            }
        };
    }

    private boolean validate() {
        return InputValidation.registerValidation(getApplicationContext(), tiet_name, tiet_email, tiet_password);
    }

}