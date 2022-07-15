package com.mybudgetmanager.mybudget.settings.target;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.charts.ChartFragment;

import java.util.ArrayList;
import java.util.Map;



public class TargetChartFragment extends ChartFragment {

    private Map<String, Float> actualMap;
    private PieChart pieChart;
    private ArrayList<PieEntry> pieEntries;


    public TargetChartFragment() {
    }

    public TargetChartFragment(Map<String, Float> actualMap) {
        this.actualMap = actualMap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_target_chart, container, false);

        if (this.actualMap != null && !this.actualMap.isEmpty()) {
            buildPieChart(view);
        }

        return view;
    }

    private void buildPieChart(View view) {
        pieChart = view.findViewById(R.id.target_chart_target);
        chartSettings();

        this.pieEntries = buildPieEntries();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(getColors());
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

    private ArrayList<PieEntry> buildPieEntries() {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        if (this.actualMap == null) {
            return new ArrayList<>();
        } else {
            for (String key : this.actualMap.keySet()) {
                if (this.actualMap.get(key) != null) {
                    pieEntries.add(new PieEntry(this.actualMap.get(key), key));
                }
            }
            return pieEntries;
        }
    }
}