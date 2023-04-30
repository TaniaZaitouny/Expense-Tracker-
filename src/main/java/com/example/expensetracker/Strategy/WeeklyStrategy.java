package com.example.expensetracker.Strategy;

import javafx.util.Pair;

import java.util.List;

public class WeeklyStrategy extends TransactionStrategy{
    @Override
    public List<Pair<Pair<String,Number>, String>> topCategories()
    {

        System.out.println(current_week);
        String sql = "SELECT t.category AS category , SUM(t.amount) AS totalAmount, c.type AS categoryType " +
                "FROM transactions t " +
                "JOIN categories c ON t.category = c.categoryName " +
                "WHERE YEAR(t.date) = " + current_year +
                " AND WEEK(t.date) = " + current_week +
                " GROUP BY category " +
                "ORDER BY totalAmount DESC " +
                "LIMIT 5";
        List<Pair<Pair<String,Number>, String>> pairs = executeQuery(sql);
        return pairs;
    }
}
