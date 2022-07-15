package com.mybudgetmanager.mybudget.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.model.Balance;

import java.util.List;



public class BalanceAdapter extends ArrayAdapter<Balance> {

    private Context context;
    private int resource;
    private List<Balance> balanceList;
    private LayoutInflater layoutInflater;

    public BalanceAdapter(@NonNull Context context,
                           int resource,
                           @NonNull List<Balance> balanceList,
                           LayoutInflater layoutInflater) {
        super(context, resource, balanceList);
        this.context = context;
        this.resource = resource;
        this.balanceList = balanceList;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(resource, parent, false);

        if (balanceList.size() > 0) {
            if (position < balanceList.size()) {
                Balance balance = balanceList.get(position);
                if (balance != null) {
                    buildBalance(view, balance);
                }
            }
        }
        return view;
    }

    private void buildBalance(View view, Balance balance) {
        TextView tv_name = view.findViewById(R.id.balances_row_name);
        setText(tv_name, balance.getName());
        TextView tv_available_amount = view.findViewById(R.id.balances_row_available_amount);
        setText(tv_available_amount, String.format("%.2f", balance.getAvailable_amount()));
    }

    private void setText(TextView tv, String text) {
        if (text != null && !text.isEmpty()) {
            tv.setText(text);
        }
    }



}
