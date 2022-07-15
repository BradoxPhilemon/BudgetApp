package com.mybudgetmanager.mybudget.model;


import androidx.annotation.NonNull;

import java.io.Serializable;

public class Currency implements Serializable, Cloneable {

    private int position;
    private String code;
    private String symbol;
    private String name;


    public Currency() {

    }

    public Currency(int position, String code, String symbol, String name) {
        this.position = position;
        this.code = code;
        this.symbol = symbol;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.code + " (" + this.name + ")";
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Currency clone = new Currency();
        clone.code = this.code;
        clone.name = this.name;
        clone.symbol = this.symbol;
        clone.position = this.position;
        return clone;
    }
}
