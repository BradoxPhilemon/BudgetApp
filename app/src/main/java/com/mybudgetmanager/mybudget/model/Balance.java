package com.mybudgetmanager.mybudget.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mybudgetmanager.mybudget.firebase.FirebaseObject;

import java.io.Serializable;
import java.text.DecimalFormat;



public class Balance extends FirebaseObject implements Parcelable {

    private String name;
    private double available_amount;

    public Balance() {
        super(null, null);
    }

    protected Balance(Parcel in) {
        id = in.readString();
        user = in.readString();
        name = in.readString();
        available_amount = in.readDouble();
    }

    public static final Creator<Balance> CREATOR = new Creator<Balance>() {
        @Override
        public Balance createFromParcel(Parcel in) {
            return new Balance(in);
        }

        @Override
        public Balance[] newArray(int size) {
            return new Balance[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAvailable_amount() {
        return available_amount;
    }

    public void setAvailable_amount(double available_amount) {
        this.available_amount = available_amount;
    }

    public boolean operation(TransactionType type, double amount) {
        if (type.equals(TransactionType.EXPENSE)) {
            return withdraw(amount);
        } else {
            return deposit(amount);
        }
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        } else {
            this.available_amount += amount;
            return true;
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount >= this.available_amount) {
            return false;
        } else {
            this.available_amount -= amount;
            return true;
        }
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return this.name + " - " +
                df.format(this.available_amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(name);
        dest.writeDouble(available_amount);
    }
}
