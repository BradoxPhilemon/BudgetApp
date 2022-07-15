package com.mybudgetmanager.mybudget.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.model.Currency;
import com.mybudgetmanager.mybudget.network.CurrenciesManager;
import com.mybudgetmanager.mybudget.util.CurrencyJSONParser;
import com.mybudgetmanager.mybudget.util.InputValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



public class ConverterActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private TextInputEditText tiet_amount_from;
    private Spinner spn_currency_from;
    private TextView tv_amount_to;
    private Spinner spn_currency_to;
    private Button btn_convert;

    private List<Currency> currencyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        initComponents();
        CurrenciesManager.getCurrenciesFromURL(currenciesCallback());
    }


    private void initComponents() {
        btn_back = findViewById(R.id.converter_back);
        btn_back.setOnClickListener(v -> finish());
        tiet_amount_from = findViewById(R.id.converter_amount_from);
        spn_currency_from = findViewById(R.id.converter_currency_from);
        tv_amount_to = findViewById(R.id.converter_amount_to);
        spn_currency_to = findViewById(R.id.converter_currency_to);
        btn_convert = findViewById(R.id.converter_convert);
        btn_convert.setOnClickListener(convertEventListener());
    }

    private View.OnClickListener convertEventListener() {
        return v -> {
            if (InputValidation.amountValidation(getBaseContext(), tiet_amount_from)) {
                double amount_from = Double.parseDouble(tiet_amount_from.getText().toString().trim());
                String from = ((Currency) spn_currency_from.getSelectedItem()).getCode();
                String to = ((Currency) spn_currency_to.getSelectedItem()).getCode();

                CurrenciesManager
                        .getConversionFromURL(from, to, conversionCallback(amount_from, from, to));
            }
        };
    }


    private Callback<String> conversionCallback(double amount_from, String from, String to) {
        return result -> {
            if (result != null) {
                String node = from + "_" + to;
                try {
                    JSONObject object = new JSONObject(result);

                    double rate = object.getDouble(node);
                    double amount_to = rate * amount_from;

                    DecimalFormat df = new DecimalFormat("#.##");
                    tv_amount_to.setText(df.format(amount_to));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Callback<String> currenciesCallback() {
        return result -> {
            currencyList.clear();
            currencyList = CurrencyJSONParser.getCurrencies(result);
            setAdapters();
        };
    }


    private void setAdapters() {
        ArrayAdapter<Currency> adapter = new ArrayAdapter<>
                (getApplicationContext(),
                        R.layout.row_spinner_simple,
                        currencyList);
        spn_currency_from.setAdapter(adapter);
        spn_currency_to.setAdapter(adapter);
    }


}