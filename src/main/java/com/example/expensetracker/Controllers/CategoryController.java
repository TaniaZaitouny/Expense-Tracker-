package com.example.expensetracker.Controllers;

import com.example.expensetracker.Filters.CategoryFilters.*;
import com.example.expensetracker.Main;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Objects.CategoryObject;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.Date;


public class CategoryController {
    @FXML
    ChoiceBox<String> filters;
    @FXML
    ChoiceBox<String> filtersType;
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
    Text frequencyLabel;
    @FXML
    ChoiceBox<String> frequency;
    @FXML
    ComboBox<String> categoryIcon;
    @FXML
    Text amountLabel;
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
            filterCategories();
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
                    frequencyLabel.setVisible(true);
                    frequencyLabel.setManaged(true);
                    frequency.setVisible(true);
                    frequency.setManaged(true);
                    amountLabel.setVisible(true);
                    amountLabel.setManaged(true);
                    amount.setVisible(true);
                    amount.setManaged(true);
                    frequency.setValue(categoryToUpdate.frequency);
                    amount.setText(String.valueOf(categoryToUpdate.amount));
                }
            }

            final File folder = new File("src/main/resources/com/example/expensetracker/Media/Icons/");
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
                        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/Icons/" + item + ".png")));
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
        Stage stage = (Stage) addCategoryButton.getScene().getWindow();
        MenuController.loadPage("Views/addCategory.fxml", stage);
    }

    public void updateCategory() throws SQLException{
        String name = categoryName.getText();
        String type;
        String icon = categoryIcon.getValue();

        if(!incomeChoiceButton.isSelected() && !expenseChoiceButton.isSelected()) {
             messageText.setText("PLease Choose A Type For Category !");
            return;
        }

        if(name.equals("")) {
             messageText.setText("PLease Choose A Name For Category !");
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
            double amountValue;
            try {
                amountValue = Double.parseDouble(amount.getText());
            }
            catch (NumberFormatException e)
            {
                messageText.setText("Please Enter Correct Amount Value !");
                return;
            }
            if(categoryNameToUpdate == null) {
                addCategory(name, type, icon, frequencyValue, amountValue);
            }
            else {
                java.sql.Date sqlDate = new java.sql.Date(categoryToUpdate.lastTransaction.getTime());
                editCategory(name, type, icon, frequencyValue, sqlDate, amountValue);
            }
        }
        else {
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
            Stage stage = (Stage) submitButton.getScene().getWindow();
            try {
                MenuController.loadPage("Views/categories.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            messageText.setText("Category Already Exists !");
        }
    }

    private void addCategory(String name, String type, String icon, String frequencyValue, double amountValue) throws SQLException
    {
        if(category.addCategory(name, type, icon, frequencyValue, amountValue)) {
            Stage stage = (Stage) submitButton.getScene().getWindow();
            try {
                MenuController.loadPage("Views/categories.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            messageText.setText("Category Already Exists !");
        }
    }

    private void editCategory(String name, String type, String icon) throws SQLException
    {
        category.updateCategory(categoryToUpdate.categoryName, name, type, icon);
        Stage stage = (Stage) submitButton.getScene().getWindow();
        try {
            MenuController.loadPage("Views/categories.fxml", stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void editCategory(String name, String type, String icon, String frequencyValue, Date lastTransactionDate, double amountValue) throws SQLException
    {

        category.updateCategory(categoryToUpdate.categoryName, name, type, icon, frequencyValue, lastTransactionDate, amountValue);
        Stage stage = (Stage) addCategoryButton.getScene().getWindow();
        try {
            MenuController.loadPage("Views/categories.fxml", stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public void showOptions() {
        if(automatic.isSelected()) {
            frequencyLabel.setVisible(true);
            frequencyLabel.setManaged(true);
            frequency.setVisible(true);
            frequency.setManaged(true);
            amountLabel.setVisible(true);
            amountLabel.setManaged(true);
            amount.setVisible(true);
            amount.setManaged(true);
        }
        else {
            frequencyLabel.setVisible(false);
            frequencyLabel.setManaged(false);
            frequency.setVisible(false);
            frequency.setManaged(false);
            amountLabel.setVisible(false);
            amountLabel.setManaged(false);
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

    public void filterCategories() throws SQLException {
        String filter = filters.getValue();
        switch (filter) {
            case "All" -> {
                filtersType.setVisible(false);
                filtersType.setManaged(false);
                getCategories(new CategoryNormalFilter(), "");
                return;
            }
            case "Type" -> setTypeList("type");
            case "Automatic" -> setTypeList("frequency");
            case "Date" -> setTypeList("date");
            case "Alphabetical" -> setTypeList("alphabetical");
        }
        filtersType.setVisible(true);
        filtersType.setManaged(true);
    }
    private void setTypeList(String Filter)
    {
        ObservableList<String> items = filtersType.getItems();
        items.clear();
        switch (Filter) {
            case "type" -> {
                items.add("Income");
                items.add("Expense");
                filtersType.setValue("Income");
            }
            case "frequency" -> {
                items.add("Automatic");
                items.add("Normal");
                filtersType.setValue("Automatic");
            }
            case "alphabetical" -> {
                items.add("Ascending");
                items.add("Descending");
                filtersType.setValue("Ascending");
            }
            case "date" -> {
                items.add("Oldest");
                items.add("Recent");
                filtersType.setValue("Recent");
            }
        }
    }

    public void filterCategoryTypes() throws SQLException {
        String filter = filtersType.getValue();
        if(filter == null) return;
        switch (filter) {
            case "Income" -> getCategories(new CategoryTypeFilter(), "income");
            case "Expense" -> getCategories(new CategoryTypeFilter(), "expense");
            case "Automatic" -> getCategories(new CategoryFrequencyFilter(), "auto");
            case "Normal" -> getCategories(new CategoryFrequencyFilter(), "never");
            case "Ascending" -> getCategories(new CategoryAlphabeticalFilter(), "ascending");
            case "Descending" -> getCategories(new CategoryAlphabeticalFilter(), "descending");
            case "Oldest" -> getCategories(new CategoryNormalFilter(), "");
            case "Recent" -> getCategories(new CategoryRecentFilter(), "");
        }
    }
}




