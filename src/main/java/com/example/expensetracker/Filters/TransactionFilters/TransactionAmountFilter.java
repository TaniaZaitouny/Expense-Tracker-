package com.example.expensetracker.Filters.TransactionFilters;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Objects.TransactionObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TransactionAmountFilter implements TransactionFilter
{
    DatabaseConnection db  = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();

    @Override
    public ArrayList<TransactionObject> filter(String filterType) throws SQLException {

        String sql;

        if(filterType.equals("ascending"))
        {
            sql = "SELECT * FROM transactions where userId = '" + userId + "' ORDER BY amount ASC;";
        }
        else {
            sql = "SELECT * FROM transactions where userId = '" + userId + "' ORDER BY amount DESC;";
        }
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
