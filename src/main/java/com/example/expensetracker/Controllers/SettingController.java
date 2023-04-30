package com.example.expensetracker.Controllers;

import com.example.expensetracker.Models.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class SettingController {
    @FXML
    Button logoutButton;
    @FXML
    Button setButton;
    @FXML
    TextField monthlyBudget;
    @FXML
    Label error;
    public void initialize()
    {

        Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
        logoutButton.setOnAction(event-> {

             prefs.remove("userId");
             Stage stage = (Stage)logoutButton.getScene().getWindow();
            try {
                Scene scene = MenuController.loadPage("Views/login.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
    public void addSettings() throws SQLException {
        String s = monthlyBudget.getText();
        if(s.equals(""))
        {
            error.setText("Please enter a valid number");
        }
        {
            double monthly = Double.parseDouble(monthlyBudget.getText());
            User u = new User();
            u.updateUser(monthly);
            Stage stage = (Stage) setButton.getScene().getWindow();
            try {
                MenuController.loadPage("Views/categories.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
