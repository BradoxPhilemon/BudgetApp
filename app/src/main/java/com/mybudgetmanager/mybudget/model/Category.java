package com.mybudgetmanager.mybudget.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mybudgetmanager.mybudget.firebase.FirebaseObject;

import java.io.Serializable;
import java.util.Objects;



public class Category extends FirebaseObject implements Parcelable {

    private TransactionType type;
    private int color;
    private String name;

    public Category() {
        super(null, null);
    }


    protected Category(Parcel in) {
        id = in.readString();
        user = in.readString();
        type = TransactionType.fromString(in.readString());
        color = in.readInt();
        name = in.readString();
    }


    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(type.toString());
        dest.writeInt(color);
        dest.writeString(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return color == category.color &&
                type.equals(category.type) &&
                Objects.equals(name, category.name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(type, color, name);
    }
}
