package com.example.expensetracker.Strategy;

import javafx.util.Pair;

import java.util.List;

public class DefaultStrategy extends TransactionStrategy {
    @Override
    public List<Pair<Pair<String, Number>, String>> topCategories() {
        String sql = "SELECT t.category AS category , SUM(t.amount) AS totalAmount , c.type AS categoryType " +
                "FROM transactions t " +
                "JOIN categories c ON category = c.categoryName " +
                "GROUP BY category " +
                "ORDER BY totalAmount desc " +
                "LIMIT 5";
        return executeQuery(sql);
    }

    @Override
    public Number totalExpense() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                "FROM transactions t " +
                "JOIN categories c ON t.category = c.categoryName " +
                "WHERE c.type = 'expense'";

        return executeQuery2(sql);
    }

    @Override
    public Number totalIncome() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                        "FROM transactions t " +
                        "JOIN categories c ON t.category = c.categoryName " +
                        "WHERE c.type = 'income'";

        return executeQuery2(sql);
    }
}
