package com.mybudgetmanager.mybudget.model;


import com.mybudgetmanager.mybudget.firebase.services.BalanceService;
import com.mybudgetmanager.mybudget.firebase.services.TransactionService;

public class Expense extends Transaction{

    public Expense(Transaction transaction) {
        super(transaction);
    }

    public static void saveExpense(Expense expense) {
        expense.getBalance_from().withdraw(expense.getAmount());

        TransactionService transactionService = new TransactionService();
        transactionService.upsert(expense);

        BalanceService balanceService = new BalanceService();
        balanceService.upsert(expense.getBalance_from());
    }

    public static void deleteExpense(Expense expense) {
        expense.getBalance_from().deposit(expense.getAmount());

        TransactionService transactionService = new TransactionService();
        transactionService.delete(expense);

        BalanceService balanceService = new BalanceService();
        balanceService.upsert(expense.getBalance_from());
    }
}
