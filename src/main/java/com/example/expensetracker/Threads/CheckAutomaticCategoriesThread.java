package com.example.expensetracker.Threads;

import com.example.expensetracker.Controllers.ObserverController;
import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Models.Transaction;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class CheckAutomaticCategoriesThread extends Thread {
    private List<ObserverController> observers = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    public void registerObserver(ObserverController observer) {
        observers.add(observer);
    }

    public void unregisterObserver(ObserverController observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (ObserverController observer : observers) {
            observer.getNotified();
        }
    }

    @Override
    public void run() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            Connection connection = db.getConnection();
            String query = "SELECT * FROM categories WHERE frequency != 'NEVER'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("categoryName");
                String frequency = resultSet.getString(5);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(resultSet.getDate(6));
                LocalDate lastTransactionDate = LocalDate.parse(dateString);
                double amount = resultSet.getDouble(7);
                LocalDate currentDate = LocalDate.now();
                java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
                Transaction transaction = new Transaction();
                Category category = new Category();
                switch (frequency) {
                    case "DAILY" -> {
                        if (lastTransactionDate.plusDays(1).isBefore(currentDate) || lastTransactionDate.plusDays(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                            category.updateCategory(name, name, resultSet.getString(3), resultSet.getString(4), frequency, sqlDate, amount);
                        }
                    }
                    case "WEEKLY" -> {
                        if (lastTransactionDate.plusWeeks(1).isBefore(currentDate) || lastTransactionDate.plusWeeks(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                            category.updateCategory(name, name, resultSet.getString(3), resultSet.getString(4), frequency, sqlDate, amount);
                        }
                    }
                    case "MONTHLY" -> {
                        if (lastTransactionDate.plusMonths(1).isBefore(currentDate) || lastTransactionDate.plusMonths(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                            category.updateCategory(name, name, resultSet.getString(3), resultSet.getString(4), frequency, sqlDate, amount);
                        }
                    }
                    case "YEARLY" -> {
                        if (lastTransactionDate.plusYears(1).isBefore(currentDate) || lastTransactionDate.plusYears(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                            category.updateCategory(name, name, resultSet.getString(3), resultSet.getString(4), frequency, sqlDate, amount);
                        }
                    }
                    default -> System.out.println("Invalid frequency for category: " + name);
                }
            }
            resultSet.close();
            statement.close();
            notifyObservers();
        }
        catch (SQLException e) {
            System.out.println("failed to check categories");
        }
    }
}
