package com.example.expensetracker.Filters.CategoryFilters;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Filters.CategoryFilters.CategoryFilter;
import com.example.expensetracker.Objects.CategoryObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CategoryAlphabeticalFilter implements CategoryFilter
{
    DatabaseConnection db  = DatabaseConnection.getInstance();
    Connection connection = db.getConnection();

    @Override
    public ArrayList<CategoryObject> filter(String order) throws SQLException {

        ResultSet resultSet;
        String sql = "SELECT * FROM categories where userId = '" + userId + "' ORDER BY categoryName ASC;";
        String sql2 = "SELECT * FROM categories where userId = '" + userId + "' ORDER BY categoryName DESC;";
        ArrayList<CategoryObject> categories = new ArrayList<>();
        Statement statement = connection.createStatement();

        if(order.equals("ascending"))
        {
            resultSet = statement.executeQuery(sql);
        }
        else {
            resultSet = statement.executeQuery(sql2);
        }
        while (resultSet.next()) {
            categories.add(new CategoryObject(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getDate(6), resultSet.getDouble(7)));
        }
        statement.close();
        return categories;
    }
}
