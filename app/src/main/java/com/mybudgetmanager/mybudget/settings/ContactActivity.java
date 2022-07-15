package com.mybudgetmanager.mybudget.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mybudgetmanager.mybudget.R;


public class ContactActivity extends AppCompatActivity {

    private static final String GITHUB = "https://github.com/samwelnyandoro";
    private static final String LINKEDIN = "https://www.linkedin.com/in/samwel-nyandoro-4aba9b190/";
    private static final String MAIL = "samwelnyandoro1@gmail.com";

    private ImageButton ib_mail;
    private ImageButton ib_github;
    private ImageButton ib_linkedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initComponents();
    }

    private void initComponents() {
        ib_mail = findViewById(R.id.contact_mail);
        ib_github = findViewById(R.id.contact_github);
        ib_linkedIn = findViewById(R.id.contact_linkedin);

        ib_mail.setOnClickListener(mailEventListener());
        ib_github.setOnClickListener(githubEventListener());
        ib_linkedIn.setOnClickListener(linkedInEventListener());
    }

    private View.OnClickListener mailEventListener() {
        return v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{MAIL});
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(),
                        "There are no email clients installed.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

    private View.OnClickListener githubEventListener() {
        return v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB));
            startActivity(intent);
        };
    }

    private View.OnClickListener linkedInEventListener() {
        return v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LINKEDIN));
            startActivity(intent);
        };
    }
}