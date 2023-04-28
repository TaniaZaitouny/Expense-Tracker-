package com.example.expensetracker;

import com.example.expensetracker.Controllers.MenuController;
import com.example.expensetracker.Database.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;

import java.io.IOException;
import java.util.prefs.Preferences;

public class HelloApplication extends Application {

    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        stage.initStyle(StageStyle.UNDECORATED);

        int userId = prefs.getInt("userId", 0);
        if(userId == 0) {
            MenuController.loadPage("Views/login.fxml", stage);
        }
        else {
            MenuController.loadPage("Views/home.fxml", stage);
        }


        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection connection = db.getConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}