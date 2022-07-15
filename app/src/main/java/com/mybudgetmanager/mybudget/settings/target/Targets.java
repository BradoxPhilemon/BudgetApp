package com.mybudgetmanager.mybudget.settings.target;

public enum Targets {

    SPENDINGS("spendings"),
    SAVINGS("savings");

    String name;

    Targets(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
