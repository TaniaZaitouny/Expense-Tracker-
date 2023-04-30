package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Filters.TransactionFilter;
import com.example.expensetracker.Filters.TransactionNormalFilter;
import com.example.expensetracker.Objects.CategoryObject;
import com.example.expensetracker.Objects.TransactionObject;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.prefs.Preferences;

public class Transaction {

    Connection connection;
    private final int userId;

    public Transaction() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
        Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
        userId = prefs.getInt("userId", 0);
    }



    public void addTransaction(LocalDate date, String selectedCategory, Double amount) throws SQLException {
        String sqlQuery = "INSERT INTO transactions(userId,date,amount,category)" +
                "VALUES('" + userId + "','" + date + "','" + amount + "','" + selectedCategory + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.close();
    }
    public void deleteTransaction(int transactionId) throws SQLException{
        String sqlQuery = "DELETE FROM transactions WHERE id =" + transactionId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    public void updateTransaction(int transactionId, LocalDate newDate, String newCategory, Double newAmount) throws SQLException{
        String sqlQuery = "UPDATE transactions SET date = '" + newDate + "', amount = '" + newAmount + "', category = '" + newCategory +"' WHERE id = " + transactionId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    public ArrayList<TransactionObject> getTransactions() throws SQLException
    {
        TransactionFilter filter = new TransactionNormalFilter();
        return filter.filter("");
    }

    public TransactionObject getTransaction(int id) throws SQLException {
        String sql = "SELECT * FROM transactions where id = '" + id + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        TransactionObject transaction = new TransactionObject(resultSet.getInt(1), resultSet.getInt(2), resultSet.getDate(3), resultSet.getDouble(4), resultSet.getString(5));
        statement.close();
        return transaction;

    }

    //Strategy design pattern to get top transactions monthly and weekly
    public abstract class TransactionStrategy{
        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);
        int current_month = calendar.get(Calendar.MONTH) + 1;
        int current_week = calendar.get(Calendar.WEEK_OF_YEAR);
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
    public class DefaultStrategy extends TransactionStrategy {
        @Override
        public List<Pair<Pair<String, Number>, String>> topCategories() {
            String sql = "SELECT t.category AS category , t.amount AS totalAmount , c.type AS categoryType " +
                    "FROM transactions t " +
                    "JOIN categories c ON category = c.categoryName " +
                    "GROUP BY category " +
                    "ORDER BY totalAmount desc " +
                    "LIMIT 5";
            List<Pair<Pair<String, Number>, String>> pairs = executeQuery(sql);
            return pairs;
        }

    }
    public class MonthlyStrategy extends TransactionStrategy {
        @Override
        public List<Pair<Pair<String,Number>, String>> topCategories()
        {

            System.out.println(current_month);
            String sql = "SELECT t.category AS category , SUM(t.amount) AS totalAmount , c.type AS categoryType " +
                    "FROM transactions t " +
                    "JOIN categories c ON category = c.categoryName " +
                    "WHERE YEAR(t.date) = " + current_year +
                    " AND MONTH(t.date) = " + current_month +
                    " GROUP BY category " +
                    "ORDER BY totalAmount DESC " +
                    "LIMIT 5";
             List<Pair<Pair<String,Number>, String>> pairs = executeQuery(sql);
            return pairs;
        }
    }

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


}
