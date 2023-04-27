package com.example.expensetracker;

import com.example.expensetracker.Database.DatabaseConnection;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override

    public void start(Stage stage) throws IOException, SQLException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Views/home.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("home");
        stage.setScene(scene);
        stage.show();
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection connection = db.getConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}