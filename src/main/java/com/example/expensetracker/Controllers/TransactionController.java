package com.example.expensetracker.Controllers;

import com.example.expensetracker.HelloApplication;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Models.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TransactionController {

    @FXML
    Button addTransactionButton;
    @FXML
    DatePicker transactionDate;
    @FXML
    ChoiceBox<String> transactionCategory;
    @FXML
    TextField transactionAmount;

    @FXML
    protected void addTransactionPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/addTransaction.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)addTransactionButton.getScene().getWindow();
        stage.setTitle("addTransaction");
        stage.setScene(scene);
        stage.show();
        new TransactionController();
    }

    @FXML
    public void initializeCategoryList()
    {
        Category category = new Category();

        try {
            ArrayList<Pair<String,String> > results = category.getCategories();
            ArrayList<String> categories=new ArrayList<>();
            results.forEach(pair -> categories.add(pair.getKey()));
            transactionCategory.setItems(FXCollections.observableList(categories));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void addTransaction() throws SQLException {
        LocalDate date = (LocalDate) transactionDate.getValue();
        String selectedCategory = transactionCategory.getValue();
        Double amount;
        if(date ==null)
        {
            return;
        }
        if(selectedCategory.equals(""))
        {
            return;
        }
        try{
            amount  = Double.parseDouble(transactionAmount.getText());
        }
        catch (NumberFormatException e){
            return;
        }
        Transaction transaction = new Transaction();
        transaction.addTransaction(date, selectedCategory, amount);
    }

}
