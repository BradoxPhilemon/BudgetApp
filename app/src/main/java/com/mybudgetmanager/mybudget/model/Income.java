package com.mybudgetmanager.mybudget.model;


import com.mybudgetmanager.mybudget.firebase.services.BalanceService;
import com.mybudgetmanager.mybudget.firebase.services.TransactionService;

public class Income extends Transaction{

    public Income(Transaction transaction) {
        super(transaction);
    }


    public static void saveIncome(Income income) {
        income.getBalance_to().deposit(income.getAmount());

        TransactionService transactionService = new TransactionService();
        transactionService.upsert(income);

        BalanceService balanceService = new BalanceService();
        balanceService.upsert(income.getBalance_to());
    }

    public static void deleteIncome(Income income) {
        income.getBalance_to().withdraw(income.getAmount());

        TransactionService transactionService = new TransactionService();
        transactionService.delete(income);

        BalanceService balanceService = new BalanceService();
        balanceService.upsert(income.getBalance_to());
    }

}
