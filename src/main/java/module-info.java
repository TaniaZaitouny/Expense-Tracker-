module com.example.expensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.expensetracker to javafx.fxml;
    exports com.example.expensetracker;
    exports com.example.expensetracker.Database;
    opens com.example.expensetracker.Database to javafx.fxml;
    exports com.example.expensetracker.Controllers;
    opens com.example.expensetracker.Controllers to javafx.fxml;
}