package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Transaction {

    Connection connection;
    private int userId;
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pairs;
    }

    public void addTransaction(LocalDate date, String selectedCategory, Double amount) throws SQLException {
        String sqlQuery = "INSERT INTO transactions(userId,date,amount,category)" +
                "VALUES('"+userId+"','"+date+"','"+amount+"','"+selectedCategory+"')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }
    public void deleteTransaction(int transactionId) throws SQLException{
        String sqlQuery = "DELETE FROM transactions WHERE id =" + transactionId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

    public void updateTransaction(int transactionId, LocalDate newDate, String newCategory, Double newAmount) throws SQLException{
        String sqlQuery = "UPDATE transactions SET date = '" + newDate + "', amount = '" + newAmount + "', category = '" + newCategory +"' WHERE id = " + transactionId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

    public ArrayList<String[]> getTransactions() throws SQLException
    {
        ArrayList<String[]> transactions = new ArrayList<String[]>();
        Statement statement = connection.createStatement();
        String query = "SELECT category, date, amount, id FROM transactions WHERE userId = " + userId;
//        System.out.println("Executing query: " + query);
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String[] singleTransaction = new String[4];
            singleTransaction[0] = resultSet.getString(1);
            singleTransaction[1] = resultSet.getString(2);
            singleTransaction[2] = resultSet.getString(3);
            singleTransaction[3] = resultSet.getString(4);
            transactions.add(singleTransaction);
        }
        return transactions;
    }

}
