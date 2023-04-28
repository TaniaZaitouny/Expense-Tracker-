package com.example.expensetracker.Controllers;

import com.example.expensetracker.HelloApplication;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Models.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    private TableView<String[]> transactionTable;
    @FXML
    private TableColumn<String[], String> categoryColumn;
    @FXML
    private TableColumn<String[], String> dateColumn;
    @FXML
    private TableColumn<String[], String> amountColumn;
    @FXML
    private TableColumn<String[], Void> actionColumn;
    public void initialize() throws SQLException {
        if(transactionCategory != null) {
            initializeCategoryList();

        }

        if(transactionTable!= null)
        {

            populateTransactions();
        }

    }
    @FXML
    protected void addTransactionPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Views/addTransaction.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage)addTransactionButton.getScene().getWindow();
        stage.setTitle("addTransaction");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initializeCategoryList()
    {
        Category category = new Category();

        try {
            ArrayList<Pair<String,String> > results = category.getCategories();
            ArrayList<String> categories = new ArrayList<>();
            results.forEach(pair -> categories.add(pair.getKey()));
            transactionCategory.setItems(FXCollections.observableList(categories));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void addTransaction() throws SQLException {
        LocalDate date = transactionDate.getValue();
        String selectedCategory = transactionCategory.getValue();
        double amount = Double.parseDouble(transactionAmount.getText());
        if(date == null)
        {
            System.out.println("date null");
            return;
        }
        if(selectedCategory.equals(""))
        {
            System.out.println("date null");
            return;
        }

        Transaction transaction = new Transaction();
        transaction.addTransaction(date, selectedCategory, amount);
    }
    public void populateTransactions() throws SQLException {
        Transaction transaction = new Transaction();
        ArrayList<String[]> transactions = transaction.getTransactions();
        //hidden field to store id
        TableColumn<String[], String> idColumn = new TableColumn<>("ID");
        idColumn.setVisible(false);
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        amountColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        transactionTable.getColumns().add(idColumn);
        transactionTable.setItems(FXCollections.observableArrayList(transactions));


        Callback<TableColumn<String[], Void>, TableCell<String[], Void>> cellFactory = new Callback<TableColumn<String[], Void>, TableCell<String[], Void>>() {
            @Override
            public TableCell<String[], Void> call(final TableColumn<String[], Void> param) {
                final TableCell<String[], Void> cell = new TableCell<String[], Void>() {
                    private final Button editBtn = new Button("Edit");
                    private final Button deleteBtn = new Button("Delete");

                    {
                        editBtn.setOnAction((ActionEvent event) -> {
                            // handle edit button click event
                        });

                        deleteBtn.setOnAction((ActionEvent event) -> {

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hb = new HBox();
                            hb.getChildren().addAll(editBtn, deleteBtn);
                            setGraphic(hb);
                        }
                    }
                };
                return cell;
            }
        };
        actionColumn.setCellFactory(cellFactory);



    }
}
