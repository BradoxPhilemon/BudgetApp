package com.mybudgetmanager.mybudget.firebase.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.firebase.DateDisplayType;
import com.mybudgetmanager.mybudget.firebase.FirebaseService;
import com.mybudgetmanager.mybudget.firebase.Table;
import com.mybudgetmanager.mybudget.main.SplashActivity;
import com.mybudgetmanager.mybudget.model.Transaction;
import com.mybudgetmanager.mybudget.util.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class TransactionService {

    private FirebaseService firebaseService;

    public TransactionService() {
        firebaseService = FirebaseService.getInstance();
    }


    public void upsert(Transaction transaction) {
        if (transaction == null) {
            return;
        }

        if (transaction.getId() == null || transaction.getId().trim().isEmpty()) {
            String id = firebaseService.getDatabase().push().getKey();
            transaction.setId(id);
        }

        transaction.setUser(SplashActivity.KEY);

        firebaseService
                .getDatabase()
                .child(Table.TRANSACTIONS.toString())
                .child(transaction.getId())
                .setValue(transaction);

    }

    public void delete(Transaction transaction) {
        if (transaction == null || transaction.getId() == null || transaction.getId().trim().isEmpty()) {
            return;
        }

        firebaseService
                .getDatabase()
                .child(Table.TRANSACTIONS.toString())
                .child(transaction.getId())
                .removeValue();
    }

    public void updateTransactionsUI(final Callback<List<Transaction>> callback, DateDisplayType type, Date filter) {
        Query query = firebaseService.getQuery(Table.TRANSACTIONS);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Transaction> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Transaction transaction = data.getValue(Transaction.class);
                    if (transaction != null) {
                        if (DateConverter.filter(type, transaction.getDate(), filter)) {
                            list.add(transaction);
                        }
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
