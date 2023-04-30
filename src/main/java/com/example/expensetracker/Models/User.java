package com.example.expensetracker.Models;

import com.example.expensetracker.Database.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;

public class User {

    DatabaseConnection connection = DatabaseConnection.getInstance();

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = digest.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private boolean validateAccount(String email) throws SQLException {
        boolean valid= false;
        String sql = "SELECT * FROM users WHERE email = ?";
        PreparedStatement statement = connection.getConnection().prepareStatement(sql);
        statement.setString(1, email);

        ResultSet result = statement.executeQuery();

        if (result.next()) {
            valid = true;
        }
        result.close();
        statement.close();
        return valid;
    };

    private void saveId(int id)
    {
        Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
        prefs.putInt("userId", id);
    }

    public boolean login(String email, String password) throws SQLException {
        int userId;
        if(!validateAccount(email)) {
            return false;
        }
        String sql = "SELECT id FROM users WHERE email like ? AND password like ?";
        PreparedStatement statement = connection.getConnection().prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, hashPassword(password));
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            userId = result.getInt("id");
            result.close();
            statement.close();
            saveId(userId);
            System.out.println(userId);
            return true;
        }
        result.close();
        statement.close();
        return false;
    }

    public boolean signup(String username,String email, String password) throws SQLException {
        int userId;
        if(validateAccount(email))
        {
            System.out.println("found");
            return false;
        }
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, username);
        statement.setString(2, email);
        statement.setString(3, hashPassword(password));

        int rowsAffected = statement.executeUpdate();
        if (rowsAffected > 0) {
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                userId = result.getInt(1);
                result.close();
                statement.close();
                saveId(userId);
                System.out.println(userId);
                statement.close();
                return true;
            }
        }
        statement.close();
        System.out.println("shit"); //???????????????????????
        return false;
    }

    public void updateUser(double amount) throws SQLException {
        String sql = "UPDATE users SET monthlyBudget =  " + amount;
        Statement statement = connection.getConnection().createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

}
