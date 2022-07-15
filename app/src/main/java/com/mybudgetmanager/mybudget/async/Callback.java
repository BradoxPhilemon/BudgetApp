package com.mybudgetmanager.mybudget.async;

public interface Callback<R> {
    void updateUI(R result);
}
