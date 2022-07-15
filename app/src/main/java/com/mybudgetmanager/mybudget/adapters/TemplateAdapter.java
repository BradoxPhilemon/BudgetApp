package com.mybudgetmanager.mybudget.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.firebase.services.TemplateService;
import com.mybudgetmanager.mybudget.main.AddTransactionActivity;
import com.mybudgetmanager.mybudget.model.Transaction;
import com.mybudgetmanager.mybudget.model.TransactionType;
import com.mybudgetmanager.mybudget.settings.TemplatesActivity;
import com.mybudgetmanager.mybudget.util.CustomDialog;

import java.util.List;



public class TemplateAdapter extends ArrayAdapter<Transaction> {

    private final Context context;
    private final int resource;
    private final List<Transaction> templates;
    private final LayoutInflater layoutInflater;

    public TemplateAdapter(@NonNull Context context,
                           int resource,
                           List<Transaction> templates,
                           LayoutInflater layoutInflater) {
        super(context, resource, templates);
        this.context = context;
        this.resource = resource;
        this.templates = templates;
        this.layoutInflater = layoutInflater;
    }


    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(resource, parent, false);

        if (templates.size() > 0) {
            if (position < templates.size()) {
                Transaction template = templates.get(position);
                if (template != null) {
                    buildTemplate(view, template);
                }
            }
        }

        view.setOnClickListener(useTemplateEventListener(position));
        view.setOnLongClickListener(deleteTemplateListener(position));

        return view;
    }

    private View.OnLongClickListener deleteTemplateListener(int position) {
        return v -> {

            CustomDialog.show(context,
                    R.string.dialog_delete_header,
                    R.string.dialog_delete_template,
                    (dialog1, which) -> {
                        TemplateService templateService = new TemplateService();
                        templateService.delete(templates.get(position));
                    });

            return false;
        };
    }

    private View.OnClickListener useTemplateEventListener(int position) {
        return v -> {
            Intent intent = new Intent(context, AddTransactionActivity.class);
            intent.putExtra(TemplatesActivity.USE_TEMPLATE, templates.get(position));
            context.startActivity(intent);
        };
    }

    private void buildTemplate(View view, Transaction template) {
        buildCategory(view, template);
        buildAmount(view, template);
        buildBalances(view, template);
    }

    private void buildCategory(View view, Transaction template) {
        RadioButton rb_category_type = view.findViewById(R.id.row_item_template_category_type);
        rb_category_type.setText(template.getCategory().getType().toString());

        TextView tv_category_name = view.findViewById(R.id.row_item_template_category_name);
        view.setBackgroundColor(context.getResources().getColor(template.getCategory().getColor()));
        tv_category_name.setText(template.getCategory().getName());
    }

    private void buildAmount(View view, Transaction template) {
        TextView tv_amount = view.findViewById(R.id.row_item_template_amount);
        tv_amount.setText(String.valueOf(template.getAmount()));
    }

    private void buildBalances(View view, Transaction template) {
        TextView tv_balance_from = view.findViewById(R.id.row_item_template_balance_from);
        TextView tv_balance_to = view.findViewById(R.id.row_item_template_balance_to);

        if (template.getCategory().getType().equals(TransactionType.EXPENSE)) {
            tv_balance_from
                    .setText(context
                            .getResources()
                            .getString(R.string.row_item_transaction_expand_balance_from,
                                    template.getBalance_from().getName()));
        } else if (template.getCategory().getType().equals(TransactionType.INCOME)) {
            tv_balance_to
                    .setText(context
                            .getResources()
                            .getString(R.string.row_item_transaction_expand_balance_to,
                                    template.getBalance_to().getName()));
        } else {
            tv_balance_from
                    .setText(context
                            .getResources()
                            .getString(R.string.row_item_transaction_expand_balance_from,
                                    template.getBalance_from().getName()));
            tv_balance_to
                    .setText(context
                            .getResources()
                            .getString(R.string.row_item_transaction_expand_balance_to,
                                    template.getBalance_to().getName()));
        }
    }


}
