package com.example.expensetracker.Threads;

import com.example.expensetracker.Controllers.ObserverController;
import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.Models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckAutomaticCategoriesThread extends Thread {
    private List<ObserverController> observers = new ArrayList<>();

    public void addListener(ObserverController listener) {
        observers.add(listener);
    }

    public void removeListener(ObserverController listener) {
        observers.remove(listener);
    }

    private void notifyListeners() {
        for (ObserverController observer : observers) {
            observer.notify(new ArrayList<>());
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
                String name = resultSet.getString("name");
                String frequency = resultSet.getString(5);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(resultSet.getDate(6));
                LocalDate lastTransactionDate = LocalDate.parse(dateString);
                double amount = resultSet.getDouble(7);
                LocalDate currentDate = LocalDate.now();
                Transaction transaction = new Transaction();
                switch (frequency) {
                    case "DAILY" -> {
                        if (lastTransactionDate.plusDays(1).isBefore(currentDate) || lastTransactionDate.plusDays(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                        }
                    }
                    case "WEEKLY" -> {
                        if (lastTransactionDate.plusWeeks(1).isBefore(currentDate) || lastTransactionDate.plusWeeks(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                        }
                    }
                    case "MONTHLY" -> {
                        if (lastTransactionDate.plusMonths(1).isBefore(currentDate) || lastTransactionDate.plusMonths(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                        }
                    }
                    case "YEARLY" -> {
                        if (lastTransactionDate.plusYears(1).isBefore(currentDate) || lastTransactionDate.plusYears(1).isEqual(currentDate)) {
                            transaction.addTransaction(currentDate, name, amount);
                        }
                    }
                    default -> System.out.println("Invalid frequency for category: " + name);
                }
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            System.out.println("failed to check categories");
        }
    }
}
