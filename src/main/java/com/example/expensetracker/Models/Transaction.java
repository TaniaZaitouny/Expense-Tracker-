package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    Connection connection;

    public Transaction() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }

    public List<Pair<String, Number>> getTopCategories() {
        String sql = "SELECT categoryName, amount " +
                "FROM transactions " +
                "GROUP BY categoryName " +
                "ORDER BY amount desc " +
                "LIMIT 5";
        List<Pair<String, Number>> pairs = new ArrayList<>(5);
        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                pairs.add(new Pair<>(result.getString("columnName"), result.getDouble(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pairs;
    }

}
