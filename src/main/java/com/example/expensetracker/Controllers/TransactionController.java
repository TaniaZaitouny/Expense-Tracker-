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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class TransactionController {

    @FXML
    Button addTransactionButton;
    @FXML
    Label pageTitle;
    @FXML
    Button submitButton;
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

    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    Transaction transaction = new Transaction();

    private static String transactionAmountToUpdate;
    private static String transactionCategoryToUpdate;
    private static String transactionIdToUpdate = null;
    private static String transactionDateToUpdate;

    public void initialize() throws SQLException, ParseException {
        if (transactionTable != null) {
            populateTransactions();
        }
        else {
            initializeCategoryList();
            if (transactionIdToUpdate != null) {
                pageTitle.setText("Update transaction");
                submitButton.setText("Update Transaction");
                transactionAmount.setText(transactionAmountToUpdate);
                transactionCategory.setValue(transactionCategoryToUpdate);
                LocalDate date = LocalDate.parse(transactionDateToUpdate);
                transactionDate.setValue(date);
            }
        }
    }
    @FXML
    protected void addTransactionPage() throws IOException {
        transactionIdToUpdate = null;
        Stage stage = (Stage)addTransactionButton.getScene().getWindow();
        MenuController.loadPage("Views/addTransaction.fxml",stage);
    }

    protected void addTransactionPage(String id, String amount, String date, String category) throws IOException {
        transactionIdToUpdate = id;
        transactionAmountToUpdate = amount;
        transactionCategoryToUpdate = category;
        transactionDateToUpdate = date;
        Stage stage = (Stage)addTransactionButton.getScene().getWindow();
        MenuController.loadPage("Views/addTransaction.fxml",stage);
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
    public void updateTransaction(ActionEvent actionEvent) throws SQLException, BackingStoreException {
        String action = ((Button) actionEvent.getSource()).getText();
        if(action.equals("Submit")) {
            addTransaction();
        }
        else {
            editTransaction();
        }
        transactionIdToUpdate = null;
    }

    public void addTransaction() throws SQLException {
        LocalDate date = transactionDate.getValue();
        String selectedCategory = transactionCategory.getValue();
        double amount = Double.parseDouble(transactionAmount.getText());
        if(date == null) {
            System.out.println("date null");
            return;
        }
        if(selectedCategory.equals("")) {
            System.out.println("category null");
            return;
        }

        Transaction transaction = new Transaction();
        transaction.addTransaction(date, selectedCategory, amount);
    }

    public void editTransaction() throws SQLException {
        LocalDate date = transactionDate.getValue();
        String selectedCategory = transactionCategory.getValue();
        double amount = Double.parseDouble(transactionAmount.getText());
        if(date == null) {
            System.out.println("date null");
            return;
        }
        if(selectedCategory.equals("")) {
            System.out.println("category null");
            return;
        }

        transaction.updateTransaction(Integer.parseInt(transactionIdToUpdate), date, selectedCategory, amount);
    }

    public void populateTransactions() throws SQLException {
        Transaction transaction = new Transaction();
        ArrayList<String[]> transactions = transaction.getTransactions();
//        hidden field to store id
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

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hb = new HBox();
                            hb.getChildren().addAll(editBtn, deleteBtn);
                            setGraphic(hb);
                            editBtn.setOnAction((ActionEvent event) -> {
                                String[] rowData = getTableRow().getItem();
                                try {
                                    addTransactionPage(rowData[3], rowData[2], rowData[1], rowData[0]);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            deleteBtn.setOnAction((ActionEvent event) -> {
                                String[] rowData = getTableRow().getItem();
                                try {
                                    transaction.deleteTransaction(Integer.parseInt(rowData[3]));
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                //reload after deleting
                                Stage stage = (Stage)deleteBtn.getScene().getWindow();
                                try {
                                    MenuController.loadPage("Views/transactions.fxml",stage);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                };
                return cell;
            }
        };
        actionColumn.setCellFactory(cellFactory);



    }
}
