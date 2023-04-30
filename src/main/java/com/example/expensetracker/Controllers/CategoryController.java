package com.example.expensetracker.Controllers;

import com.example.expensetracker.Filters.*;
import com.example.expensetracker.Main;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Objects.CategoryObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.prefs.BackingStoreException;


public class CategoryController {
    @FXML
    ChoiceBox<String> filters;
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
    Label pageTitle;
    @FXML
    TableView<CategoryObject> categoriesTable;
    @FXML
    CheckBox automatic;
    @FXML
    ChoiceBox<String> frequency;
    @FXML
    ComboBox<String> categoryIcon;
    @FXML
    TextField amount;
    @FXML
    TableColumn<CategoryObject, String> categoryColumn;
    @FXML
    TableColumn<CategoryObject,String> typeColumn;
    @FXML
    TableColumn<CategoryObject, Void> actionsColumn;
    @FXML
    Label messageText;

    Category category = new Category();
    private static String categoryNameToUpdate = null;
    private CategoryObject categoryToUpdate;
    public void initialize() throws SQLException {
        if(categoriesTable != null) {
            filterCategories(null);
        }
        else
        {
            if(categoryNameToUpdate != null) {
                categoryToUpdate = category.getCategory(categoryNameToUpdate);
                pageTitle.setText("Update Category");
                submitButton.setText("Update");
                categoryName.setText(categoryToUpdate.categoryName);
                categoryIcon.setValue(categoryToUpdate.categoryIcon);
                if(categoryToUpdate.categoryType.equals("income")) {
                    incomeChoiceButton.setSelected(true);
                    expenseChoiceButton.setSelected(false);
                    expenseChoiceButton.setStyle("-fx-background-color: #E1E5F2; -fx-text-fill: #3A4D8F;");
                    incomeChoiceButton.setStyle("-fx-background-color:  #3A4D8F; -fx-text-fill: #FFFFFF;");
                }
                else {
                    incomeChoiceButton.setSelected(false);
                    expenseChoiceButton.setSelected(true);
                    incomeChoiceButton.setStyle("-fx-background-color: #E1E5F2; -fx-text-fill: #3A4D8F;");
                    expenseChoiceButton.setStyle("-fx-background-color:  #3A4D8F; -fx-text-fill: #FFFFFF;");
                }
                if(!categoryToUpdate.frequency.equals("NEVER")) {
                    automatic.setSelected(true);
                    frequency.setVisible(true);
                    amount.setVisible(true);
                    frequency.setValue(categoryToUpdate.frequency);
                    amount.setText(String.valueOf(categoryToUpdate.amount));
                }
            }

            final File folder = new File("src/main/resources/com/example/expensetracker/Media");
            final File[] files = folder.listFiles();
            String fileName, nameWithoutExtension;
            List<String> iconNames = new ArrayList<>();
            int extensionIndex;
            assert files != null;
            for (File file : files) {
                fileName = file.getName();
                extensionIndex = fileName.lastIndexOf(".");
                nameWithoutExtension = extensionIndex == -1 ? fileName : fileName.substring(0, extensionIndex);
                iconNames.add(nameWithoutExtension);
            }
            categoryIcon.setItems(FXCollections.observableArrayList(iconNames));
            categoryIcon.setCellFactory(listView -> new ListCell<>() {
                private final ImageView imageView = new ImageView();
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/" + item + ".png")));
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
        categoryNameToUpdate = null;
        Stage stage = (Stage)addCategoryButton.getScene().getWindow();
        MenuController.loadPage("Views/addCategory.fxml",stage);
    }

    public void updateCategory(ActionEvent actionEvent) throws SQLException, BackingStoreException {
        String name = categoryName.getText();
        String type;
        String icon = categoryIcon.getValue();

        if(!incomeChoiceButton.isSelected() && !expenseChoiceButton.isSelected()) {
             messageText.setText("PLease choose a type for category");
            return;
        }

        if(name.equals("")) {
             messageText.setText("PLease choose a name for category");
            return;
        }

        if(incomeChoiceButton.isSelected()) {
            type = "income";
            incomeChoiceButton.setSelected(false);
        }
        else {
            type = "expense";
            expenseChoiceButton.setSelected(false);
        }

        if(automatic.isSelected()) {
            String frequencyValue = frequency.getValue();
            double amountValue = Double.parseDouble(amount.getText());
            if(categoryNameToUpdate == null) {
                addCategory(name, type, icon, frequencyValue, amountValue);
            }
            else {
                editCategory(name, type, icon, frequencyValue, amountValue);
            }
        }
        else {
            categoryName.setText("");
            messageText.setText("Category added successfully");
            if(categoryNameToUpdate == null) {
                addCategory(name, type, icon);
            }
            else {
                editCategory(name, type, icon);
            }
        }
    }

    private void addCategory(String name, String type, String icon) throws SQLException
    {
        if(category.addCategory(name, type, icon)) {
            categoryName.setText("");
            messageText.setText("Category added successfully");
        }
        else {
            messageText.setText("Category already exists");
        }
    }

    private void addCategory(String name, String type, String icon, String frequencyValue, double amountValue) throws SQLException
    {
        if(category.addCategory(name, type, icon, frequencyValue, amountValue)) {
            categoryName.setText("");
            messageText.setText("Category added successfully");
        }
        else {
            messageText.setText("Category already exists");
        }
    }

    private void editCategory(String name, String type, String icon) throws SQLException
    {
        category.updateCategory(categoryToUpdate.categoryName, name, type, icon);
        categoryName.setText("");
        messageText.setText("Category updated successfully");

    }

    private void editCategory(String name, String type, String icon, String frequencyValue, double amountValue) throws SQLException
    {
        category.updateCategory(categoryToUpdate.categoryName, name, type, icon, frequencyValue, amountValue);
        categoryName.setText("");
        messageText.setText("Category updated successfully");
    }

    private void getCategories(CategoryFilter Filter, String filterType) throws SQLException
    {
        ArrayList<CategoryObject> categories = category.getCategories(Filter, filterType);
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().categoryName));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().categoryType));
        categoriesTable.setItems(FXCollections.observableArrayList(categories));
        Callback<TableColumn<CategoryObject, Void>, TableCell<CategoryObject, Void>> cellFactory = new Callback<TableColumn<CategoryObject, Void>, TableCell<CategoryObject, Void>>() {
            @Override

            public TableCell<CategoryObject, Void> call(final TableColumn<CategoryObject, Void> param) {
                final TableCell<CategoryObject, Void> cell = new TableCell<CategoryObject, Void>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");


                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            HBox hb = new HBox();
                            hb.getChildren().addAll(editButton, deleteButton);
                            setGraphic(hb);
                            editButton.setOnAction(event -> {
                                CategoryObject rowData = getTableRow().getItem();
                                categoryNameToUpdate = rowData.categoryName;
                                try {
                                    Stage stage = (Stage) editButton.getScene().getWindow();
                                    MenuController.loadPage("Views/addCategory.fxml", stage);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            deleteButton.setOnAction(event -> {
                                CategoryObject rowData = getTableRow().getItem();
                                String categoryName = rowData.categoryName;
                                try {
                                    category.deleteCategory(categoryName);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                //reload after deleting
                                Stage stage = (Stage) deleteButton.getScene().getWindow();
                                try {
                                    MenuController.loadPage("Views/categories.fxml", stage);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                        else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        };
        actionsColumn.setCellFactory(cellFactory);
    }

    @FXML
    public void showOptions(ActionEvent actionEvent) {
        if(automatic.isSelected()) {
            frequency.setVisible(true);
            frequency.setManaged(true);
            amount.setVisible(true);
            amount.setManaged(true);
        }
        else {
            frequency.setVisible(false);
            frequency.setManaged(false);
            amount.setVisible(false);
            amount.setManaged(false);
        }
    }

    @FXML
    public void changeColors(ActionEvent actionEvent) {
        ToggleButton pressed = (ToggleButton) actionEvent.getSource();
        if(pressed == incomeChoiceButton) {
            expenseChoiceButton.setStyle("-fx-background-color: #E1E5F2; -fx-text-fill: #3A4D8F;");
            incomeChoiceButton.setStyle("-fx-background-color:  #3A4D8F; -fx-text-fill: #FFFFFF;");
        }
        else {
            incomeChoiceButton.setStyle("-fx-background-color: #E1E5F2; -fx-text-fill: #3A4D8F;");
            expenseChoiceButton.setStyle("-fx-background-color:  #3A4D8F; -fx-text-fill: #FFFFFF;");
        }
    }

    public void filterCategories(ActionEvent actionEvent) throws SQLException {
        String filter = filters.getValue();
        CategoryFilter Filter = null;
        String filterType = "";
        switch (filter)
        {
            case "All", "Oldest":
                Filter = new CategoryNormalFilter();
                filterType = "";
                break;
            case "Expense":
                Filter = new CategoryTypeFilter();
                filterType = "expense";
                break;
            case "Income":
                Filter = new CategoryTypeFilter();
                filterType = "income";
                break;
            case "Automatic":
                Filter = new CategoryFrequencyFilter();
                filterType = "AUTO";
                break;
            case "Normal":
                Filter = new CategoryFrequencyFilter();
                filterType = "NEVER";
                break;
            case "Recent":
                Filter = new CategoryRecentFilter();
                filterType = "";
                break;
            case "Ascending":
                Filter = new CategoryAlphabeticalFilter();
                filterType = "ascending";
                break;
            case "Descending":
                Filter = new CategoryAlphabeticalFilter();
                filterType = "descending";
                break;
        }
        getCategories(Filter,filterType);
    }

}




