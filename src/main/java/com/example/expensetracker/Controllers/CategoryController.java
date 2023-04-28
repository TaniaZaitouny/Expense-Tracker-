package com.example.expensetracker.Controllers;

import com.example.expensetracker.HelloApplication;
import com.example.expensetracker.Models.Category;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

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

    Preferences prefs = Preferences.userRoot().node("com.example.expensetracker");
    Category category = new Category();
    private String previousCategoryName=prefs.get("name","");
    private String previousCategoryType=prefs.get("type","");
    private boolean addingMode=prefs.getBoolean("mode",true);
    public void initialize() throws SQLException {
        if(categoriesTable != null) {
            getCategories();
        }
        else
        {
            if (addingMode)
            {
                submitButton.setText("Add Category");
            }
            else
            {
                submitButton.setText("Update Category");
                categoryName.setText(previousCategoryName);
                if(previousCategoryType.equals("income"))
                {
                    incomeChoiceButton.setSelected(true);
                    expenseChoiceButton.setSelected(false);
                }
                else
                {
                    incomeChoiceButton.setSelected(false);
                    expenseChoiceButton.setSelected(true);
                }
            }

        }
    }

    @FXML
    protected void addCategoryPage() throws IOException {

        Stage stage = (Stage)addCategoryButton.getScene().getWindow();
        MenuController.loadPage("Views/addCategory.fxml",stage);
        prefs.putBoolean("mode",true);
    }

    public void updateCategory(ActionEvent actionEvent) throws SQLException, BackingStoreException {
        if(addingMode)
        {
            addCategory();
        }
        else {
            editCategory();
        }
        prefs.remove("mode");
        prefs.remove("name");
        prefs.remove("type");
    }


    private void addCategory() throws SQLException {
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
        if(category.addCategory(name,type)) {
            categoryName.setText("");
            //   messageText.setText("Category added successfully");
        }
        else {
            //   messageText.setText("Category already exists");
        }
    }

    private void editCategory() throws SQLException {
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
            category.updateCategory(previousCategoryName,name,type);
            categoryName.setText("");
            //   messageText.setText("Category updated successfully");

    }

    private void getCategories() throws SQLException {
        ArrayList<Pair<String,String>> categories = category.getCategories();
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        categoriesTable.setItems(FXCollections.observableArrayList(categories));

        actionsColumn.setCellFactory(col -> new TableCell<Pair<String,String>, String>() {
            private final Button editButton = new Button("edit");
            private final Button deleteButton = new Button("delete");
            private final HBox hbox = new HBox(editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Pair<String,String> rowData = getTableRow().getItem();
                    prefs.put("name",rowData.getKey());
                    prefs.put("type",rowData.getValue());
                    prefs.putBoolean("mode",false);
                    Stage stage = (Stage)editButton.getScene().getWindow();
                    try {
                        MenuController.loadPage("Views/addCategory.fxml",stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Pair<String,String> rowData = getTableRow().getItem();
                    String categoryName = rowData.getKey();
                    try {
                        category.deleteCategory(categoryName);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    //reload after deleting
                    Stage stage = (Stage)deleteButton.getScene().getWindow();
                    try {
                        MenuController.loadPage("Views/categories.fxml",stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(hbox);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

}
