package com.example.expensetracker.Controllers;

import com.example.expensetracker.Models.Transaction;
import com.example.expensetracker.Strategy.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ReportsController implements ObserverController {
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxis;
    @FXML
    PieChart pieChart;
    @FXML
    ChoiceBox<String> filterReport;
    @FXML
    Label selectedStrategy, expenseLabel, incomeLabel, balanceLabel;

    //    boolean chartDrawn = false;
    public void initialize() {
        filterReport.setValue("Default");
    }


    public void initializeBarChart(List<Pair<Pair<String, Number>, String>> results) {
        barChart.getData().clear();
        ((CategoryAxis) barChart.getXAxis()).getCategories().clear();
        Transaction transaction = new Transaction();

//        List<Pair<String, Number>> results = transaction.getTopCategories();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<String> categoryNames = new ArrayList<String>(5);
        for (Pair<Pair<String, Number>, String> result : results) {
            Pair<String, Number> categoryAmount = result.getKey();
            String name = categoryAmount.getKey();
            String type = result.getValue();

            categoryNames.add(categoryAmount.getKey());
            XYChart.Data<String, Number> data = new XYChart.Data<>(name, categoryAmount.getValue());
            series.getData().add(data);

//
//            if (type.equals("expense")) {
//                Node node = data.getNode();
//                if (node != null) {
//                    node.setStyle("-fx-bar-fill: #ff6961;");
//                }
//            } else if (type.equals("income")) {
//                Node node = data.getNode();
//                if(node!=null) {
//                    data.getNode().setStyle("-fx-bar-fill: #77dd77;");
//                }
//            }

        }

        categoryAxis.setCategories(FXCollections.observableArrayList(categoryNames));

        barChart.setPrefWidth(600);
        barChart.setStyle("-fx-bar-fill: #77dd77;");
        barChart.getData().add(series);

    }

    //
    private void initializePieChart(List<Pair<Pair<String, Number>, String>> results) {
        pieChart.getData().clear();
        List<PieChart.Data> data = new ArrayList<>(5);

        for (Pair<Pair<String, Number>, String> result : results) {
            Pair<String, Number> categoryAmount = result.getKey();
            String categoryName = categoryAmount.getKey();
            Number categoryValue = categoryAmount.getValue();
            data.add(new PieChart.Data(categoryName, categoryValue.doubleValue()));
        }

        pieChart.setData(FXCollections.observableArrayList(data));
    }

    @Override
    public void notify(ArrayList<Object> tableData) {
        if (barChart != null) {

        }
    }


    public void filterReports(ActionEvent event) {
        String strategy = filterReport.getValue();
        List<Pair<Pair<String, Number>, String>> topCategories = new ArrayList<>();
        Number totalExpenses = 0.0 , totalIncome = 0.0;
        Double totalBalance = 0.0;
        switch (strategy) {
            case "Default":
                TransactionStrategy defaultStrategy = new DefaultStrategy();
                topCategories = defaultStrategy.topCategories();
                totalExpenses = defaultStrategy.totalExpense();
                totalIncome = defaultStrategy.totalIncome();
                break;
            case "Daily":
                TransactionStrategy dailyStrategy = new DailyStrategy();
                topCategories = dailyStrategy.topCategories();
                totalExpenses = dailyStrategy.totalExpense();
                totalIncome = dailyStrategy.totalIncome();
                break;
            case "Weekly":
                TransactionStrategy weeklyStrategy = new WeeklyStrategy();
                topCategories = weeklyStrategy.topCategories();
                totalExpenses = weeklyStrategy.totalExpense();
                totalIncome = weeklyStrategy.totalIncome();
                break;
            case "Monthly":
                TransactionStrategy monthlyStrategy = new MonthlyStrategy();
                topCategories = monthlyStrategy.topCategories();
                totalExpenses = monthlyStrategy.totalExpense();
                totalIncome = monthlyStrategy.totalIncome();
                break;

        }
        initializeBarChart(topCategories);
        initializePieChart(topCategories);
        selectedStrategy.setText(strategy);
        incomeLabel.setText(totalIncome.toString());
        expenseLabel.setText(totalExpenses.toString());
        totalBalance = totalIncome.doubleValue() - totalExpenses.doubleValue();
        balanceLabel.setText(Double.toString(totalBalance));
//        for (Pair<Pair<String, Number>, String> pair : topCategories) {
//            Pair<String, Number> categoryAmountPair = pair.getKey();
//            String categoryType = pair.getValue();
//            System.out.println(categoryAmountPair.getKey() + " : " + categoryAmountPair.getValue() + " : " + categoryType);
//        }
    }
}
