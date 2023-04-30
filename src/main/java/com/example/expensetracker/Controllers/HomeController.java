package com.example.expensetracker.Controllers;


import com.example.expensetracker.Main;
import com.example.expensetracker.Models.Transaction;
import com.example.expensetracker.Strategy.DefaultStrategy;
import com.example.expensetracker.Strategy.TransactionStrategy;
import com.example.expensetracker.Threads.CheckAutomaticCategoriesThread;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeController implements ObserverController {
    @FXML
    private HBox topCategoriesBox;

    public void initialize() {
        CheckAutomaticCategoriesThread thread = new CheckAutomaticCategoriesThread();
        thread.registerObserver(this);
        thread.start();
        initializeTopCategories();
    }
   public void initializeTopCategories()
   {

       displayTopCategories();

   }
    public void displayTopCategories()
    {
        TransactionStrategy defaultStrategy = new DefaultStrategy();
        List<Pair<Pair<String, Number>, String>> topCategories = defaultStrategy.topCategories();
       for(Pair<Pair<String, Number>,String> category : topCategories)
        {
            Pair<String, Number> categoryPair = category.getKey();
            String categoryName = categoryPair.getKey();
            Label label = new Label(categoryName);

            Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("Media/coffee.png")));
            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(29);
            imageView.setFitHeight(29);
            VBox imgBox = new VBox(imageView);
            imgBox.setStyle("-fx-background-color:#3A4D8F; -fx-background-radius: 50px;");
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
    public void getNotified() {
        if(topCategoriesBox != null) {

        }
    }
}