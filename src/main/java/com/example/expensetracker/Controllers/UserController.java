package com.example.expensetracker.Controllers;


import com.example.expensetracker.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class UserController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField validatePasswordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Label messageText;


   User login = new User();

    public void loginPage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = MenuController.loadPage("Views/login.fxml", stage);
    }

    public void signupPage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)signupButton.getScene().getWindow();
        Scene scene = MenuController.loadPage("Views/signup.fxml", stage);
    }

    public void signup(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String validatePassword = validatePasswordField.getText();
        if(username.equals("") || email.equals("") || password.equals("") || !password.equals(validatePassword)){
            messageText.setText("PLease fill information correctly");
        }
        else if(!login.signup(username, email, password)){
            messageText.setText("Error account is already taken");
        }
        else
        {
            Stage stage = (Stage) signupButton.getScene().getWindow();
            Scene scene = MenuController.loadPage("Views/home.fxml", stage);
        }
    }

    public void login(ActionEvent actionEvent) throws IOException, SQLException {
        String email = emailField.getText();
        String password = passwordField.getText();
        if(email.equals("") || password.equals("")){
            messageText.setText("PLease fill information correctly");
            return;
        }
        if(login.login(email, password))
        {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = MenuController.loadPage("Views/home.fxml", stage);
        }
    }



}
