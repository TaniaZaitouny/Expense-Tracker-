package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Filters.TransactionFilters.TransactionFilter;
import com.example.expensetracker.Objects.TransactionObject;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public ArrayList<TransactionObject> getTransactions(TransactionFilter Filter, String filterType) throws SQLException
    {
        return Filter.filter(filterType);
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




}
