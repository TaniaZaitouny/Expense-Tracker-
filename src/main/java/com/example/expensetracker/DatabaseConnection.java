package com.example.expensetracker;

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
            createTables();
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
        createUsersTable();
        createCategoriesTable();
        createTransactionsTable();
    }

    private void createUsersTable() {
        String sql = "CREATE TABLE USERS " +
                "(id INTEGER not NULL, " +
                " user_name VARCHAR(255), " +
                " email VARCHAR(255), " +
                " PRIMARY KEY (id))";
        try {
            Statement statement = this.connection.createStatement();
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "USERS", new String[] {"TABLE"});
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
                "(id INTEGER not NULL, " +
                " category_name VARCHAR(255), " +
                " type VARCHAR(255), " +
                " PRIMARY KEY (id))";
        try {
            Statement statement = this.connection.createStatement();
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "CATEGORIES", new String[] {"TABLE"});
            if(!resultSet.next()) {
                statement.executeUpdate(sql);
                System.out.println("Created categories table in given database...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTransactionsTable() {
        String sql = "CREATE TABLE TRANSACTIONS " +
                "(id INTEGER not NULL, " +
                " date DATE, " +
                " amount DOUBLE, " +
                " category INTEGER not null, " +
                " PRIMARY KEY (id), " +
                " FOREIGN KEY (category) REFERENCES CATEGORIES(id))";
        try {
            Statement statement = this.connection.createStatement();
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "TRANSACTIONS", new String[] {"TABLE"});
            if(!resultSet.next()) {
                statement.executeUpdate(sql);
                System.out.println("Created table transactions in given database...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
