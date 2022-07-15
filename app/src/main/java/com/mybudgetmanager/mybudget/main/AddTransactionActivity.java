package com.mybudgetmanager.mybudget.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.firebase.services.BalanceService;
import com.mybudgetmanager.mybudget.firebase.services.CategoryService;
import com.mybudgetmanager.mybudget.model.Balance;
import com.mybudgetmanager.mybudget.model.Category;
import com.mybudgetmanager.mybudget.model.Transaction;
import com.mybudgetmanager.mybudget.model.TransactionType;
import com.mybudgetmanager.mybudget.model.Transfer;
import com.mybudgetmanager.mybudget.settings.TemplatesActivity;
import com.mybudgetmanager.mybudget.util.DateConverter;
import com.mybudgetmanager.mybudget.util.InputValidation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class AddTransactionActivity extends AppCompatActivity {


    public static final String TRANSACTION = "transaction";
    private RadioGroup rg_type;
    private TextInputEditText tiet_details;
    private TextInputEditText tiet_amount;
    private Spinner spn_category;
    private Spinner spn_balances_from;
    private Spinner spn_balances_to;
    private Button btn_date;
    private Button btn_save;


    private Transaction transaction = new Transaction();
    private List<Category> expenseCategories = new ArrayList<>();
    private List<Category> incomeCategories = new ArrayList<>();
    private List<Balance> balances = new ArrayList<>();

    private boolean isTransaction = false;

    int year;
    int month;
    int day;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        initComponents();
        initDefaults();
        retrieveDataFromFirebase();
    }

    private void handleTransaction() {
        intent = getIntent();
        Transaction transaction = intent.getParcelableExtra(TemplatesActivity.USE_TEMPLATE);


        boolean isTemplate = intent.getBooleanExtra(TemplatesActivity.NEW_TEMPLATE, false);
        if (isTemplate) {
            btn_save.setText(R.string.save_template);
        }

        if (transaction != null) {
            buildTransaction(transaction);
            transaction.setId(null);
            this.transaction.setId(null);
            isTransaction = true;
        }

        setCurrentDate();
    }

    private int getCategoryPosition(Category category, List<Category> categories) {
        if (category != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId().equals(category.getId())) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getBalancePosition(Balance balance, List<Balance> balances) {
        if (balance != null) {
            for (int i = 0; i < balances.size(); i++) {
                if (balances.get(i).getId().equals(balance.getId())) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void buildTransaction(Transaction transaction) {

        switch (transaction.getCategory().getType()) {
            case EXPENSE:
                rg_type.check(R.id.add_transaction_type_expense);
                break;
            case INCOME:
                rg_type.check(R.id.add_transaction_type_income);
                break;
            case TRANSFER:
                rg_type.check(R.id.add_transaction_type_transfer);
                break;
        }

        if (transaction.getDetails() != null && !transaction.getDetails().isEmpty()) {
            tiet_details.setText(transaction.getDetails());
        }

        tiet_amount.setText(String.valueOf(transaction.getAmount()));

        if (transaction.getDate() != null) {
            this.transaction.setDate(transaction.getDate());
            btn_date.setText(DateConverter.toString(transaction.getDate()));
        }

        if (transaction.getCategory() != null) {
            if (transaction.getCategory().getType().equals(TransactionType.EXPENSE)) {
                spn_category.setSelection(expenseCategories.indexOf(transaction.getCategory()));
            } else {
                spn_category.setSelection(incomeCategories.indexOf(transaction.getCategory()));
            }
        }

        if (transaction.getCategory().getType().equals(TransactionType.EXPENSE)
                || transaction.getCategory().getType().equals(TransactionType.TRANSFER)) {
            if (transaction.getBalance_from() != null) {
                for (int i = 0; i < balances.size(); i++) {
                    if (transaction.getBalance_from().getId().equals(balances.get(i).getId())) {
                        spn_balances_from.setSelection(i);
                    }
                }
            }
        }

        if (transaction.getCategory().getType().equals(TransactionType.INCOME)
                || transaction.getCategory().getType().equals(TransactionType.TRANSFER)) {
            if (transaction.getBalance_to() != null) {
                for (int i = 0; i < balances.size(); i++) {
                    if (transaction.getBalance_to().getId().equals(balances.get(i).getId())) {
                        spn_balances_to.setSelection(i);
                    }
                }
            }
        }

        try {
            this.transaction = (Transaction) transaction.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void retrieveDataFromFirebase() {
        CategoryService categoryService = new CategoryService();
        categoryService.updateCategoriesUI(updateCategoriesCallback());

        BalanceService balanceService = new BalanceService();
        balanceService.updateBalancesUI(updateBalancesCallback());
    }

    private Callback<List<Category>> updateCategoriesCallback() {
        return result -> {
            if (result != null) {
                for (Category category : result) {
                    if (category != null) {
                        if (category.getType() != null) {
                            if (category.getType().equals(TransactionType.EXPENSE)) {
                                expenseCategories.add(category);
                            } else if (category.getType().equals(TransactionType.INCOME)) {
                                incomeCategories.add(category);
                            }
                        }
                    }
                    setCategoryAdapter();
                }
            }
        };
    }

    private Callback<List<Balance>> updateBalancesCallback() {
        return result -> {
            if (result != null) {
                balances.addAll(result);
                setBalanceAdapter();
            }
            handleTransaction();
        };
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        transaction.setDate(DateConverter.toDate(day, month, year));
        btn_date.setText(DateConverter.format(day, month, year));
    }

    private void initDefaults() {
        setCurrentDate();
        transaction.getCategory().setType(rg_type.getCheckedRadioButtonId()
                == R.id.add_transaction_type_expense
                ? TransactionType.EXPENSE : TransactionType.INCOME);

    }

    private void initComponents() {
        rg_type = findViewById(R.id.add_transaction_type);
        tiet_details = findViewById(R.id.add_transaction_details);
        tiet_amount = findViewById(R.id.add_transaction_amount);
        spn_category = findViewById(R.id.add_transaction_category);
        spn_balances_from = findViewById(R.id.add_transaction_balance_from);
        spn_balances_to = findViewById(R.id.add_transaction_balance_to);
        btn_date = findViewById(R.id.add_transaction_date);
        btn_save = findViewById(R.id.add_transaction_save);

        rg_type.setOnCheckedChangeListener(changeTypeEventListener());
        btn_date.setOnClickListener(dateDialogEventListener());
        btn_save.setOnClickListener(saveTransactionEventListener());

        spn_balances_from.setVisibility(View.VISIBLE);
        spn_category.setVisibility(View.VISIBLE);
        spn_balances_to.setVisibility(View.INVISIBLE);
        transaction.getCategory().setType(TransactionType.EXPENSE);
    }

    private RadioGroup.OnCheckedChangeListener changeTypeEventListener() {
        return (group, checkedId) -> {
            if (transaction.getCategory() == null) {
                transaction.setCategory(new Category());
            }
            switch (checkedId) {
                case R.id.add_transaction_type_expense:
                    spn_balances_from.setVisibility(View.VISIBLE);
                    spn_category.setVisibility(View.VISIBLE);
                    spn_balances_to.setVisibility(View.INVISIBLE);
                    transaction.getCategory().setType(TransactionType.EXPENSE);
                    break;
                case R.id.add_transaction_type_income:
                    spn_balances_from.setVisibility(View.INVISIBLE);
                    spn_category.setVisibility(View.VISIBLE);
                    spn_balances_to.setVisibility(View.VISIBLE);
                    transaction.getCategory().setType(TransactionType.INCOME);
                    break;
                case R.id.add_transaction_type_transfer:
                    spn_balances_from.setVisibility(View.VISIBLE);
                    spn_category.setVisibility(View.INVISIBLE);
                    spn_balances_to.setVisibility(View.VISIBLE);
                    transaction.getCategory().setType(TransactionType.TRANSFER);
                    break;
            }

            setCategoryAdapter();
        };
    }

    private View.OnClickListener dateDialogEventListener() {
        return v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTransactionActivity.this,
                    setDateEventListener(),
                    year, month, day);
            datePickerDialog.show();
            datePickerDialog
                    .getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.rally_dark_green));
            datePickerDialog
                    .getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.rally_dark_green));
        };
    }

    private DatePickerDialog.OnDateSetListener setDateEventListener() {
        return (view, year, month, dayOfMonth) -> {
            transaction.setDate(DateConverter.toDate(dayOfMonth, month, year));
            btn_date.setText(DateConverter.format(dayOfMonth, month, year));
        };
    }

    private View.OnClickListener saveTransactionEventListener() {
        return v -> {

            if (InputValidation.amountValidation(getBaseContext(), tiet_amount)) {

                buildTransaction();

                if (validate(rg_type.getCheckedRadioButtonId(), transaction)) {
                    close();
                }
            }
        };

    }

    private boolean validate(int id, Transaction transaction) {
        if (id == R.id.add_transaction_type_expense) {
            return InputValidation.expenseValidation(getBaseContext(), transaction);
        } else if (id == R.id.add_transaction_type_income) {
            return InputValidation.incomeValidation(getBaseContext(), transaction);
        } else {
            transaction.setCategory(Transfer.getTransferCategory());
            return InputValidation.transferValidation(getBaseContext(), transaction);
        }
    }

    private void buildTransaction() {
        if (!tiet_details.getText().toString().trim().isEmpty()) {
            transaction.setDetails(tiet_details.getText().toString().trim());
        }

        if (InputValidation.amountValidation(getBaseContext(), tiet_amount)) {
            double amount = Double.parseDouble(tiet_amount.getText().toString());
            transaction.setAmount(amount);
        }

        Balance balance_from = (Balance) spn_balances_from.getSelectedItem();
        transaction.setBalance_from(balance_from);

        Balance balance_to = (Balance) spn_balances_to.getSelectedItem();
        transaction.setBalance_to(balance_to);

        if (rg_type.getCheckedRadioButtonId() != R.id.add_transaction_type_transfer) {
            Category category = (Category) spn_category.getSelectedItem();
            transaction.setCategory(category);
        }
    }


    private void close() {
        if (isTransaction) {
            Transaction.saveTransaction(transaction);
        } else {
            Intent intent = getIntent();
            intent.putExtra(TRANSACTION, transaction);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    private void setCategoryAdapter() {
        ArrayAdapter<Category> adapter =
                new ArrayAdapter<>
                        (getApplicationContext(),
                                R.layout.row_spinner_simple,
                                getCategoriesByType());
        spn_category.setAdapter(adapter);
        spn_category.setPrompt(getString(R.string.add_transaction_spinner_prompt_categories));
        if (transaction.getCategory() != null) {
            spn_category.setSelection(getCategoryPosition(transaction.getCategory(), expenseCategories));
        }
    }

    private void setBalanceAdapter() {
        ArrayAdapter<Balance> adapter =
                new ArrayAdapter<>
                        (getApplicationContext(),
                                R.layout.row_spinner_simple,
                                balances);
        spn_balances_from.setAdapter(adapter);
        spn_balances_to.setAdapter(adapter);
        spn_balances_from.setPrompt(getString(R.string.add_transaction_spinner_prompt_balance_from));
        spn_balances_to.setPrompt(getString(R.string.add_transaction_spinner_prompt_balance_to));

        if (transaction.getBalance_from() != null) {
            spn_balances_from.setSelection(getBalancePosition(transaction.getBalance_from(), balances));
        }

        if (transaction.getBalance_to() != null) {
            spn_balances_to.setSelection(getBalancePosition(transaction.getBalance_to(), balances));
        }
    }

    private List<Category> getCategoriesByType() {
        if (rg_type.getCheckedRadioButtonId() == R.id.add_transaction_type_expense) {
            return expenseCategories;
        } else if (rg_type.getCheckedRadioButtonId() == R.id.add_transaction_type_income) {
            return incomeCategories;
        } else {
            return new ArrayList<>();
        }
    }
}