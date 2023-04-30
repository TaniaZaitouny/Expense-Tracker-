package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Filters.TransactionFilter;
import com.example.expensetracker.Filters.TransactionNormalFilter;
import com.example.expensetracker.Objects.TransactionObject;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<Pair<String, Number>> getTopCategories() {
        String sql = "SELECT category, amount " +
                "FROM transactions " +
                "GROUP BY category " +
                "ORDER BY amount desc " +
                "LIMIT 5";
        List<Pair<String, Number>> pairs = new ArrayList<>(5);
        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                pairs.add(new Pair<>(result.getString("category"), result.getDouble(2)));
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pairs;
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

}
