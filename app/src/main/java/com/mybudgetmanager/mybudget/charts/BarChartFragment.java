package com.mybudgetmanager.mybudget.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.model.Transaction;
import com.mybudgetmanager.mybudget.model.TransactionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class BarChartFragment extends ChartFragment {

    private BarChart barChart;
    private HashMap<String, Float> map = new HashMap<>();
    private ArrayList<BarEntry> barEntries = new ArrayList<>();

    private ArrayList<Integer> colors = new ArrayList<>();

    public BarChartFragment() {
    }

    public BarChartFragment(List<Transaction> transactionList) {
        super(transactionList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        if (this.transactionList != null && !this.transactionList.isEmpty()) {
            buildBarChart(view);
        }

        return view;
    }

    private void buildBarChart(View view) {
        barChart = view.findViewById(R.id.main_fragment_chart_bar);
        chartSettings();

        this.map = buildMap();
        this.barEntries = buildEntries();

        ArrayList<LegendEntry> legendEntries = buildLegend();
        barChart.getLegend().setCustom(legendEntries);

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColors(this.colors);
        barDataSet.setValueTextSize(14.0f);
        barDataSet.setValueTextColor(getTextColor());

        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.invalidate();
    }

    private void chartSettings() {
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setTextColor(getTextColor());
        barChart.getXAxis().setTextColor(getTextColor());
        barChart.getXAxis().setGridColor(getTextColor());
        barChart.getAxisLeft().setTextColor(getTextColor());
        barChart.getAxisLeft().setGridColor(getTextColor());
        barChart.getAxisRight().setTextColor(getTextColor());
        barChart.getAxisRight().setGridColor(getTextColor());
    }

    private ArrayList<BarEntry> buildEntries() {
        if (this.transactionList == null || this.transactionList.isEmpty()) {
            return new ArrayList<>();
        }

        float x = 5.0f;
        for (String key : this.map.keySet()) {
            BarEntry entry = new BarEntry(x, this.map.get(key));
            this.barEntries.add(entry);
            x += 1.0f;
        }

        return this.barEntries;
    }

    private HashMap<String, Float> buildMap() {
        map.put(getResources().getString(R.string.expense), 0.0f);
        map.put(getResources().getString(R.string.income), 0.0f);
        map.put(getResources().getString(R.string.transfer), 0.0f);

        String key;

        if (transactionList != null && !transactionList.isEmpty()) {
            for (Transaction transaction : transactionList) {
                if (transaction.getCategory().getType().equals(TransactionType.EXPENSE)) {
                    key = getResources().getString(R.string.expense);
                } else if (transaction.getCategory().getType().equals(TransactionType.INCOME)) {
                    key = getResources().getString(R.string.income);
                } else {
                    key = getResources().getString(R.string.transfer);
                }

                float old_value = map.get(key);
                float new_value = old_value + (float) transaction.getAmount();
                map.put(key, new_value);
            }
        }

        return this.map;
    }


    private ArrayList<LegendEntry> buildLegend() {
        if (this.map.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (String key : this.map.keySet()) {
            int color = R.color.rally_white;

            if (key.equals(getResources().getString(R.string.expense))) {
                color = R.color.rally_red;
            } else if (key.equals(getResources().getString(R.string.income))) {
                color = R.color.rally_dark_green;
            } else if (key.equals(getResources().getString(R.string.transfer))) {
                color = R.color.color_26;
            }
            LegendEntry entry = buildLegendEntry(key, color);
            entries.add(entry);
        }
        return entries;
    }

    private LegendEntry buildLegendEntry(String name, int color) {
        LegendEntry entry = new LegendEntry();
        entry.label = name;
        entry.formColor = getResources().getColor(color);
        colors.add(entry.formColor);
        return entry;
    }
}
