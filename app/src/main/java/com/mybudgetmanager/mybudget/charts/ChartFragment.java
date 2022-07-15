package com.mybudgetmanager.mybudget.charts;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.model.Transaction;
import com.mybudgetmanager.mybudget.settings.theme.ThemeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public abstract class ChartFragment extends Fragment {

    public static final String TRANSACTIONS = "transactions";
    protected List<Transaction> transactionList;

    protected ChartFragment() {
        transactionList = new ArrayList<>();
    }

    protected ChartFragment(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.transactionList = getArguments().getParcelableArrayList(TRANSACTIONS);
        }
    }

    protected int getTextColor() {
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        boolean isNightTheme = ThemeManager.getTheme(context);
        if (isNightTheme) {
            return getResources().getColor(R.color.rally_white);
        } else {
            return getResources().getColor(R.color.rally_dark_grey);
        }
    }


    protected List<Integer> getColors() {
        int spendingsColor = getResources().getColor(R.color.rally_dark_red);
        int savingsColor = getResources().getColor(R.color.rally_dark_green);
        List<Integer> colors = new ArrayList<>();
        colors.add(spendingsColor);
        colors.add(savingsColor);
        return colors;
    }
}


