package com.example.expensetracker.Filters;

import com.example.expensetracker.Objects.CategoryObject;
import com.example.expensetracker.Objects.TransactionObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public interface TransactionFilter
{
    public ArrayList<TransactionObject> filter(String filterType) throws SQLException;
    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    public static int userId = prefs.getInt("userId", 0);
}
