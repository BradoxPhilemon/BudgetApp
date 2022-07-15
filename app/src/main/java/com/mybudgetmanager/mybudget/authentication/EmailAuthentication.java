package com.mybudgetmanager.mybudget.authentication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mybudgetmanager.mybudget.model.Account;

public class EmailAuthentication {

    private final Context context;
    private final FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Account account;

    public EmailAuthentication(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = null;
    }

    public void registerAccount(TextInputEditText name, TextInputEditText email, TextInputEditText password) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),
                password.getText().toString().trim())
                .addOnCompleteListener(completeRegisterEvent(name.getText().toString()));
    }

    private OnCompleteListener<AuthResult> completeRegisterEvent(String name) {
        return task -> {
            if (task.isSuccessful()) {
                createUserProfile(name);
            } else {
                Toast.makeText(context,
                        task.getException().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void createUserProfile(String name) {
        currentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(task -> {
                    Account.createAccount(context, currentUser.getDisplayName(), currentUser.getEmail());
                });
    }

    public void loginAccount(@NonNull TextInputEditText tiet_email,
                             @NonNull TextInputEditText tiet_password) {

        String email = tiet_email.getText().toString().trim();
        String password = tiet_password.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(completeLoginEvent(email))
                .addOnFailureListener(e -> Toast.makeText(context,
                        e.getMessage(),
                        Toast.LENGTH_LONG).show());
    }

    private OnCompleteListener<AuthResult> completeLoginEvent(String email) {
        return task -> {
            if (task.isSuccessful()) {
                Account.authenticate(context, email);
            } else {
                Log.e("EmailAuthentication", "Task not successful.");
            }
        };
    }

    public void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> Toast.makeText(context,
                        "Email sent.",
                        Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(context,
                        "Email failed.",
                        Toast.LENGTH_LONG).show());
    }
}
