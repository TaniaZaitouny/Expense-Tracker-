package com.example.expensetracker.Controllers;

import com.example.expensetracker.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

    public static Scene loadPage(String pageName, Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(pageName)));
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/logo.png"))));
        stage.setScene(scene);
        stage.show();
        return scene;
    }

    @FXML
    protected void homePage() throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        Scene scene = MenuController.loadPage("Views/home.fxml", stage);
        homeButton.setStyle("-fx-background-color:   #FFFFFF; -fx-background-radius: 0 0 0 0; -fx-text-fill: #3A4D8F;");
    }
    @FXML
    protected void categoriesPage() throws IOException {
        Stage stage = (Stage) categoriesButton.getScene().getWindow();
        Scene scene = MenuController.loadPage("Views/categories.fxml", stage);
        categoriesButton.setStyle("-fx-background-color:   #FFFFFF; -fx-background-radius: 0 0 0 0; -fx-text-fill: #3A4D8F;");
    }
    @FXML
    protected void transactionsPage() throws IOException {
        Stage stage = (Stage) transactionsButton.getScene().getWindow();
        Scene scene = MenuController.loadPage("Views/transactions.fxml", stage);
        transactionsButton.setStyle("-fx-background-color:   #FFFFFF; -fx-background-radius: 0 0 0 0; -fx-text-fill: #3A4D8F;");
    }
    @FXML
    protected void reportsPage() throws IOException {
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        Scene scene = MenuController.loadPage("Views/reports.fxml", stage);
        reportsButton.setStyle("-fx-background-color:   #FFFFFF; -fx-background-radius: 0 0 0 0; -fx-text-fill: #3A4D8F;");
    }

    @FXML
    protected void logoutPage() throws IOException {
        Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
        prefs.remove("userId");
        Stage stage = (Stage)logoutButton.getScene().getWindow();
        Scene scene = MenuController.loadPage("Views/login.fxml", stage);
    }
}
