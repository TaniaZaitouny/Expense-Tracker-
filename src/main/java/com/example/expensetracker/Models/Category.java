package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Category {
    DatabaseConnection db  = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();
    public void addCategory(String name,String type) throws SQLException {

        String sqlQuery = "INSERT INTO categories (category_name, type) " +
                "VALUES ('" + name + "', '" + type + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);

    }
    public void deleteCategory(int categoryId) throws SQLException{
        String sqlQuery = "DELETE FROM categories WHERE id =" + categoryId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

    public void updateCategory(int categoryId, String newName, String newType) throws SQLException{
        String sqlQuery = "UPDATE categories SET category_name = '" + newName + "', type = '" + newType + "' WHERE id = " + categoryId;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

    public ObservableList<String> getCategories() throws SQLException{
        ObservableList<String> categories = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT category_name FROM categories";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String category = resultSet.getString("category_name");
            categories.add(category);
        }

        return categories;
    }

}
