package com.mybudgetmanager.mybudget.charts;

import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.collection.LLRBNode;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.model.Transaction;
import com.mybudgetmanager.mybudget.model.TransactionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PieChartFragment extends ChartFragment {

    private Map<String, Float> categoriesMap;
    private ArrayList<PieEntry> pieEntries;
    private PieChart pieChart;
    private List<Integer> colors = new ArrayList<>();


    public PieChartFragment() {
        super();
    }

    public PieChartFragment(List<Transaction> transactionList) {
        super(transactionList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        if (this.transactionList != null && !this.transactionList.isEmpty()) {
            buildPieChart(view);
        }

        return view;
    }

    private void buildPieChart(View view) {
        pieChart = view.findViewById(R.id.main_fragment_chart_pie);
        chartSettings();

        this.categoriesMap = buildMap();
        this.pieEntries = buildPieEntries();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(this.colors);
        pieDataSet.setValueTextSize(14.0f);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.rally_white));

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void chartSettings() {
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextColor(getTextColor());
    }


    private Map<String, Float> buildMap() {
        if (transactionList == null || transactionList.isEmpty()) {
            return null;
        } else {
            Map<String, Float> source = new HashMap<>();
            for (Transaction transaction : transactionList) {
                if (transaction.getCategory().getType().equals(TransactionType.EXPENSE)) {
                    String key = transaction.getCategory().getName();

                    int color = transaction.getCategory().getColor();


                    if (source.containsKey(key)) {
                        Float currentValue = source.get(key);
                        Float newValue = (currentValue != null ? currentValue : 0.0f)
                                + (float) transaction.getAmount();
                        source.put(key, newValue);
                    } else {
                        source.put(key, (float) transaction.getAmount());
                    }
                }
            }
            this.colors = getCategoriesColors(source, this.transactionList);
            return source;
        }
    }

    private List<Integer> getCategoriesColors(Map<String, Float> source,
                                              List<Transaction> transactions) {
        List<Integer> colorsList = new ArrayList<>();
        if (source != null || !source.isEmpty()) {
            for (String key : source.keySet()) {
                for (int i = 0; i < transactions.size(); i++) {
                    if (key.equals(transactions.get(i).getCategory().getName())) {
                        int id = transactions.get(i).getCategory().getColor();
                        int color = getResources().getColor(id);
                        if (!colorsList.contains(color)) {
                            colorsList.add(color);
                        }
                    }
                }
            }
        }
        return colorsList;
    }

    private ArrayList<PieEntry> buildPieEntries() {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        if (this.categoriesMap == null) {
            return new ArrayList<>();
        } else {
            for (String key : this.categoriesMap.keySet()) {
                pieEntries.add(new PieEntry(this.categoriesMap.get(key), key));
            }
            return pieEntries;
        }
    }
}