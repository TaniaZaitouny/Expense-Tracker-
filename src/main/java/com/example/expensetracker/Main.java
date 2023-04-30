package com.example.expensetracker;

import com.example.expensetracker.Controllers.MenuController;
import com.example.expensetracker.Database.DatabaseConnection;
import javafx.application.Application;

import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {

    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");

    @Override
    public void start(Stage stage) throws IOException {
        int userId = prefs.getInt("userId", 0);
        if(userId == 0) {
            MenuController.loadPage("Views/login.fxml", stage);
        }
        else {
            MenuController.loadPage("Views/home.fxml", stage);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}