package com.example.expensetracker.Strategy;

import javafx.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DailyStrategy extends TransactionStrategy {
    Date current_date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = dateFormat.format(current_date);

    @Override
    public List<Pair<Pair<String, Number>, String>> topCategories() {
        String sql = "SELECT t.category AS category, SUM(t.amount) AS totalAmount, c.type AS categoryType " +
                "FROM transactions t " +
                "JOIN categories c ON category = c.categoryName " +
                "WHERE t.date = '" + formattedDate + "' AND c.userId = " + userId + " " +
                "GROUP BY category " +
                "ORDER BY totalAmount desc " +
                "LIMIT 5";
        return executeQuery(sql);
    }

    public Number totalIncome() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                        "FROM transactions t " +
                        "JOIN categories c ON t.category = c.categoryName " +
                        "WHERE c.type = 'income' AND t.date = '" + formattedDate + "'  AND c.userId = " + userId;

        return executeQuery2(sql);
    }
    public Number totalExpense() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                        "FROM transactions t " +
                        "JOIN categories c ON t.category = c.categoryName " +
                        "WHERE c.type = 'expense' AND t.date = '" + formattedDate + "' AND c.userId = " + userId;

        return executeQuery2(sql);
    }

}
