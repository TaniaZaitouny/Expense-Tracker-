package com.example.expensetracker.Controllers;

import com.example.expensetracker.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ReportsController
{
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("home.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)homeButton.getScene().getWindow();
        stage.setTitle("home");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void categoriesPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("categories.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)categoriesButton.getScene().getWindow();
        stage.setTitle("categories");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void transactionsPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("transactions.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)transactionsButton.getScene().getWindow();
        stage.setTitle("transactions");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void reportsPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("reports.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)reportsButton.getScene().getWindow();
        stage.setTitle("reports");
        stage.setScene(scene);
        stage.show();
    }
}
