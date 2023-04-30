package com.example.expensetracker.Filters;

import com.example.expensetracker.Objects.CategoryObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public interface CategoryFilter
{
    public ArrayList<CategoryObject> filter(String filterType) throws SQLException;
    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    public static int userId = prefs.getInt("userId", 0);
}
