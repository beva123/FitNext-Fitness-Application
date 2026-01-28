module com.example.employeefitnessapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    requires mysql.connector.j;

    exports com.example.employeefitnessapplication;

    opens com.example.employeefitnessapplication.controller to javafx.fxml;
    opens com.example.employeefitnessapplication.model to javafx.base;
}