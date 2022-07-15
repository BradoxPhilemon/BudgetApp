package com.mybudgetmanager.mybudget.charts;

import androidx.fragment.app.Fragment;

import com.mybudgetmanager.mybudget.model.Transaction;

import java.util.ArrayList;



public enum ChartType {
    PIE_CHART,
    BAR_CHART,
    LINE_CHART;

    public static Fragment getFragment(ChartType type, ArrayList<Transaction> transactions) {
        switch (type) {
            case BAR_CHART:
                return new BarChartFragment(transactions);
            case LINE_CHART:
                return new LineChartFragment(transactions);
            default:
                return new PieChartFragment(transactions);
        }
    }
}
