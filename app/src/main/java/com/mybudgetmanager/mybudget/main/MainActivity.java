package com.mybudgetmanager.mybudget.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;




import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.adapters.TransactionExpandableAdapter;
import com.mybudgetmanager.mybudget.async.Callback;
import com.mybudgetmanager.mybudget.charts.ChartType;
import com.mybudgetmanager.mybudget.charts.PieChartFragment;
import com.mybudgetmanager.mybudget.firebase.DateDisplayType;
import com.mybudgetmanager.mybudget.firebase.services.BalanceService;
import com.mybudgetmanager.mybudget.firebase.services.CategoryService;
import com.mybudgetmanager.mybudget.firebase.services.TransactionService;
import com.mybudgetmanager.mybudget.model.Transaction;
import com.mybudgetmanager.mybudget.settings.SettingsActivity;
import com.mybudgetmanager.mybudget.settings.language.LanguageManager;
import com.mybudgetmanager.mybudget.util.CustomDialog;
import com.mybudgetmanager.mybudget.util.DateConverter;
import com.mybudgetmanager.mybudget.util.Streak;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_TRANSACTION = 201;
    private static final int SNACKBAR_DELAY = 2000;
    private static int COUNT = 0;

    private ImageButton ib_refresh;
    private TextView tv_date_filter;
    private ImageButton btn_settings;
    private ImageButton btn_add;
    private ImageButton ib_chart_pie;
    private ImageButton ib_chart_bar;
    private ImageButton ib_chart_line;


    private TabLayout tabLayout;
    private ExpandableListView elv_transactions;

    private ArrayList<Transaction> transactionList = new ArrayList<>();

    private TransactionService transactionService = new TransactionService();
    private BalanceService balanceService = new BalanceService();
    private CategoryService categoryService = new CategoryService();

    private int day, month, year;

    private Fragment fragment = new PieChartFragment();
    private DateDisplayType dateDisplayType = DateDisplayType.DAY_MONTH_YEAR;
    private ChartType selectedChartType = ChartType.PIE_CHART;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LanguageManager.getSettings(MainActivity.this);
        LanguageManager.apply(MainActivity.this);

        initComponents();
        setDefaultDate();
        if (COUNT == 0) {
            displayStreakDays();
        }

    }

    private void displayStreakDays() {
        new Handler().postDelayed(() -> {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout,
                    getResources().getString(R.string.settings_account_streak, Streak.days),
                    BaseTransientBottomBar.LENGTH_LONG).show();
        }, SNACKBAR_DELAY);
        COUNT++;
    }

    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setFilterText();
        filterTransactions();
    }

    private void initComponents() {
        ib_refresh = findViewById(R.id.main_refresh);
        ib_refresh.setOnClickListener(v -> setDefaultDate());

        tv_date_filter = findViewById(R.id.main_date_filter);
        tv_date_filter.setOnClickListener(filterEventListener());

        btn_add = findViewById(R.id.main_add_transaction);
        btn_settings = findViewById(R.id.main_settings);

        btn_add.setOnClickListener(addEventListener());
        btn_settings.setOnClickListener(settingsEventListener());

        tabLayout = findViewById(R.id.main_tabs);
        tabLayout.addOnTabSelectedListener(changeTabEventListener());

        ib_chart_pie = findViewById(R.id.main_chart_pie);
        ib_chart_bar = findViewById(R.id.main_chart_bar);
        ib_chart_line = findViewById(R.id.main_chart_line);

        ib_chart_pie.setOnClickListener(changeChart(ChartType.PIE_CHART));
        ib_chart_bar.setOnClickListener(changeChart(ChartType.BAR_CHART));
        ib_chart_line.setOnClickListener(changeChart(ChartType.LINE_CHART));

        setExpandableListView();
    }

    private void setExpandableListView() {
        elv_transactions = findViewById(R.id.main_expandable_list_transactions);
        setAdapter();
        final int[] lastPosition = {-1};
        elv_transactions.setOnGroupExpandListener(groupPosition -> {
            if (lastPosition[0] != -1
                    && groupPosition != lastPosition[0]) {
                elv_transactions.collapseGroup(lastPosition[0]);
            }
            lastPosition[0] = groupPosition;
        });

        elv_transactions.setOnItemLongClickListener(deleteTransactionEventListener());
    }

    private AdapterView.OnItemLongClickListener deleteTransactionEventListener() {
        return (parent, view, position, id) -> {
            CustomDialog.show(this,
                    R.string.dialog_delete_header,
                    R.string.dialog_delete_transaction,
                    deleteTransaction(position));
            return false;
        };
    }

    private DialogInterface.OnClickListener deleteTransaction(int position) {
        return (dialog, which) -> {
            Transaction.deleteTransaction(transactionList.get(position));
        };
    }


    private TabLayout.OnTabSelectedListener changeTabEventListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                displaySelectedFragment(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private void displaySelectedFragment(TabLayout.Tab tab) {
        dateDisplayType = DateDisplayType.getDateDisplayType(tab.getPosition());
        filterTransactions();
    }


    private View.OnClickListener changeChart(ChartType chartType) {
        return v -> {
            selectedChartType = chartType;
            show();
        };
    }


    private View.OnClickListener filterEventListener() {
        return v -> {
            dateDisplayType = DateDisplayType.getDateDisplayType(tabLayout.getSelectedTabPosition());
            buildDatePicker();
        };
    }

    private void buildDatePicker() {
        switch (dateDisplayType) {
            case MONTH_YEAR:
            case YEAR:
                buildMonthYearPicker(dateDisplayType);
                break;
            case TOTAL:
                break;
            default:
                buildDayMonthYearPicker();
                break;
        }
    }

    private void buildDayMonthYearPicker() {
        DatePickerDialog datePicker = new DatePickerDialog(
                MainActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    day = selectedDay;
                    month = selectedMonth;
                    year = selectedYear;
                    filterTransactions();
                },
                year, month, day);
        datePicker.show();
        datePicker
                .getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.rally_dark_green));
        datePicker
                .getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.rally_dark_green));
    }

    private void buildMonthYearPicker(DateDisplayType type) {
        MonthPickerDialog.Builder picker = new MonthPickerDialog.Builder(MainActivity.this,
                (selectedMonth, selectedYear) -> {
                    month = selectedMonth;
                    year = selectedYear;
                    filterTransactions();
                }
                , year, month);

        picker.setActivatedMonth(month)
                .setMinYear(1990)
                .setActivatedYear(year)
                .setMaxYear(2099);

        if (type.equals(DateDisplayType.YEAR)) {
            picker.showYearOnly();
        }

        picker.build()
                .show();
    }

    private void getTransactionsFromFirebase() {
        Date date = DateConverter.toDate(day, month, year);
        transactionService.updateTransactionsUI(updateTransactionsCallback(), dateDisplayType, date);
    }

    private Callback<List<Transaction>> updateTransactionsCallback() {
        return result -> {
            if (result != null) {
                transactionList.clear();
                transactionList.addAll(result);
                setAdapter();
                show();
            }
        };
    }

    private View.OnClickListener addEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), AddTransactionActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_TRANSACTION);
        };
    }

    private View.OnClickListener settingsEventListener() {
        return v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        };
    }

    private void setAdapter() {
        TransactionExpandableAdapter adapter = new TransactionExpandableAdapter(MainActivity.this, transactionList);
        elv_transactions.setAdapter(adapter);
    }

    private void notifyAdapter() {
        TransactionExpandableAdapter adapter = (TransactionExpandableAdapter) elv_transactions.getExpandableListAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TRANSACTION && resultCode == RESULT_OK && data != null) {
            Transaction transaction = data.getParcelableExtra(AddTransactionActivity.TRANSACTION);
            Transaction.saveTransaction(transaction);
            notifyAdapter();
        }
    }


    private void filterTransactions() {
        setFilterText();
        getTransactionsFromFirebase();
    }

    private void setFilterText() {
        tv_date_filter.setText(DateDisplayType.display(getApplicationContext(), dateDisplayType, day, month, year));
    }

    private void show() {
        this.fragment = ChartType.getFragment(selectedChartType, transactionList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment, this.fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}