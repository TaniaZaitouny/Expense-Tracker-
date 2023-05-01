package com.example.expensetracker.Filters.TransactionFilters;

import com.example.expensetracker.Objects.TransactionObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public interface TransactionFilter {
    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    int userId = prefs.getInt("userId", 0);

    ArrayList<TransactionObject> filter(String filterType) throws SQLException;
}
