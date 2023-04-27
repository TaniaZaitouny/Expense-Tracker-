package com.example.expensetracker;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class HelloApplication extends Application {

    Preferences prefs = Preferences.userRoot().node("com.example.app");

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        int userId=prefs.getInt("userId", 0);
        if(userId==0)
        {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Views/login.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("login");
        stage.setScene(scene);
        }
        else
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Views/home.fxml")));
            Scene scene = new Scene(root);
            stage.setTitle("user id = "+userId);
            stage.setScene(scene);
        }
        stage.show();
        stage.setOnCloseRequest(event -> {  //to be removed
            prefs.remove("userId");   // wil be added on logout button action
        });
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection connection = db.getConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}