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
import com.mybudgetmanager.mybudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;



public class TemplateService {

    private FirebaseService firebaseService;

    public TemplateService() {
        firebaseService = FirebaseService.getInstance();
    }


    public void upsert(Transaction template) {
        if (template == null) {
            return;
        }

        Log.e("upsertTemplate", template.toString());

        if (template.getId() == null || template.getId().trim().isEmpty()) {
            String id = firebaseService.getDatabase().push().getKey();
            template.setId(id);
        }

        template.setUser(SplashActivity.KEY);

        firebaseService
                .getDatabase()
                .child(Table.TEMPLATES.toString())
                .child(template.getId())
                .setValue(template);

    }


    public void delete(Transaction template) {
        if (template == null || template.getId() == null || template.getId().trim().isEmpty()) {
            return;
        }

        firebaseService
                .getDatabase()
                .child(Table.TEMPLATES.toString())
                .child(template.getId())
                .removeValue();
    }

    public void updateTemplatesUI(final Callback<List<Transaction>> callback) {
        Query query = firebaseService.getQuery(Table.TEMPLATES);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Transaction> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Transaction template = data.getValue(Transaction.class);
                    if (template != null) {
                        list.add(template);
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
