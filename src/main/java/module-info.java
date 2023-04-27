module com.example.expensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports com.example.expensetracker.Database;
    opens com.example.expensetracker.Database to javafx.fxml;
    exports com.example.expensetracker.Controllers;
    opens com.example.expensetracker.Controllers to javafx.fxml;
    exports com.example.expensetracker;
    opens com.example.expensetracker to javafx.fxml;
}