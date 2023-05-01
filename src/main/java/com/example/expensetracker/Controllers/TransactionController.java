package com.example.expensetracker.Controllers;

import com.example.expensetracker.Filters.CategoryFilters.*;
import com.example.expensetracker.Filters.TransactionFilters.*;
import com.example.expensetracker.Main;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Models.Transaction;
import com.example.expensetracker.Objects.CategoryObject;
import com.example.expensetracker.Objects.TransactionObject;
import com.example.expensetracker.Threads.CheckAutomaticCategoriesThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class TransactionController implements ObserverController {
    @FXML
    ChoiceBox<String> filters;
    @FXML
    ChoiceBox<String> filtersType;
    @FXML
    Button addTransactionButton;
    @FXML
    Label pageTitle;
    @FXML
    Label messageText;
    @FXML
    Button submitButton;
    @FXML
    DatePicker transactionDate;
    @FXML
    ChoiceBox<String> transactionCategory;
    @FXML
    TextField transactionAmount;
    @FXML
    TableView<TransactionObject> transactionTable;
    @FXML
    TableColumn<TransactionObject, String> categoryColumn;
    @FXML
    TableColumn<TransactionObject, String> dateColumn;
    @FXML
    TableColumn<TransactionObject, String> amountColumn;
    @FXML
    TableColumn<TransactionObject, Void> actionColumn;

    Transaction transaction = new Transaction();
    private TransactionObject transactionToUpdate;
    private static int transactionToUpdateId = 0;

    public void initialize() throws SQLException {
        CheckAutomaticCategoriesThread thread = new CheckAutomaticCategoriesThread();
        thread.registerObserver(this);
        thread.start();
        if (transactionTable != null) {
            if(!transaction.checkCategories()) {
                addTransactionButton.setManaged(false);
                addTransactionButton.setVisible(false);
                filters.setManaged(false);
                filters.setVisible(false);
                return;
            }
            addTransactionButton.setManaged(true);
            addTransactionButton.setVisible(true);
            filters.setManaged(true);
            filters.setVisible(true);

            filterTransactions();
        }
        else {
            initializeCategoryList(transactionCategory);
            if (transactionToUpdateId == 0) {
                submitButton.setText("Add Transaction");
            }
            else {
                transactionToUpdate = transaction.getTransaction(transactionToUpdateId);
                pageTitle.setText("Update Transaction");
                submitButton.setText("Update");
                transactionAmount.setText(String.valueOf(transactionToUpdate.amount));
                transactionCategory.setValue(transactionToUpdate.category);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(transactionToUpdate.date);
                transactionDate.setValue(LocalDate.parse(dateString));
            }
        }
    }

    @FXML
    protected void addTransactionPage() throws IOException{
        transactionToUpdateId = 0;
        Stage stage = (Stage) addTransactionButton.getScene().getWindow();
        MenuController.loadPage("Views/addTransaction.fxml", stage);
    }

    @FXML
    public void initializeCategoryList(ChoiceBox<String> toFill) {
        Category category = new Category();

        try {
            ArrayList<CategoryObject> results = category.getCategories(new CategoryNormalFilter(),"");
            ArrayList<String> categories = new ArrayList<>();
            results.forEach(pair -> categories.add(pair.categoryName));
            toFill.setItems(FXCollections.observableList(categories));
            if(transactionTable != null) {
                toFill.setValue(categories.get(0));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateTransaction() throws SQLException{
        LocalDate date = transactionDate.getValue();
        String selectedCategory = transactionCategory.getValue();
        double amount;

        try {
            amount = Double.parseDouble(transactionAmount.getText());
        }
        catch (NumberFormatException e) {
            messageText.setText("Please enter correct amount value!");
            return;
        }

        if(date == null) {
            messageText.setText("Please choose a date!");
            return;
        }

        if(selectedCategory.equals("No Category")) {
            messageText.setText("Please choose a category!");
            return;
        }

        if(transactionToUpdateId == 0) {
            addTransaction(date,selectedCategory,amount);
        }
        else {
            editTransaction(date,selectedCategory,amount);
        }

        Stage stage = (Stage) submitButton.getScene().getWindow();

        try {
            MenuController.loadPage("Views/transactions.fxml", stage);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTransaction(LocalDate date, String selectedCategory, double amount) throws SQLException {
        transaction.addTransaction(date, selectedCategory, amount);
    }

    public void editTransaction(LocalDate date, String selectedCategory, double amount) throws SQLException {
        transaction.updateTransaction(transactionToUpdateId, date, selectedCategory, amount);
    }

    public void populateTransactions(TransactionFilter Filter, String filterType) throws SQLException {
        transaction = new Transaction();
        ArrayList<TransactionObject> transactions = transaction.getTransactions(Filter, filterType);
        TableColumn<TransactionObject, String> idColumn = new TableColumn<>("ID");
        idColumn.setVisible(false);
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().id)));
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().category));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().date)));
        amountColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().amount)));
        transactionTable.getColumns().add(idColumn);
        transactionTable.setItems(FXCollections.observableArrayList(transactions));

        Callback<TableColumn<TransactionObject, Void>, TableCell<TransactionObject, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<TransactionObject, Void> call(final TableColumn<TransactionObject, Void> param) {
                final TableCell<TransactionObject, Void> cell = new TableCell<TransactionObject, Void>() {
                    private final Button editBtn = new Button("Edit");
                    private final Button deleteBtn = new Button("Delete");

                    {
                        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/Actions/edit.png"))));
                        editIcon.setFitHeight(16);
                        editIcon.setFitWidth(16);
                        editBtn.setStyle("-fx-background-color: transparent;");
                        editBtn.setGraphic(editIcon);

                        ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/Actions/delete.png"))));
                        deleteIcon.setFitHeight(16);
                        deleteIcon.setFitWidth(16);
                        deleteBtn.setStyle("-fx-background-color: transparent;");
                        deleteBtn.setGraphic(deleteIcon);
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
                            editBtn.setOnAction((ActionEvent event) -> {
                                TransactionObject rowData = getTableRow().getItem();
                                transactionToUpdateId = rowData.id;
                                try {
                                    Stage stage = (Stage) editBtn.getScene().getWindow();
                                    MenuController.loadPage("Views/addTransaction.fxml", stage);
                                }
                                catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            deleteBtn.setOnAction((ActionEvent event) -> {
                                TransactionObject rowData = getTableRow().getItem();
                                try {
                                    transaction.deleteTransaction(rowData.id);
                                }
                                catch (SQLException e) {
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


    public void filterTransactions() throws SQLException {
        String filter = filters.getValue();
        switch (filter) {
            case "All" -> {
                filtersType.setVisible(false);
                filtersType.setManaged(false);
                populateTransactions(new TransactionNormalFilter(), "");
                return;
            }
            case "Date" -> setTypeList("date");
            case "Category" -> setTypeList("category");
            case "Amount" -> setTypeList("amount");
        }
        filtersType.setVisible(true);
        filtersType.setManaged(true);
    }

    private void setTypeList(String Filter)
    {
        ObservableList<String> items = filtersType.getItems();
        items.clear();

        switch (Filter) {
            case "date" -> {
                items.add("Recent");
                items.add("Oldest");
                filtersType.setValue("Recent");
            }
            case "category" -> initializeCategoryList(filtersType);
            case "amount" -> {
                items.add("Ascending");
                items.add("Descending");
                filtersType.setValue("Ascending");
            }
        }
    }


    public void filterTransactionTypes() throws SQLException {
        String filter = filtersType.getValue();
        if(filter == null) return;
        switch (filter) {
            case "Recent" -> populateTransactions(new TransactionDateFilter(), "recent");
            case "Oldest" -> populateTransactions(new TransactionDateFilter(), "oldest");
            case "Ascending" -> populateTransactions(new TransactionAmountFilter(), "ascending");
            case "Descending" -> populateTransactions(new TransactionAmountFilter(), "descending");
            default -> populateTransactions(new TransactionCategoryFilter(), filter);
        }
    }

    @Override
    public void getNotified() {
        if(transactionTable != null) {
            new TransactionController();
        }
    }
}
