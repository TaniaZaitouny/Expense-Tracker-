package com.example.expensetracker.Controllers;

import com.example.expensetracker.Database.DatabaseConnection;
import com.example.expensetracker.HelloApplication;
import com.example.expensetracker.Models.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReportsController
{
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxis;
    @FXML
    PieChart pieChart;

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
