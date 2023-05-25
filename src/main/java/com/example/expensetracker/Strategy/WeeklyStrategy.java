package com.example.expensetracker.Strategy;

import javafx.util.Pair;

import java.util.List;

public class WeeklyStrategy extends TransactionStrategy {
    @Override
    public List<Pair<Pair<String,Number>, String>> topCategories() {
        String sql = "SELECT t.category AS category , SUM(t.amount) AS totalAmount, c.type AS categoryType " +
                "FROM transactions t " +
                "JOIN categories c ON t.category = c.categoryName " +
                "WHERE YEAR(t.date) = " + current_year +
                " AND WEEK(t.date) = " + current_week +
                " AND c.userId = " + userId +
                " GROUP BY category " +
                "ORDER BY totalAmount DESC " +
                "LIMIT 5";
        return executeQuery(sql);
    }

    @Override
    public Number totalExpense() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                        "FROM transactions t " +
                        "JOIN categories c ON t.category = c.categoryName " +
                        "WHERE c.type = 'expense' AND YEAR(t.date) = " + current_year +
                        " AND WEEK(t.date) = " + current_week + " AND c.userId = " + userId;

        return executeQuery2(sql);
    }

    @Override
    public Number totalIncome() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                        "FROM transactions t " +
                        "JOIN categories c ON t.category = c.categoryName " +
                        "WHERE c.type = 'income' AND YEAR(t.date) = " + current_year +
                        " AND WEEK(t.date) = " + current_week + " AND c.userId = " + userId;

        return executeQuery2(sql);
    }
}
