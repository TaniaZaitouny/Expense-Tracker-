package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;

import java.sql.SQLException;
import java.sql.Statement;

public class Category {


    public void addCategory(String name, String type) throws SQLException {
        String sqlQuery = "INSERT INTO categories (categoryName, type) " +
                "VALUES ('" + name + "', '" + type + "')";
        DatabaseConnection connection = DatabaseConnection.getInstance();
        Statement statement = connection.getConnection().createStatement();
        statement.executeUpdate(sqlQuery);
    }

}
