package com.example.expensetracker.Strategy;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class TransactionStrategy {
    Calendar calendar = Calendar.getInstance();
    int current_year = calendar.get(Calendar.YEAR);
    int current_month = calendar.get(Calendar.MONTH) + 1;
    int current_week = calendar.get(Calendar.WEEK_OF_YEAR);
    DatabaseConnection db = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();
    public abstract List<Pair<Pair<String, Number>,String>> topCategories(); // implemented with 2 different algorithms monthly/weekly

    List<Pair<Pair<String,Number>, String>> executeQuery(String sql) {
        List<Pair<Pair<String,Number>, String>> pairs = new ArrayList<>(5);
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String category = result.getString("category");
                Number totalAmount = result.getDouble("totalAmount");
                String categoryType = result.getString("categoryType");
                pairs.add(new Pair<>(new Pair<>(category, totalAmount), categoryType));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pairs;
    }
}