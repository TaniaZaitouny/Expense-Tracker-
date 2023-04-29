package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.*;
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

    public boolean addCategory(String name,String type) throws SQLException {
        if(findCategory(name)) {
            return false;
        }
        String sqlQuery = "INSERT INTO categories (userId, categoryName, type) " +
                "VALUES ('" + userId + "','" + name + "', '" + type + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.close();
        return true;

    }
    public void deleteCategory(String categoryName) throws SQLException{
        String sqlQuery = "DELETE FROM categories WHERE categoryName = '" + categoryName+"';";
        String sqlQuery2 = "DELETE FROM transactions WHERE category = '" + categoryName+"';";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.executeUpdate(sqlQuery2);
        statement.close();
    }

    public void updateCategory(String previousName, String newName, String newType) throws SQLException{
        String sqlQuery = "UPDATE categories SET categoryName = '" + newName + "', type = '" + newType + "' WHERE categoryName = '" + previousName + "';";
        String sqlQuery2 = "UPDATE transactions SET category = '" + newName +"' WHERE category = '" + previousName + "';";
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

}
