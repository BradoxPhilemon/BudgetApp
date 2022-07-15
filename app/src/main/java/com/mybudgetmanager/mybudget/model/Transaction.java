package com.mybudgetmanager.mybudget.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mybudgetmanager.mybudget.firebase.FirebaseObject;
import com.mybudgetmanager.mybudget.util.DateConverter;

import java.util.Date;



public class Transaction extends FirebaseObject implements Parcelable, Cloneable {

    protected String details;
    protected Category category;
    protected Balance balance_from;
    protected Balance balance_to;
    protected double amount;
    protected Date date;

    public Transaction() {
        super(null, null);
        this.category = new Category();
        this.balance_from = new Balance();
        this.balance_to = new Balance();
    }

    public Transaction(Transaction transaction) {
        super(transaction.getId(), transaction.getUser());
        this.details = transaction.getDetails();
        this.category = transaction.getCategory();
        this.balance_from = transaction.getBalance_from();
        this.balance_to = transaction.getBalance_to();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Balance getBalance_from() {
        return balance_from;
    }

    public void setBalance_from(Balance balance_from) {
        this.balance_from = balance_from;
    }

    public Balance getBalance_to() {
        return balance_to;
    }

    public void setBalance_to(Balance balance_to) {
        this.balance_to = balance_to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "details='" + details + '\'' +
                ", category=" + category +
                ", balance_from=" + balance_from +
                ", balance_to=" + balance_to +
                ", amount=" + amount +
                ", date=" + date +
                ", id='" + id + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    protected Transaction(Parcel in) {
        this.id = in.readString();
        this.user = in.readString();
        this.details = in.readString();
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.balance_from = in.readParcelable(Balance.class.getClassLoader());
        this.balance_to = in.readParcelable(Balance.class.getClassLoader());
        this.amount = in.readDouble();

        String dateString = in.readString();
        this.date = DateConverter.toDate(dateString);
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(details);
        dest.writeParcelable(category, flags);
        dest.writeParcelable(balance_from, flags);
        dest.writeParcelable(balance_to, flags);
        dest.writeDouble(amount);

        String dateString = DateConverter.toString(date);
        dest.writeString(dateString);
    }

    public static void saveTransaction(Transaction transaction) {
        switch (transaction.getCategory().getType()) {
            case EXPENSE:
                Expense.saveExpense(new Expense(transaction));
                break;
            case INCOME:
                Income.saveIncome(new Income(transaction));
                break;
            case TRANSFER:
                Transfer.saveTransfer(new Transfer(transaction));
                break;
        }
    }

    public static void deleteTransaction(Transaction transaction) {
        switch (transaction.getCategory().getType()) {
            case EXPENSE:
                Expense.deleteExpense(new Expense(transaction));
                break;
            case INCOME:
                Income.deleteIncome(new Income(transaction));
                break;
            case TRANSFER:
                Transfer.deleteTransfer(new Transfer(transaction));
                break;
        }
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        Transaction clone = new Transaction();

        clone.setId(id);
        clone.setUser(user);
        clone.setDetails(details);
        clone.setCategory(category);
        clone.setBalance_from(balance_from);
        clone.setBalance_to(balance_to);
        clone.setDate(date);

        return clone;
    }
}
