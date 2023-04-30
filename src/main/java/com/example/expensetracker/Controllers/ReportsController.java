package com.example.expensetracker.Controllers;

import com.example.expensetracker.Models.Transaction;
import com.example.expensetracker.Strategy.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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
            double amount = (double) categoryAmount.getValue();
            String type = result.getValue();

            categoryNames.add(name);
            XYChart.Data<String, Number> data = new XYChart.Data<>(name, amount);
            data.setExtraValue(type);
            series.getData().add(data);
        }

        categoryAxis.setCategories(FXCollections.observableArrayList(categoryNames));

        barChart.setPrefWidth(600);
        barChart.getData().add(series);

        for (XYChart.Series<String, Number> bars : barChart.getData()) {
            for (XYChart.Data<String, Number> bar : bars.getData()) {
                String type = (String) bar.getExtraValue();
                if(type == null) System.out.println("bar " + bar.getXValue());
                assert type != null;
                bar.getNode().setStyle(type.equals("expense") ? "-fx-bar-fill: #3A4D8F;" : "-fx-bar-fill: #99b3ff;");
            }
        }
    }

    //
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
        double totalBalance = 0.0;
        switch (strategy) {
            case "Default" -> {
                TransactionStrategy defaultStrategy = new DefaultStrategy();
                topCategories = defaultStrategy.topCategories();
                totalExpenses = defaultStrategy.totalExpense();
                totalIncome = defaultStrategy.totalIncome();
            }
            case "Daily" -> {
                TransactionStrategy dailyStrategy = new DailyStrategy();
                topCategories = dailyStrategy.topCategories();
                totalExpenses = dailyStrategy.totalExpense();
                totalIncome = dailyStrategy.totalIncome();
            }
            case "Weekly" -> {
                TransactionStrategy weeklyStrategy = new WeeklyStrategy();
                topCategories = weeklyStrategy.topCategories();
                totalExpenses = weeklyStrategy.totalExpense();
                totalIncome = weeklyStrategy.totalIncome();
            }
            case "Monthly" -> {
                TransactionStrategy monthlyStrategy = new MonthlyStrategy();
                topCategories = monthlyStrategy.topCategories();
                totalExpenses = monthlyStrategy.totalExpense();
                totalIncome = monthlyStrategy.totalIncome();
            }
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
