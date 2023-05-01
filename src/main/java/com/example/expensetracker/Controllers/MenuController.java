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

    static String homeStyle = "-fx-background-color: #2D3B64;";
    static String categoryStyle = null;
    static String transactionStyle = null;
    static String reportsStyle = null;

    public static Scene loadPage(String pageName, Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(pageName)));
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/Menu/logo.png"))));
        stage.setScene(scene);
        stage.show();
        return scene;
    }

    public void initialize() {
        homeButton.setStyle(homeStyle);
        categoriesButton.setStyle(categoryStyle);
        transactionsButton.setStyle(transactionStyle);
        reportsButton.setStyle(reportsStyle);
    }

    @FXML
    protected void homePage() throws IOException {
        homeStyle = "-fx-background-color: #2D3B64;";
        categoryStyle = "";
        transactionStyle = "";
        reportsStyle = "";
        Stage stage = (Stage) homeButton.getScene().getWindow();
        MenuController.loadPage("Views/home.fxml", stage);
    }

    @FXML
    protected void categoriesPage() throws IOException {
        homeStyle = "";
        categoryStyle = "-fx-background-color: #2D3B64;";
        transactionStyle = "";
        reportsStyle = "";
        Stage stage = (Stage) categoriesButton.getScene().getWindow();
        MenuController.loadPage("Views/categories.fxml", stage);
        categoriesButton.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 0 0 0 0; -fx-text-fill: #3A4D8F;");
    }

    @FXML
    protected void transactionsPage() throws IOException {
        homeStyle = "";
        categoryStyle = "";
        transactionStyle = "-fx-background-color: #2D3B64;";
        reportsStyle = "";
        Stage stage = (Stage) transactionsButton.getScene().getWindow();
        MenuController.loadPage("Views/transactions.fxml", stage);
    }

    @FXML
    protected void reportsPage() throws IOException {
        homeStyle = "";
        categoryStyle = "";
        transactionStyle = "";
        reportsStyle = "-fx-background-color: #2D3B64;";
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        MenuController.loadPage("Views/reports.fxml", stage);
    }

    @FXML
    protected void logout() {
        Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
        prefs.remove("userId");
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        try {
            MenuController.loadPage("Views/login.fxml", stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
