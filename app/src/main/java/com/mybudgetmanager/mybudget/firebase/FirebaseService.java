package com.mybudgetmanager.mybudget.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mybudgetmanager.mybudget.main.SplashActivity;


public class FirebaseService<T extends FirebaseObject> {
    public static final String ATTRIBUTE_USER = "user";

    private final DatabaseReference database;
    private static FirebaseService firebaseService;

    public DatabaseReference getDatabase() {
        return database;
    }

    private FirebaseService() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseService getInstance() {
        if (firebaseService == null) {
            synchronized (FirebaseService.class) {
                if (firebaseService == null) {
                    firebaseService = new FirebaseService();
                }
            }
        }
        return firebaseService;
    }

    public Query getQuery(Table table) {
        return database
                .child(table.toString())
                .orderByChild(ATTRIBUTE_USER)
                .equalTo(SplashActivity.KEY);
    }

}
