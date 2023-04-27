package com.example.expensetracker;

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

public class HelloApplication extends Application {
    @Override

    public void start(Stage stage) throws IOException, SQLException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("home");
        stage.setScene(scene);
        stage.show();
        // Get a reference to the VBox element in the home.fxml file
        VBox vbox = (VBox)scene.lookup("#home_category");

        // Add the mouse entered event listener to make the VBox glow
        vbox.setOnMouseEntered(e -> {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(15);
            dropShadow.setSpread(0.5);
            dropShadow.setColor(Color.LAVENDER);
            vbox.setEffect(dropShadow);
            vbox.setScaleX(1.1);
            vbox.setScaleY(1.1);
        });

        // Add the mouse exited event listener to revert the VBox back to its original style
        vbox.setOnMouseExited(e -> {
            vbox.setEffect(null);
            vbox.setScaleX(1.0);
            vbox.setScaleY(1.0);
        });
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection connection = db.getConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}