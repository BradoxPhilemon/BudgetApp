package com.mybudgetmanager.mybudget.firebase.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.firebase.FirebaseService;
import com.mybudgetmanager.mybudget.firebase.Table;
import com.mybudgetmanager.mybudget.main.SplashActivity;
import com.mybudgetmanager.mybudget.model.Account;


public class AccountService {

    public static final String ATTRIBUTE_EMAIL = "email";
    private FirebaseService firebaseService;

    public AccountService() {
        firebaseService = FirebaseService.getInstance();
    }

    public Account upsert(Account account) {
        if (account == null) {
            return null;
        }

        if (account.getId() == null || account.getId().trim().isEmpty()) {
            String id = firebaseService.getDatabase().push().getKey();
            account.setId(id);
        }

        firebaseService
                .getDatabase()
                .child(Table.USERS.toString())
                .child(account.getId())
                .setValue(account);

        Log.e("AccService", account.toString());
        return account;
    }

    public void getAccount(final Callback<Account> callback, String email) {
        Query query =
                firebaseService
                        .getDatabase()
                        .child(Table.USERS.toString())
                        .orderByChild(ATTRIBUTE_EMAIL)
                        .equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    callback.updateUI(null);
                } else {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        Account account = item.getValue(Account.class);
                        if (account != null) {
                            callback.updateUI(account);
                        } else {
                            Log.e("FirebaseService", "ACCOUNT IS NULL!");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("cancel", error.getMessage());
            }
        });
    }

    public void getAccount(final Callback<Account> callback) {
        Query query = firebaseService
                .getDatabase()
                .child(Table.USERS.toString())
                .child(SplashActivity.KEY);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    callback.updateUI(null);
                } else {
                    Account account = snapshot.getValue(Account.class);
                    if (account != null) {
                        callback.updateUI(account);
                    } else {
                        Log.e("FirebaseService", "ACCOUNT IS NULL!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("cancel", error.getMessage());
            }
        });
    }
}
