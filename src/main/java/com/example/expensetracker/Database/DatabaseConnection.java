package com.example.expensetracker.Database;

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
                " user_name VARCHAR(255), " +
                " email VARCHAR(255), " +
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
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " category_name VARCHAR(255), " +
                " type VARCHAR(255), " +
                " PRIMARY KEY (id))";
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
                " date DATE, " +
                " amount DOUBLE, " +
                " category INTEGER not null, " +
                " PRIMARY KEY (id), " +
                " FOREIGN KEY (category) REFERENCES CATEGORIES(id))";
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

    public void addCategory(String name, String type) throws SQLException {
        String sqlQuery = "INSERT INTO categories (category_name, type) " +
                "VALUES ('" + name + "', '" + type + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlQuery);
    }

}