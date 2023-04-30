package com.example.expensetracker.Filters;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Objects.CategoryObject;
import com.example.expensetracker.Objects.TransactionObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TransactionCategoryFilter implements TransactionFilter
{
    DatabaseConnection db  = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();

    @Override
    public ArrayList<TransactionObject> filter(String category) throws SQLException {
        String sql = "SELECT * FROM transactions where userId = '" + userId + "' AND category = '" + category + "';";
        ArrayList<TransactionObject> transactions = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            transactions.add(new TransactionObject(resultSet.getInt(1), resultSet.getInt(2), resultSet.getDate(3), resultSet.getDouble(4), resultSet.getString(5)));
        }
        statement.close();
        return transactions;
    }
}
