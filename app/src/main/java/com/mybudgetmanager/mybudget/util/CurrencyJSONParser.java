package com.mybudgetmanager.mybudget.util;

import com.mybudgetmanager.mybudget.model.Currency;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class CurrencyJSONParser {

    private static final String RESULTS = "results";
    private static final String CURRENCY_CODE = "id";
    private static final String CURRENCY_SYMBOL = "currencySymbol";
    private static final String CURRENCY_NAME = "currencyName";

    public static List<Currency> getCurrencies(String json) {
        List<Currency> list = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONObject results = jsonObject.getJSONObject(RESULTS);

            Iterator<String> keys = results.keys();

            int i = 0;
            while (keys.hasNext()) {
                String id = keys.next();
                if (results.get(id) instanceof JSONObject) {
                    JSONObject object = (JSONObject) results.get(id);
                    Currency currency = getCurrency(i, object);
                    list.add(currency);
                    i++;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Currency getCurrency(int position, JSONObject object) throws JSONException {
        String code = null;
        String symbol = null;
        String name = null;

        if (object.has(CURRENCY_CODE)) {
            code = object.getString(CURRENCY_CODE);
        }
        if (object.has(CURRENCY_SYMBOL)) {
            symbol = object.getString(CURRENCY_SYMBOL);
        }
        if (object.has(CURRENCY_NAME)) {
            name = object.getString(CURRENCY_NAME);
        }
        return new Currency(position, code, symbol, name);
    }
}
