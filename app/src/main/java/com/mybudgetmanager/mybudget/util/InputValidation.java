package com.mybudgetmanager.mybudget.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;



import com.google.android.material.textfield.TextInputEditText;
import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.model.Transaction;

import java.util.regex.Pattern;


public class InputValidation {

    public static int NUMBER_OF_MINIMUM_CHARACTERS_PASSWORD = 6;

    public static int MINIMUM_NUMBER_OF_CHARACTERS_NAME = 3;
    public static int MAXIMUM_NUMBER_OF_CHARACTERS_NAME = 20;

    public static String REGEX_ONLY_LETTERS = "^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$";

    public static double MINIMUM_AMOUNT = 0.0;
    public static double MAXIMUM_AMOUNT = 1_000_000.0;


    public static boolean nameValidation(Context context, TextInputEditText tiet) {
        if (tiet.getText() == null) {
            throw new NullPointerException();
        }
        String name = tiet.getText().toString().trim();

        if (name.length() < MINIMUM_NUMBER_OF_CHARACTERS_NAME || name.length() > MAXIMUM_NUMBER_OF_CHARACTERS_NAME) {
            tiet.setError(context.getResources().getString(R.string.error_invalid_name_length));
            return false;
        } else if (!isMatching(REGEX_ONLY_LETTERS, name)) {
            tiet.setError(context.getResources().getString(R.string.error_invalid_name_not_letters));
            return false;
        } else {
            tiet.setError(null);
            return true;
        }
    }

    private static boolean isMatching(String regex, String string) {
        if (string == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string).matches();
    }

    public static boolean emailValidation(Context context, TextInputEditText tiet) {
        if (tiet.getText() == null) {
            throw new NullPointerException();
        }
        String email = tiet.getText().toString().trim();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tiet.setError(null);
            return true;
        } else {
            tiet.setError(context.getResources().getString(R.string.error_invalid_email));
            return false;
        }

    }

    public static boolean passwordValidation(Context context, TextInputEditText tiet) {
        if (tiet.getText() == null) {
            throw new NullPointerException();
        }
        if (tiet.getText().toString().trim().length() >= NUMBER_OF_MINIMUM_CHARACTERS_PASSWORD) {
            tiet.setError(null);
            return true;
        } else {
            tiet.setError(context.getResources().getString(R.string.error_invalid_password));
            return false;
        }
    }

    public static boolean loginValidation(Context context, TextInputEditText email, TextInputEditText password) {
        return emailValidation(context, email) && passwordValidation(context, password);
    }


    public static boolean registerValidation(Context context, TextInputEditText name, TextInputEditText email, TextInputEditText password) {
        return nameValidation(context, name) && emailValidation(context, email) && passwordValidation(context, password);
    }

    public static boolean availableAmountValidation(Context context, TextInputEditText tiet) {
        if (tiet.getText() == null) {
            throw new NullPointerException();
        }

        String text = tiet.getText().toString();
        if (text.isEmpty()) {
            text = "0";
        }

        double amount = Double.parseDouble(text);
        if (amount > MINIMUM_AMOUNT && amount < MAXIMUM_AMOUNT) {
            tiet.setError(null);
            return true;
        } else {
            tiet.setError(context.getResources().getString(R.string.error_invalid_amount));
            return false;
        }
    }

    public static boolean expenseValidation(Context context, Transaction expense) {
        expense.setBalance_to(null);

        String error = null;
        if (expense.getBalance_from() == null) {
            error = context.getResources().getString(R.string.error_invalid_balance_from);
        }
        if (expense.getAmount() <= MINIMUM_AMOUNT || expense.getAmount() >= MAXIMUM_AMOUNT) {
            error = context.getResources().getString(R.string.error_invalid_amount);
        }
        if (expense.getBalance_from().getAvailable_amount() < expense.getAmount()) {
            error = context.getResources().getString(R.string.error_amount_not_available);
        }
        if (expense.getCategory() == null) {
            error = context.getResources().getString(R.string.error_invalid_category);
        }

        if (error != null) {
            Toast.makeText(context,
                    error,
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean incomeValidation(Context context, Transaction income) {
        income.setBalance_from(null);

        String error = null;
        if (income.getBalance_to() == null) {
            error = context.getResources().getString(R.string.error_invalid_balance_to);
        }

        if (income.getAmount() <= MINIMUM_AMOUNT || income.getAmount() >= MAXIMUM_AMOUNT) {
            error = context.getResources().getString(R.string.error_invalid_amount);
        }

        if (income.getCategory() == null) {
            error = context.getResources().getString(R.string.error_invalid_category);
        }

        if (error != null) {
            Toast.makeText(context,
                    error,
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean transferValidation(Context context, Transaction transfer) {
        String error = null;
        if (transfer.getBalance_from() == null) {
            error = context.getResources().getString(R.string.error_invalid_balance_from);
        }
        if (transfer.getBalance_to() == null) {
            error = context.getResources().getString(R.string.error_invalid_balance_to);
        }
        if (transfer.getBalance_from().getId().equals(transfer.getBalance_to().getId())) {
            error = context.getResources().getString(R.string.error_same_balances_transfer);
        }
        if (transfer.getAmount() <= MINIMUM_AMOUNT) {
            error = context.getResources().getString(R.string.error_amount_negative);
        }
        if (transfer.getBalance_from().getAvailable_amount() < transfer.getAmount()) {
            error = context.getResources().getString(R.string.error_amount_not_available);
        }

        if (error != null) {
            Toast.makeText(context,
                    error,
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean amountValidation(Context context, TextInputEditText tiet) {
        if (tiet.getText() == null) {
            throw new NullPointerException();
        }

        double amount = MINIMUM_AMOUNT;

        String text = tiet.getText().toString().trim();
        if (!text.isEmpty()) {
            amount = Double.parseDouble(text);
        }

        if (amount > MINIMUM_AMOUNT && amount < MAXIMUM_AMOUNT) {
            tiet.setError(null);
            return true;
        } else {
            tiet.setError(context.getResources().getString(R.string.error_invalid_amount));
            return false;
        }
    }


}
