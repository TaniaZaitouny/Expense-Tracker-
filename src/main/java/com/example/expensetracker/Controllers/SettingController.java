package com.example.expensetracker.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class SettingController {
    @FXML
    Button logoutButton;
    public void initialize()
    {
        logoutButton.setOnAction(event-> {
             Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
             prefs.remove("userId");
             Stage stage = (Stage)logoutButton.getScene().getWindow();
            try {
                Scene scene = MenuController.loadPage("Views/login.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
