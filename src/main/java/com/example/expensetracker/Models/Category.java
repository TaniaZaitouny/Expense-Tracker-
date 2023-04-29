package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Category {
    DatabaseConnection db  = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();
    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    private int userId = prefs.getInt("userId", 0);

    private boolean findCategory(String categoryName) throws SQLException {
        boolean found= false;
        String sql = "SELECT * FROM categories WHERE categoryName = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, categoryName);

        ResultSet result = statement.executeQuery();

        if (result.next()) {
            found = true;
        }
        result.close();
        statement.close();
        return found;
    };

    public boolean addCategory(String name, String type, String icon) throws SQLException {
        if(findCategory(name)) {
            return false;
        }
        String sqlQuery = "INSERT INTO categories (categoryName, userId, type, icon, frequency) " +
                "VALUES ('" + name+ "','" + userId + "', '" + type + "','" + icon +  ", 'NEVER')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.close();
        return true;
    }

    public boolean addCategory(String name, String type, String icon, String frequency, double amount) throws SQLException {
        if(findCategory(name)) {
            return false;
        }
        String sqlQuery = "INSERT INTO categories (categoryName, userId, type, icon, frequency, amount) " +
                "VALUES ('" + name + "','" + userId + "', '" + type + "','" + icon + "','" + frequency + "'," + amount + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.close();
        return true;
    }

    public void deleteCategory(String categoryName) throws SQLException {
        String sqlQuery = "DELETE FROM transactions WHERE category = '" + categoryName + "';";
        String sqlQuery2 = "DELETE FROM categories WHERE categoryName = '" + categoryName + "';";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.executeUpdate(sqlQuery2);
        statement.close();
    }

    public void updateCategory(String previousName, String newName, String newType) throws SQLException {
        String sqlQuery = "UPDATE transactions SET category = '" + newName +"' WHERE category = '" + previousName + "';";
        String sqlQuery2 = "UPDATE categories SET categoryName = '" + newName + "', type = '" + newType + "' WHERE categoryName = '" + previousName + "';";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.executeUpdate(sqlQuery2);
        statement.close();
    }

    public ArrayList<Pair<String, String>> getCategories() throws SQLException{
        ArrayList<Pair<String, String>> categories = new ArrayList<>();
        Statement statement = connection.createStatement();
        String query = "SELECT categoryName, type FROM categories WHERE userId = " + userId;
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            categories.add(new Pair<>(resultSet.getString(1), resultSet.getString(2)));
        }
        statement.close();
        return categories;
    }

    public void checkCategories() throws SQLException {
        String query = "SELECT * FROM categories WHERE frequency != 'NEVER'";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String frequency = resultSet.getString(5);
            LocalDate lastTransactionDate = resultSet.getDate(6).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            double amount = resultSet.getDouble(7);
            LocalDate currentDate = LocalDate.now();
            Transaction transaction = new Transaction();
            switch (frequency) {
                case "DAILY":
                    if (lastTransactionDate.plusDays(1).isBefore(currentDate) || lastTransactionDate.plusDays(1).isEqual(currentDate)) {
                        transaction.addTransaction(currentDate, name, amount);
                    }
                    break;
                case "WEEKLY":
                    if (lastTransactionDate.plusWeeks(1).isBefore(currentDate) || lastTransactionDate.plusWeeks(1).isEqual(currentDate)) {
                        transaction.addTransaction(currentDate, name, amount);
                    }
                    break;
                case "MONTHLY":
                    if (lastTransactionDate.plusMonths(1).isBefore(currentDate) || lastTransactionDate.plusMonths(1).isEqual(currentDate)) {
                        transaction.addTransaction(currentDate, name, amount);
                    }
                    break;
                case "YEARLY":
                    if (lastTransactionDate.plusYears(1).isBefore(currentDate) || lastTransactionDate.plusYears(1).isEqual(currentDate)) {
                        transaction.addTransaction(currentDate, name, amount);
                    }
                    break;
                default:
                    System.out.println("Invalid frequency for category: " + name);
                    break;
            }
        }
        statement.close();
    }
}
