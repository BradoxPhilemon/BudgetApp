package com.mybudgetmanager.mybudget.firebase.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.firebase.FirebaseService;
import com.mybudgetmanager.mybudget.firebase.Table;
import com.mybudgetmanager.mybudget.main.SplashActivity;
import com.mybudgetmanager.mybudget.model.Balance;

import java.util.ArrayList;
import java.util.List;



public class BalanceService {

    private FirebaseService firebaseService;

    public BalanceService() {
        firebaseService = FirebaseService.getInstance();
    }


    public void upsert(Balance balance) {
        if (balance == null) {
            return;
        }

        if (balance.getId() == null || balance.getId().trim().isEmpty()) {
            String id = firebaseService.getDatabase().push().getKey();
            balance.setId(id);
        }

        balance.setUser(SplashActivity.KEY);

        firebaseService
                .getDatabase()
                .child(Table.BALANCES.toString())
                .child(balance.getId())
                .setValue(balance);

    }

    public void delete(Balance balance) {
        if (balance == null || balance.getId() == null || balance.getId().trim().isEmpty()) {
            return;
        }

        firebaseService
                .getDatabase()
                .child(Table.BALANCES.toString())
                .child(balance.getId())
                .removeValue();
    }

    public void updateBalancesUI(final Callback<List<Balance>> callback) {
        Query query = firebaseService.getQuery(Table.BALANCES);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Balance> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Balance object = data.getValue(Balance.class);
                    if (object != null) {
                        list.add(object);
                    }
                }
                callback.updateUI(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
