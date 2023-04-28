package com.example.expensetracker.Controllers;

import com.example.expensetracker.HelloApplication;
import com.example.expensetracker.Models.Category;
import javafx.collections.FXCollections;
import java.util.Collection;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    ComboBox<String> iconComboBox;
    @FXML
    TableView<Pair<String,String>> categoriesTable;
    @FXML
    TableColumn<Pair<String,String>, String> categoryColumn;
    @FXML
    TableColumn<Pair<String,String>,String> typeColumn;
    @FXML
    TableColumn<Pair<String,String>, String> actionsColumn;

//    @FXML
//    Label messageText;

    Category category = new Category();

    public void initialize() throws SQLException {
        if(categoriesTable != null) {
            getCategories();
        }
        else {
            final File folder = new File("src/main/resources/com/example/expensetracker/Media");
            final File[] files = folder.listFiles();
            String fileName, nameWithoutExtension;
            List<String> iconNames = new ArrayList<>();
            int extensionIndex;
            for (File file : files) {
                fileName = file.getName();
                extensionIndex = fileName.lastIndexOf(".");
                nameWithoutExtension = extensionIndex == -1 ? fileName : fileName.substring(0, extensionIndex);
                iconNames.add(nameWithoutExtension);
            }
            iconComboBox.setItems(FXCollections.observableArrayList(iconNames));
            iconComboBox.setCellFactory(listView -> new ListCell<>() {
                private final ImageView imageView = new ImageView();
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        Image image = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("Media/" + item + ".png")));
                        imageView.setImage(image);
                        imageView.setFitWidth(32);
                        imageView.setFitHeight(32);
                        setGraphic(imageView);
                    }
                }
            });
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

