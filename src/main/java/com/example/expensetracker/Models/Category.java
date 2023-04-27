package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;

import java.sql.SQLException;

public class Category {
    public void addCategory(String name,String type) throws SQLException {
        DatabaseConnection connection = DatabaseConnection.getInstance();
        connection.addCategory(name,type);
        //System.out.println(name+" "+type);

    }
}
