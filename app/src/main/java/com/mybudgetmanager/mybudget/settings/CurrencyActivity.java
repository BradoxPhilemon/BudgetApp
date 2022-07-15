package com.mybudgetmanager.mybudget.settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;


import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.model.Account;
import com.mybudgetmanager.mybudget.model.Currency;
import com.mybudgetmanager.mybudget.network.CurrenciesManager;
import com.mybudgetmanager.mybudget.util.CurrencyJSONParser;

import java.util.ArrayList;
import java.util.List;




public class CurrencyActivity extends AppCompatActivity {

    private ImageButton ib_back;
    private TextInputEditText tiet_search;
    private ListView lv_currencies;

    private List<Currency> currencyList = new ArrayList<>();
    private List<Currency> filteredList = new ArrayList<>();
    private Currency currency = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        initComponents();
        CurrenciesManager.getCurrenciesFromURL(currenciesCallback());
    }

    private void initComponents() {
        ib_back = findViewById(R.id.currency_back);
        ib_back.setOnClickListener(v -> finish());

        tiet_search = findViewById(R.id.currency_search);
        tiet_search.addTextChangedListener(textChangedEventListener());

        lv_currencies = findViewById(R.id.currency_list);
        lv_currencies.setOnItemClickListener(setCurrency());

        lv_currencies.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv_currencies.setSelector(R.color.rally_dark_green);
    }

    private AdapterView.OnItemClickListener setCurrency() {
        return (parent, view, position, id) -> {
            currency = filteredList.get(position);
            Account.getInstance().setCurrency(currency);
            Account.updateAccount();

            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        };
    }

    private void setAdapter() {
        ArrayAdapter<Currency> adapter = new ArrayAdapter<>
                (getApplicationContext(),
                        R.layout.row_item_currency,
                        filteredList);
        lv_currencies.setAdapter(adapter);
    }

    private void notifyAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lv_currencies.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private Callback<String> currenciesCallback() {
        return result -> {
            if (result != null) {
                currencyList.clear();
                currencyList = CurrencyJSONParser.getCurrencies(result);
                filteredList.clear();
                filteredList.addAll(currencyList);
                setAdapter();
            }
        };
    }


    private TextWatcher textChangedEventListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    filteredList.clear();
                    filteredList.addAll(currencyList);
                } else {
                    filteredList = filter(s);
                }
                notifyAdapter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    public List<Currency> filter(CharSequence filter) {
        filteredList.clear();
        for (Currency currency : currencyList) {
            if (currency.getCode().toLowerCase().contains(filter.toString().toLowerCase())
                    || currency.getName().toLowerCase().contains(filter.toString().toLowerCase())) {
                filteredList.add(currency);
            }
        }
        return filteredList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}