package com.mybudgetmanager.mybudget.settings.balances;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.adapters.BalanceAdapter;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.firebase.services.BalanceService;
import com.mybudgetmanager.mybudget.model.Balance;
import com.mybudgetmanager.mybudget.model.Currency;
import com.mybudgetmanager.mybudget.util.CustomDialog;

import java.util.ArrayList;
import java.util.List;



public class BalancesActivity extends AppCompatActivity {


    public static final int REQUEST_CODE_ADD_BALANCE = 201;
    private TextView tv_total_text;
    private ImageButton iv_balance_add;
    private ListView lv_balances;

    private Currency currency;
    private List<Balance> balanceList = new ArrayList<>();
    private BalanceService balanceService = new BalanceService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);

        initComponents();
        getBalancesFromFirebase();
    }

    private void getBalancesFromFirebase() {
        balanceService.updateBalancesUI(updateBalancesCallback());
    }

    private Callback<List<Balance>> updateBalancesCallback() {
        return result -> {
            if (result != null) {
                balanceList.clear();
                balanceList.addAll(result);
                notifyAdapter();
            }
        };
    }


    private void initComponents() {
        tv_total_text = findViewById(R.id.balances_total);
        iv_balance_add = findViewById(R.id.balances_add);

        lv_balances = findViewById(R.id.balances_list);
        setAdapter();
        lv_balances.setOnItemLongClickListener(deleteBalanceEventListener());

        iv_balance_add.setOnClickListener(addBalanceEventListener());
    }

    private AdapterView.OnItemLongClickListener deleteBalanceEventListener() {
        return (parent, view, position, id) -> {

            CustomDialog.show(BalancesActivity.this,
                    R.string.dialog_delete_header,
                    R.string.dialog_delete_balance,
                    (dialog1, which) -> {
                        balanceService.delete(balanceList.get(position));
                    });

            notifyAdapter();
            return false;

        };
    }

    private View.OnClickListener addBalanceEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), AddBalanceActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_BALANCE);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_BALANCE & data != null) {
            Balance balance = data.getParcelableExtra(AddBalanceActivity.NEW_BALANCE);
            balanceService.upsert(balance);
        }
    }

    private void setAdapter() {
        BalanceAdapter adapter = new BalanceAdapter(getApplicationContext(),
                R.layout.row_item_balance,
                balanceList,
                getLayoutInflater());
        lv_balances.setAdapter(adapter);
    }

    private void notifyAdapter() {
        BalanceAdapter adapter = (BalanceAdapter) lv_balances.getAdapter();
        adapter.notifyDataSetChanged();
        setTotal(tv_total_text, getTotalAmount());
    }

    private double getTotalAmount() {
        double total = 0.0;
        for (Balance balance : balanceList) {
            total += balance.getAvailable_amount();
        }
        return total;
    }

    private void setTotal(TextView tv_total_text, double totalAmount) {
        tv_total_text.setText(getResources().getString(R.string.balances_total, totalAmount));
    }
}