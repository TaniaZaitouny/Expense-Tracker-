package com.example.expensetracker.Controllers;

import com.example.expensetracker.Models.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ReportsController
{
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxis;
    @FXML
    PieChart pieChart;
    @FXML
    Button monthlyButton;
    @FXML
    Button weeklyButton;
//    boolean chartDrawn = false;
    public void initialize() {
        Transaction transaction = new Transaction();
        Transaction.TransactionStrategy defaultStrategy = transaction.new DefaultStrategy();
        List<Pair<Pair<String, Number>, String>> topCategories = defaultStrategy.topCategories();
        initializeBarChart(topCategories);
        initializePieChart(topCategories);
        weeklyButton.setOnAction(event-> {
            Transaction transaction1 = new Transaction();
            Transaction.TransactionStrategy weeklyStrategy = transaction1.new WeeklyStrategy();
            List<Pair<Pair<String, Number>, String>> topCategories1 = weeklyStrategy.topCategories();
            initializeBarChart(topCategories1);
            initializePieChart(topCategories1);
//            chartDrawn = true;

            System.out.println("Weekly strategy top categories:");
            for (Pair<Pair<String, Number>, String> pair : topCategories1) {
                Pair<String, Number> categoryAmountPair = pair.getKey();
                String categoryType = pair.getValue();
                System.out.println(categoryAmountPair.getKey() + " : " + categoryAmountPair.getValue() + " : " + categoryType);
            }
        });
        monthlyButton.setOnAction(event->{
            Transaction transaction2 = new Transaction();
            Transaction.TransactionStrategy monthlyStrategy = transaction2.new MonthlyStrategy();
            List<Pair<Pair<String, Number>, String>> topCategories2 = monthlyStrategy.topCategories();
            initializeBarChart(topCategories2);
            initializePieChart(topCategories2);
            System.out.println("Weekly strategy top categories:");
            for (Pair<Pair<String, Number>, String> pair : topCategories2) {
                Pair<String, Number> categoryAmountPair = pair.getKey();
                String categoryType = pair.getValue();
                System.out.println(categoryAmountPair.getKey() + " : " + categoryAmountPair.getValue() + " : " + categoryType);
            }
            monthlyButton.setDisable(true);
        });


        }




    private void initializeBarChart(List<Pair<Pair<String, Number>,String>> results) {
        barChart.getData().clear();
        ((CategoryAxis) barChart.getXAxis()).getCategories().clear();
        Transaction transaction = new Transaction();

//        List<Pair<String, Number>> results = transaction.getTopCategories();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<String> categoryNames = new ArrayList<String>(5);
        for(Pair<Pair<String, Number>,String>  result : results) {
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

}
