package com.example.expensetracker.Controllers;

import com.example.expensetracker.Models.Transaction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsController
{
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxis;
    @FXML
    PieChart pieChart;

    public void initialize(URL location, ResourceBundle resources) {
        initializeBarChart();
        initializePieChart();
    }

    private void initializeBarChart() {
        Transaction transaction = new Transaction();

        List<Pair<String, Number>> results = transaction.getTopCategories();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<String> categoryNames = new ArrayList<String>(5);
        for(Pair<String, Number> result : results) {
            categoryNames.add(result.getKey());
            series.getData().add(new XYChart.Data<>(result.getKey(), result.getValue()));
        }
        categoryAxis.setCategories(FXCollections.observableArrayList(categoryNames));
        barChart.getData().add(series);
    }

    private void initializePieChart() {
        Transaction transaction = new Transaction();

        List<Pair<String, Number>> results = transaction.getTopCategories();

        List<PieChart.Data> data = new ArrayList<>(5);

        for(Pair<String, Number> result : results) {
            data.add(new PieChart.Data(result.getKey(), (double) result.getValue()));
        }

        pieChart.setData(FXCollections.observableArrayList(data));
    }

}
