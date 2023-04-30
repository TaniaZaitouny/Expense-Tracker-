package com.example.expensetracker.Controllers;

import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Models.Transaction;
import com.example.expensetracker.Objects.CategoryObject;
import com.example.expensetracker.Objects.TransactionObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.prefs.BackingStoreException;

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
    private TableView<TransactionObject> transactionTable;
    @FXML
    private TableColumn<TransactionObject, String> categoryColumn;
    @FXML
    private TableColumn<TransactionObject, String> dateColumn;
    @FXML
    private TableColumn<TransactionObject, String> amountColumn;
    @FXML
    private TableColumn<TransactionObject, Void> actionColumn;

    Transaction transaction = new Transaction();

    private TransactionObject transactionToUpdate;
    private static int transactionToUpdateId = 0;
    public void initialize() throws SQLException, ParseException {
        if (transactionTable != null) {
            populateTransactions();
        }
        else {
            initializeCategoryList();
            if (transactionToUpdateId == 0) {
                submitButton.setText("Add Transaction");
            }
            else {
                transactionToUpdate = transaction.getTransaction(transactionToUpdateId);
                pageTitle.setText("Update Transaction");
                submitButton.setText("Update");
                transactionAmount.setText(transactionToUpdate.amount + "");
                transactionCategory.setValue(transactionToUpdate.category);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(transactionToUpdate.date);
               transactionDate.setValue(LocalDate.parse(dateString));
            }
        }
    }
    @FXML
    protected void addTransactionPage() throws IOException {
        transactionToUpdateId = 0;
        Stage stage = (Stage) addTransactionButton.getScene().getWindow();
        MenuController.loadPage("Views/addTransaction.fxml",stage);
    }

    @FXML
    public void initializeCategoryList()
    {
        Category category = new Category();

        try {

            ArrayList<CategoryObject> results = category.getCategories();

            ArrayList<String> categories = new ArrayList<>();
            results.forEach(pair -> categories.add(pair.categoryName));
            transactionCategory.setItems(FXCollections.observableList(categories));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateTransaction(ActionEvent actionEvent) throws SQLException, BackingStoreException {
        if(transactionToUpdateId == 0) {
            addTransaction();
        }
        else {
            editTransaction();
        }
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

        transaction.updateTransaction(transactionToUpdateId, date, selectedCategory, amount);
    }

    public void populateTransactions() throws SQLException {
        transaction = new Transaction();
        ArrayList<TransactionObject> transactions = transaction.getTransactions();
//        hidden field to store id
        TableColumn<TransactionObject, String> idColumn = new TableColumn<>("ID");
        idColumn.setVisible(false);
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().id+""));
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().category));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().date+""));
        amountColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().amount+""));
        transactionTable.getColumns().add(idColumn);
        transactionTable.setItems(FXCollections.observableArrayList(transactions));


        Callback<TableColumn<TransactionObject, Void>, TableCell<TransactionObject, Void>> cellFactory = new Callback<TableColumn<TransactionObject, Void>, TableCell<TransactionObject, Void>>() {
            @Override
            public TableCell<TransactionObject, Void> call(final TableColumn<TransactionObject, Void> param) {
                final TableCell<TransactionObject, Void> cell = new TableCell<TransactionObject, Void>() {
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
                                TransactionObject rowData = getTableRow().getItem();
                                transactionToUpdateId =rowData.id;
                                try {
                                    Stage stage = (Stage) editBtn.getScene().getWindow();
                                    MenuController.loadPage("Views/addTransaction.fxml", stage);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            deleteBtn.setOnAction((ActionEvent event) -> {
                                TransactionObject rowData = getTableRow().getItem();
                                try {
                                    transaction.deleteTransaction(rowData.id);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                //reload after deleting
                                Stage stage = (Stage) deleteBtn.getScene().getWindow();
                                try {
                                    MenuController.loadPage("Views/transactions.fxml", stage);
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
