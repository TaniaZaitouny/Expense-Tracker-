package com.example.expensetracker.Objects;

import java.util.Date;

public class TransactionObject {
    public Date date;
    public double amount;
    public int id;
    public int userId;
    public String category;

    public TransactionObject(int id, int userId, Date date, double amount, String category) {
        this.date = date;
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.userId = userId;
    }
}
