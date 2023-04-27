package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;

import java.sql.Connection;

public class Report {
    Connection connection;

    public Report() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }


}
