package com.example.expensetracker.Controllers;

import com.example.expensetracker.Models.Category;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
    @FXML private TableView<Pair<String,String>> categoriesTable;
    @FXML private TableColumn<Pair<String,String>, String> categoryColumn;
    @FXML private TableColumn<Pair<String,String>,String> typeColumn;
    @FXML private TableColumn<Pair<String,String>, String> actionsColumn;

//    @FXML
//    Label messageText;

    Category category = new Category();

    public void initialize() throws SQLException {
        if(categoriesTable != null) {
            getCategories();
        }
    }

    @FXML
    protected void addCategoryPage() throws IOException {
        Stage stage = (Stage)addCategoryButton.getScene().getWindow();
        MenuController.loadPage("Views/addCategory.fxml",stage);
    }

    public void addCategory(ActionEvent actionEvent) throws SQLException {
        String name=categoryName.getText();
        String type;
        if(!incomeChoiceButton.isSelected() && !expenseChoiceButton.isSelected()){
           // messageText.setText("PLease choose a type for category");
            return;
        }
        if(name.equals(""))
        {
           // messageText.setText("PLease choose a name for category");
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

        category.addCategory(name,type);
        categoryName.setText("");
     //   messageText.setText("Category added successfully");
    }

    private void getCategories() throws SQLException {
        ArrayList<Pair<String,String>> categories = category.getCategories();
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        categoriesTable.setItems(FXCollections.observableArrayList(categories));
    }


}
