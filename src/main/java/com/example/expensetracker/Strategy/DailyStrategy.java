package com.example.expensetracker.Strategy;

import javafx.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DailyStrategy extends TransactionStrategy{
    Date current_date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = dateFormat.format(current_date);
    @Override
    public List<Pair<Pair<String, Number>, String>> topCategories() {
        String sql = "SELECT t.category AS category , t.amount AS totalAmount , c.type AS categoryType " +
                "FROM transactions t " +
                "JOIN categories c ON category = c.categoryName " +
                "WHERE t.date = '" + formattedDate + "' " +
                "GROUP BY category " +
                "ORDER BY totalAmount desc " +
                "LIMIT 5";
        List<Pair<Pair<String, Number>, String>> pairs = executeQuery(sql);
        return pairs;
    }

    public Number totalIncome() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                        "FROM transactions t " +
                        "JOIN categories c ON t.category = c.categoryName " +
                        "WHERE c.type = 'income' AND t.date = '" + formattedDate + "' " ;
        Number result = executeQuery2(sql);

        return result;
    }
    public Number totalExpense() {
        String sql =
                "SELECT SUM(t.amount) AS totalExpense " +
                        "FROM transactions t " +
                        "JOIN categories c ON t.category = c.categoryName " +
                        "WHERE c.type = 'expense' AND t.date = '" + formattedDate + "' " ;
        Number result = executeQuery2(sql);

        return result;
    }

}
