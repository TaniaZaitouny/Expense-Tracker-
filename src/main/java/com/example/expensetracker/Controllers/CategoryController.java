package com.example.expensetracker.Controllers;

import com.example.expensetracker.HelloApplication;
import com.example.expensetracker.Models.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CategoryController
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
    Button addCategoryButton;
    @FXML
    ToggleButton incomeChoiceButton;
    @FXML
    ToggleButton expenseChoiceButton;
    @FXML
    Button submitButton;
    @FXML
    TextField categoryName;
    @FXML
    Label messageText;

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

    @FXML
    protected void addCategoryPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("addcategory.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)addCategoryButton.getScene().getWindow();
        stage.setTitle("addCategory");
        stage.setScene(scene);
        stage.show();
    }

    public void addCategory(ActionEvent actionEvent)
    {
        String name=categoryName.getText();
        String type;
        if(!incomeChoiceButton.isSelected() && !expenseChoiceButton.isSelected()){
            messageText.setText("PLease choose a type for category");
            return;
        }
        if(name.equals(""))
        {
            messageText.setText("PLease choose a name for category");
            return;
        }
        if(incomeChoiceButton.isSelected())
        {
            type="income";
            incomeChoiceButton.setSelected(false);
        }
        else {
            type="expense";
            expenseChoiceButton.setSelected(false);
        }
        Category category = new Category();
        category.addCategory(name,type);
        categoryName.setText("");
        messageText.setText("Category added successfully");
    }
}
