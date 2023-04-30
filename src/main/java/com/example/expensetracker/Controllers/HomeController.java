package com.example.expensetracker.Controllers;


import com.example.expensetracker.Main;
import com.example.expensetracker.Models.Category;
import com.example.expensetracker.Models.Transaction;
import com.example.expensetracker.Strategy.DailyStrategy;
import com.example.expensetracker.Strategy.DefaultStrategy;
import com.example.expensetracker.Strategy.TransactionStrategy;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HomeController implements ObserverController {
    @FXML
    private HBox topCategoriesBox;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxis;
    @FXML
    Button dailyStatistics;
    @FXML
    PieChart pieChart;
    public void initialize() {
        initializeTopCategories();
    }
   public void initializeTopCategories()
   {

       displayTopCategories();
       initializeHomeCharts();
   }
  public void initializeHomeCharts()
  {
      TransactionStrategy dailyStrategy = new DailyStrategy();
      List<Pair<Pair<String, Number>, String>> topCategories = new ArrayList<>();
      topCategories = dailyStrategy.topCategories();
      initializeBarChart(topCategories);
      initializePieChart(topCategories);
  }
    private void initializeBarChart(List<Pair<Pair<String, Number>, String>> results) {

            ((CategoryAxis) barChart.getXAxis()).getCategories().clear();
            Transaction transaction = new Transaction();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            List<String> categoryNames = new ArrayList<String>(5);
            for (Pair<Pair<String, Number>, String> result : results) {
                Pair<String, Number> categoryAmount = result.getKey();
                String name = categoryAmount.getKey();
                double amount = (double) categoryAmount.getValue();
                String type = result.getValue();

                categoryNames.add(name);
                XYChart.Data<String, Number> data = new XYChart.Data<>(name, amount);
                data.setExtraValue(type);
                series.getData().add(data);
            }
            categoryAxis.setCategories(FXCollections.observableArrayList(categoryNames));
//            barChart.setPrefWidth(600);
            barChart.getData().add(series);
            String type = null;
            for (XYChart.Series<String, Number> bars : barChart.getData()) {
                for (XYChart.Data<String, Number> bar : bars.getData()) {
                    type = (String) bar.getExtraValue();
                    if (type == null) {
                        System.out.println("bar " + bar.getXValue());
                    }
                    assert type != null;
                    bar.getNode().setStyle(type.equals("expense") ? "-fx-bar-fill: #3A4D8F;" : "-fx-bar-fill: #99b3ff;");
                }
            }
            barChart.setLegendVisible(false);

    }
    private void initializePieChart(List<Pair<Pair<String, Number>, String>> results) {
        pieChart.getData().clear();
        List<PieChart.Data> data = new ArrayList<>(5);
        List<String> types = new ArrayList<>();

        for (Pair<Pair<String, Number>, String> result : results) {
            Pair<String, Number> categoryAmount = result.getKey();
            String categoryName = categoryAmount.getKey();
            Number categoryValue = categoryAmount.getValue();
            PieChart.Data slice = new PieChart.Data(categoryName, categoryValue.doubleValue());
            types.add(result.getValue());
            data.add(slice);
        }

        pieChart.setData(FXCollections.observableArrayList(data));

        int currentSliceIndex = 0;
        for (PieChart.Data slice : pieChart.getData()) {
            String type = types.get(currentSliceIndex);
            if(type == null) System.out.println("pie" + currentSliceIndex);
            currentSliceIndex += 1;
            slice.getNode().setStyle(type.equals("expense") ? "-fx-pie-color: #3A4D8F;" : "-fx-pie-color: #99b3ff;");
        }
        pieChart.setLegendVisible(false);
    }
    public void displayTopCategories()
    {

        TransactionStrategy defaultStrategy = new DefaultStrategy();
        List<Pair<Pair<String, Number>, String>> topCategories = defaultStrategy.topCategories();
       for(Pair<Pair<String, Number>,String> category : topCategories)
        {
            Pair<String, Number> categoryPair = category.getKey();
            String categoryName = categoryPair.getKey();
            Category c = new Category();
            String iconName = c.getIcon(categoryName);
            Label label = new Label(categoryName);

            Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/Icons/" + iconName + ".png")));
            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(35);
            imageView.setFitHeight(35);
            VBox imgBox = new VBox(imageView);

            imgBox.setStyle("-fx-background-color:#ffffff; -fx-background-radius: 50px;");
            imgBox.setAlignment(Pos.CENTER);
            imgBox.setPrefWidth(42);
            imgBox.setPrefHeight(40);
            imgBox.setMaxWidth(Region.USE_PREF_SIZE);
            VBox box = new VBox(imgBox,label);

            box.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 20px;");

            box.setPrefHeight(30);
            box.setPrefWidth(80);
            box.setPadding(new Insets(10,0,0,0));
            box.setAlignment(Pos.TOP_CENTER);

            // Add the mouse entered event listener to make the VBox glow
            box.setOnMouseEntered(e -> {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(15);
                dropShadow.setSpread(0.5);
                dropShadow.setColor(Color.LAVENDER);
                box.setEffect(dropShadow);
                box.setScaleX(1.1);
                box.setScaleY(1.1);
            });

            // Add the mouse exited event listener to revert the VBox back to its original style
            box.setOnMouseExited(e -> {
                box.setEffect(null);
                box.setScaleX(1.0);
                box.setScaleY(1.0);
            });
            topCategoriesBox.getChildren().add(box);
        }

    }




    @Override
    public void notify(ArrayList<Object> tableData) {
        if(topCategoriesBox != null) {

        }
    }
}