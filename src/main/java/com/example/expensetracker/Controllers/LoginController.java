package com.example.expensetracker.Controllers;


import com.example.expensetracker.HelloApplication;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Models.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField validatePasswordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Label messageText;


   Login login = new Login();

    public void loginPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/login.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)loginButton.getScene().getWindow();
        stage.setTitle("login");
        stage.setScene(scene);
        stage.show();
    }

    public void signupPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/signup.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)signupButton.getScene().getWindow();
        stage.setTitle("signup");
        stage.setScene(scene);
        stage.show();
    }

    public void signup(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String validatePassword = validatePasswordField.getText();
        if(username.equals("") || email.equals("") || password.equals("") || !password.equals(validatePassword)){
            messageText.setText("PLease fill information correctly");
        }
        else if(!login.signup(username,email,password)){
            messageText.setText("Error account is already taken");
        }
        else
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/home.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage)signupButton.getScene().getWindow();
            stage.setTitle("home");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void login(ActionEvent actionEvent) throws IOException, SQLException {
        String email = emailField.getText();
        String password = passwordField.getText();
        if(email.equals("") || password.equals("")){
            messageText.setText("PLease fill information correctly");
            return;
        }
        if(login.login(email,password))
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/home.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage)loginButton.getScene().getWindow();
            stage.setTitle("home");
            stage.setScene(scene);
            stage.show();
        }
    }


}