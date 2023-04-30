package com.example.expensetracker.Database;

import com.example.expensetracker.Models.Category;

import java.sql.*;

public class DatabaseConnection {

    private static DatabaseConnection  databaseConnection = null;
    private final Connection connection;

    private DatabaseConnection() {
        String dbURL = "jdbc:mysql://localhost:3306/expense-tracker";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(dbURL, user, password);
            this.createTables();
//            Category category = new Category();
//            category.checkCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if(databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void createTables() {
        this.createUsersTable();
        this.createCategoriesTable();
        this.createTransactionsTable();
    }

    private void createUsersTable() {
        String sql = "CREATE TABLE USERS " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " username VARCHAR(255), " +
                " email VARCHAR(255), " +
                " password VARCHAR(255), " +
                " PRIMARY KEY (id))";
        try {
            Statement statement = this.connection.createStatement();
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet resultSet = meta.getTables("expense-tracker", null, "USERS", new String[] {"TABLE"});
            if(!resultSet.next()) {
                statement.executeUpdate(sql);
                System.out.println("Created users table in given database...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCategoriesTable() {
        String sql = "CREATE TABLE CATEGORIES " +
                "(categoryName VARCHAR(255) not NULL, " +
                " userId INTEGER not NULL, " +
                " type VARCHAR(255), " +
                " icon VARCHAR(255), " +
                " frequency VARCHAR(255), " +
                " lastTransaction DATE, " +
                " amount DOUBLE, " +
                " PRIMARY KEY (categoryName), " +
                " FOREIGN KEY (userId) REFERENCES users(id))";
        try {
            Statement statement = this.connection.createStatement();
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet resultSet = meta.getTables("expense-tracker", null, "CATEGORIES", new String[] {"TABLE"});
            if(!resultSet.next()) {
                statement.executeUpdate(sql);
                System.out.println("Created categories table in given database...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTransactionsTable() {
        String sql = "CREATE TABLE TRANSACTIONS " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " userId INTEGER not NULL, " +
                " date DATE, " +
                " amount DOUBLE, " +
                " category VARCHAR(255) not null, " +
                " PRIMARY KEY (id), " +
                " FOREIGN KEY (category) REFERENCES categories(categoryName), " +
                " FOREIGN KEY (userId) REFERENCES users(id))";
        try {
            Statement statement = this.connection.createStatement();
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet resultSet = meta.getTables("expense-tracker", null, "transactions", new String[] {"TABLE"});
            if(!resultSet.next()) {
                statement.executeUpdate(sql);
                System.out.println("Created table transactions in given database...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
