package com.example.expensetracker.Controllers;

import com.example.expensetracker.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class HomeController {

    @FXML
    Button homeButton;
    @FXML
    Button categoriesButton;
    @FXML
    Button transactionsButton;
    @FXML
    Button reportsButton;

    @FXML
    protected void homePage() throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/home.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)homeButton.getScene().getWindow();
        stage.setTitle("home");
        stage.setScene(scene);
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
        stage.show();
    }
    @FXML
    protected void categoriesPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/categories.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)categoriesButton.getScene().getWindow();
        stage.setTitle("categories");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void transactionsPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/transactions.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)transactionsButton.getScene().getWindow();
        stage.setTitle("transactions");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void reportsPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/reports.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)reportsButton.getScene().getWindow();
        stage.setTitle("reports");
        stage.setScene(scene);
        stage.show();
    }

}