package com.mybudgetmanager.mybudget.settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.adapters.TemplateAdapter;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.firebase.services.TemplateService;
import com.mybudgetmanager.mybudget.firebase.services.TransactionService;
import com.mybudgetmanager.mybudget.main.AddTransactionActivity;
import com.mybudgetmanager.mybudget.model.Transaction;

import java.util.ArrayList;
import java.util.List;



public class TemplatesActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CREATE_TEMPLATE = 201;
    public static final int REQUEST_CODE_USE_TEMPLATE = 202;
    public static final String USE_TEMPLATE = "use_template";
    public static final String NEW_TEMPLATE = "new_template";
    private ImageButton ib_back;
    private ImageButton ib_add;
    private ListView lv_templates;

    private List<Transaction> templates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        initComponents();
        getTemplates();
    }

    private void initComponents() {
        ib_back = findViewById(R.id.templates_back);
        ib_add = findViewById(R.id.templates_add);
        lv_templates = findViewById(R.id.templates_list);

        ib_back.setOnClickListener(v -> finish());
        ib_add.setOnClickListener(addTemplateEventListener());

        setAdapter();
    }

    private void getTemplates() {
        TemplateService templateService = new TemplateService();
        templateService.updateTemplatesUI(getTemplatesCallback());
    }

    private Callback<List<Transaction>> getTemplatesCallback() {
        return result -> {
            if (result != null) {
                templates.clear();
                templates.addAll(result);
                notifyAdapter();
            }
        };
    }

    private void setAdapter() {
        TemplateAdapter adapter = new TemplateAdapter(
                TemplatesActivity.this,
                R.layout.row_item_template,
                templates,
                getLayoutInflater());
        lv_templates.setAdapter(adapter);
    }

    private void notifyAdapter() {
        TemplateAdapter adapter = (TemplateAdapter) lv_templates.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener addTemplateEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), AddTransactionActivity.class);
            intent.putExtra(NEW_TEMPLATE, true);
            startActivityForResult(intent, REQUEST_CODE_CREATE_TEMPLATE);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Transaction template = data.getParcelableExtra(AddTransactionActivity.TRANSACTION);

                if (requestCode == REQUEST_CODE_CREATE_TEMPLATE && data != null) {
                    TemplateService templateService = new TemplateService();
                    templateService.upsert(template);
                    setAdapter();
                } else if (requestCode == REQUEST_CODE_USE_TEMPLATE && data != null) {
                    Transaction transaction = data.getParcelableExtra(AddTransactionActivity.TRANSACTION);
                    TransactionService transactionService = new TransactionService();
                    transactionService.upsert(transaction);
                }
            }
        }
    }




}