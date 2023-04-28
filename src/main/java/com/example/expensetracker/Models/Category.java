package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Category {
    DatabaseConnection db  = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();
    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    private int userId = prefs.getInt("userId", 0);

    public void addCategory(String name,String type) throws SQLException {

        String sqlQuery = "INSERT INTO categories (userId,categoryName, type) " +
                "VALUES ('"+userId+"','" + name + "', '" + type + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);

    }
    public void deleteCategory(int categoryId) throws SQLException{
        String sqlQuery = "DELETE FROM categories WHERE id =" + categoryId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

    public void updateCategory(int categoryId, String newName, String newType) throws SQLException{
        String sqlQuery = "UPDATE categories SET categoryName = '" + newName + "', type = '" + newType + "' WHERE id = " + categoryId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

    public ArrayList<Pair<String,String>> getCategories() throws SQLException{
        ArrayList<Pair<String,String>> categories = new ArrayList<>();
        Statement statement = connection.createStatement();
        String query = "SELECT categoryName,type FROM categories WHERE userId = '"+userId+"';";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Pair result = new Pair<>(resultSet.getString(1),resultSet.getString(2));
            categories.add(result);
        }

        return categories;
    }

}
