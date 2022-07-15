package com.mybudgetmanager.mybudget.settings.balances;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.model.Balance;
import com.mybudgetmanager.mybudget.util.InputValidation;


public class AddBalanceActivity extends AppCompatActivity {

    public static final String NEW_BALANCE = "new_balance";

    private TextInputEditText tiet_name;
    private TextInputEditText tiet_amount;
    private Button btn_save;

    private Balance balance = new Balance();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);

        initComponents();
    }

    private void initComponents() {
        intent = getIntent();
        tiet_name = findViewById(R.id.add_balance_name);
        tiet_amount = findViewById(R.id.add_balance_available_amount);
        btn_save = findViewById(R.id.add_balance_save);

        btn_save.setOnClickListener(saveBalanceEventListener());
    }

    private View.OnClickListener saveBalanceEventListener() {
        return v -> {
            if (InputValidation.nameValidation(getBaseContext(), tiet_name)) {
                balance = new Balance();
                balance.setName(tiet_name.getText().toString().trim());
                if (InputValidation.availableAmountValidation(getBaseContext(), tiet_amount)) {
                    balance.setAvailable_amount(Double.parseDouble(tiet_amount.getText().toString().trim()));
                    saveBalance();
                }
            }
        };
    }

    private void saveBalance() {
        intent.putExtra(NEW_BALANCE, balance);
        setResult(RESULT_OK, intent);
        finish();
    }

}