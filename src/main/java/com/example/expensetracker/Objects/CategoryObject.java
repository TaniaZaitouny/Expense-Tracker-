package com.example.expensetracker.Objects;

import java.util.Date;

public class CategoryObject {
    public String categoryName;
    public String categoryIcon;
    public int userId;
    public String categoryType;
    public String frequency;
    public Date lastTransaction;
    public double amount;

    public CategoryObject(String categoryName, int userId, String categoryType, String categoryIcon, String frequency, Date lastTransaction, double amount) {
        this.categoryIcon = categoryIcon;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.userId = userId;
        this.amount = amount;
        this.frequency = frequency;
        this.lastTransaction = lastTransaction;
    }

}
