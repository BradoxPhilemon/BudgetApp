package com.mybudgetmanager.mybudget.model;

import androidx.annotation.NonNull;

public enum TransactionType {

    EXPENSE("EXPENSE"),
    INCOME("INCOME"),
    TRANSFER("TRANSFER");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static TransactionType fromString(String type) {
        if (type.equals(EXPENSE.toString())) {
            return EXPENSE;
        } else if (type.equals(INCOME.toString())) {
            return INCOME;
        } else {
            return TRANSFER;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getType();
    }
}
