package com.mybudgetmanager.mybudget.settings.target;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.firebase.DateDisplayType;
import com.mybudgetmanager.mybudget.firebase.services.BalanceService;
import com.mybudgetmanager.mybudget.firebase.services.TransactionService;
import com.mybudgetmanager.mybudget.model.Balance;
import com.mybudgetmanager.mybudget.model.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class TargetActivity extends AppCompatActivity {

    TextView tv_current_balance;
    TextView tv_initial_balance;
    TextView tv_target_spendings;
    TextView tv_current_spendings;
    Button btn_change_target;

    Target target;
    TransactionService transactionService;
    BalanceService balanceService;

    private List<Transaction> transactionList = new ArrayList<>();
    private List<Balance> balanceList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        initComponents();
        setTarget();
    }

    private void initComponents() {
        btn_change_target = findViewById(R.id.target_change_target);
        btn_change_target.setOnClickListener(changeTargetEventListener());
        tv_current_spendings = findViewById(R.id.target_current_spendings);
        tv_current_balance = findViewById(R.id.target_current_balance);
        tv_initial_balance = findViewById(R.id.target_initial_balance);
        tv_target_spendings = findViewById(R.id.target_target_spendings);
    }

    private View.OnClickListener changeTargetEventListener() {
        return v -> {
            if (target != null) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TargetActivity.this);
                alert.setTitle(getResources().getString(R.string.change_target));
                final TextInputEditText input = new TextInputEditText(TargetActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setPositiveButton(getResources().getString(R.string.ok), (dialog, whichButton) -> {
                    if (input.getText() != null && !input.getText().toString().isEmpty()) {
                        float percent = Float.parseFloat(input.getText().toString());
                        if (percent > 0 && percent < 100) {
                            target.changeTarget(percent);
                            updateTarget();
                        } else {
                            displayChangeTargetError();
                        }
                    } else {
                        displayChangeTargetError();
                    }

                });
                alert.setNegativeButton(getResources().getString(R.string.cancel), null);
                alert.show();
            }
        };
    }

    private void displayChangeTargetError() {
        new Handler().postDelayed(() -> {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout,
                    getResources().getString(R.string.target_change_target_error),
                    BaseTransientBottomBar.LENGTH_LONG).show();
        }, 0);
    }

    private void setTarget() {
        Date currentDate = Calendar.getInstance().getTime();

        transactionService = new TransactionService();
        transactionService.updateTransactionsUI(getAllTransactionsCallback(), DateDisplayType.MONTH_YEAR, currentDate);

        balanceService = new BalanceService();
        balanceService.updateBalancesUI(getAllBalancesCallback());

        target = new Target(getApplicationContext(), transactionList, balanceList);
        updateTarget();
    }

    private void updateTarget() {
        target.update();
        displayText();
        setChart();
    }

    private void displayText() {
        DecimalFormat df = new DecimalFormat("#.##");
        String initial_balance_amount = df.format(target.getInitialBalance());
        String target_spendings_amount = df.format(target.getTargetSpendings());
        String target_spendins_percent = df.format(Target.TARGET_SPENDINGS) + "%";
        String current_spendings_amount = df.format(target.getSpendings());
        String current_spendings_percent = df.format(target.getSpendingsPercent()) + "%";

        String inital_balance = getResources().getString(R.string.target_initial_balance, target.getInitialBalance());
        String current_balance = getResources().getString(R.string.target_current_balance, target.getCurrentBalance());
        String target_spendings = getResources().getString(R.string.target_target_spendings,
                target_spendings_amount, initial_balance_amount, target_spendins_percent);
        String current_spendings = getResources().getString(R.string.target_current_spendings,
                current_spendings_amount, current_spendings_percent, target_spendings_amount, target_spendins_percent);

        tv_initial_balance.setText(inital_balance);
        tv_current_balance.setText(current_balance);
        tv_target_spendings.setText(target_spendings);
        tv_current_spendings.setText(current_spendings);
    }

    private Callback<List<Transaction>> getAllTransactionsCallback() {
        return result -> {
            if (result != null) {
                transactionList.clear();
                transactionList.addAll(result);
                updateTarget();
            }
        };
    }

    private Callback<List<Balance>> getAllBalancesCallback() {
        return result -> {
            if (result != null) {
                balanceList.clear();
                balanceList.addAll(result);
                updateTarget();
            }
        };
    }

    private void setChart() {
        Fragment fragment = new TargetChartFragment(target.buildMap());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.target_frame_chart_actual, fragment)
                .commitAllowingStateLoss();
    }


}