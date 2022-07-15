package com.mybudgetmanager.mybudget.authentication;

import android.content.Context;
import android.content.Intent;



import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mybudgetmanager.mybudget.model.Account;

public class GoogleAuthentication {

    private final String WEB_CLIENT_ID = "774225059164-6gdh4ga1ssa5bvuv0rnp8g6neoj2n777.apps.googleusercontent.com";

    private final Context context;
    private final GoogleSignInClient mGoogleSignInClient;

    public GoogleAuthentication(Context context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public void handleSignInResult(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);

            Account.googleAuthentication(context, account);

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
