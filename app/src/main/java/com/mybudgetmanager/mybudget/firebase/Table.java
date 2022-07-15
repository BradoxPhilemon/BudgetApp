package com.mybudgetmanager.mybudget.firebase;

public enum Table {

    USERS ("users"),
    CATEGORIES ("categories"),
    BALANCES ("balances"),
    TRANSACTIONS ("transactions"),
    TEMPLATES ("templates");

    private final String table;

    Table(String table) {
        this.table = table;
    }
}
