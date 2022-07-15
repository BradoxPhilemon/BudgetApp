package com.mybudgetmanager.mybudget.firebase;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class FirebaseObject implements Parcelable {

    protected String id;
    protected String user;


    public FirebaseObject(String id, String user) {
        this.id = id;
        this.user = user;
    }

    public FirebaseObject() {

    }


    protected FirebaseObject(Parcel in) {
        id = in.readString();
        user = in.readString();
    }

    public static final Creator<FirebaseObject> CREATOR = new Creator<FirebaseObject>() {
        @Override
        public FirebaseObject createFromParcel(Parcel in) {
            return new FirebaseObject(in);
        }

        @Override
        public FirebaseObject[] newArray(int size) {
            return new FirebaseObject[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
    }
}
