package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Filters.CategoryFilters.CategoryFilter;
import com.example.expensetracker.Objects.CategoryObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Category {
    DatabaseConnection db  = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();
    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    private final int userId = prefs.getInt("userId", 0);

    private boolean findCategory(String name) throws SQLException {
        boolean found = false;
        String sql = "SELECT * FROM categories WHERE categoryName = '" + name + "'";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            found = true;
        }

        result.close();
        statement.close();
        return found;
    }

    public CategoryObject getCategory(String name) throws SQLException {
        String sql = "SELECT * FROM categories where categoryName = '" + name + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        CategoryObject category = new CategoryObject(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getDate(6), resultSet.getDouble(7));
        statement.close();
        return category;
    }

    public boolean addCategory(String name, String type, String icon) throws SQLException {
        if(findCategory(name)) {
            return false;
        }
        String sqlQuery = "INSERT INTO categories (categoryName, userId, type, icon, frequency) " +
                "VALUES ('" + name+ "','" + userId + "', '" + type + "','" + icon + "', 'NEVER')";
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

    public void updateCategory(String categoryToUpdate, String name, String type, String icon) throws SQLException {
        String sqlQuery = "UPDATE transactions SET category = '" + name +"' WHERE category = '" + categoryToUpdate + "';";
        String sqlQuery2 = "UPDATE categories SET categoryName = '" + name + "', type = '" + type + "', icon = '" + icon + "', frequency = 'NEVER' WHERE categoryName = '" + categoryToUpdate + "';";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.executeUpdate(sqlQuery2);
        statement.close();
    }

    public void updateCategory(String previousName, String name, String type, String icon, String frequency, double amount) throws SQLException {
        String sqlQuery = "UPDATE transactions SET category = '" + name +"' WHERE category = '" + previousName + "';";
        String sqlQuery2 = "UPDATE categories SET categoryName = '" + name + "', type = '" + type + "', icon = '" + icon + "', frequency = '" + frequency + "', amount = " + amount +  " WHERE categoryName = '" + previousName + "';";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.executeUpdate(sqlQuery2);
        statement.close();
    }

    public void deleteCategory(String categoryName) throws SQLException {
        String sqlQuery = "DELETE FROM transactions WHERE category = '" + categoryName + "';";
        String sqlQuery2 = "DELETE FROM categories WHERE categoryName = '" + categoryName + "';";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
        statement.executeUpdate(sqlQuery2);
        statement.close();
    }

    public ArrayList<CategoryObject> getCategories(CategoryFilter Filter, String filterType) throws SQLException{
        return Filter.filter(filterType);
    }

    public String getIcon(String name) {
        String sql = "SELECT icon AS iconName FROM categories WHERE categoryName = '" + name + "';";
        String iconName;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery(sql);
            if (resultset.next()) {
                iconName = resultset.getString("iconName");
            } else {
                iconName = "Category";
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return iconName;
    }

}
