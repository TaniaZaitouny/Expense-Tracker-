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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class MenuController {

    @FXML
    Button homeButton;
    @FXML
    Button categoriesButton;
    @FXML
    Button transactionsButton;
    @FXML
    Button reportsButton;
    @FXML
    Button logoutButton;

    @FXML
    protected void homePage() throws IOException {
        Stage stage = (Stage)homeButton.getScene().getWindow();
        Scene scene = HelloApplication.loadPage("Views/home.fxml",stage);
         //Get a reference to the VBox element in the home.fxml file

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
    }
    @FXML
    protected void categoriesPage() throws IOException {
        Stage stage = (Stage)categoriesButton.getScene().getWindow();
        Scene scene = HelloApplication.loadPage("Views/categories.fxml",stage);
    }
    @FXML
    protected void transactionsPage() throws IOException {
        Stage stage = (Stage)transactionsButton.getScene().getWindow();
        Scene scene = HelloApplication.loadPage("Views/transactions.fxml",stage);
    }
    @FXML
    protected void reportsPage() throws IOException {
        Stage stage = (Stage)reportsButton.getScene().getWindow();
        Scene scene = HelloApplication.loadPage("Views/reports.fxml",stage);
    }

    @FXML
    protected void logoutPage() throws IOException {
        Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
        prefs.remove("userId");
        Stage stage = (Stage)logoutButton.getScene().getWindow();
        Scene scene = HelloApplication.loadPage("Views/login.fxml",stage);
    }
}
