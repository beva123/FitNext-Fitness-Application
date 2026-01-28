package com.example.employeefitnessapplication.controller;

import com.example.employeefitnessapplication.MainApplication;
import com.example.employeefitnessapplication.dao.ActivityDAO;
import com.example.employeefitnessapplication.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AddActivityController {

    @FXML private AnchorPane addActivityPane;

    @FXML private ComboBox<Employee> employeeComboBox;
    @FXML private ComboBox<String> activityTypeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Label measurementLabel;
    @FXML private TextField measurementField;
    @FXML private Button addButton;

    private final ActivityDAO activityDAO = new ActivityDAO();

    @FXML
    public void initialize() {
        loadEmployees();
        activityTypeComboBox.setItems(FXCollections.observableArrayList("Running", "Swimming", "Workout"));
        activityTypeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                measurementLabel.setText(switch (newValue) {
                    case "Running" -> "Number of Steps";
                    case "Swimming", "Workout" -> "Number of Hours";
                    default -> "Measurement";
                });
            }
        });
        datePicker.setValue(LocalDate.now());
    }

    private void loadEmployees() {
        List<Employee> employeeList = activityDAO.getAllEmployees();
        employeeComboBox.setItems(FXCollections.observableArrayList(employeeList));
    }

    @FXML
    private void handleAddActivity() {
        Employee selectedEmployee = employeeComboBox.getValue();
        String activityType = activityTypeComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String measurementText = measurementField.getText();

        if (selectedEmployee == null || activityType == null || date == null || measurementText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }

        double measurement;
        try {
            measurement = Double.parseDouble(measurementText);
            if (measurement <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Measurement must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid number for the measurement.");
            return;
        }

        Activity newActivity = switch (activityType) {
            case "Running" -> new Running(selectedEmployee.getId(), date, (int) measurement);
            case "Swimming" -> new Swimming(selectedEmployee.getId(), date, measurement);
            case "Workout" -> new Workout(selectedEmployee.getId(), date, measurement);
            default -> null;
        };

        if (newActivity != null) {
            if (activityDAO.addActivity(newActivity)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Activity added successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add the activity to the database.");
            }
        }
    }

    @FXML
    private void navigateToDashboard() {
        try {
            BorderPane mainPane = (BorderPane) addActivityPane.getScene().getRoot();
            Parent view = FXMLLoader.load(MainApplication.class.getResource("/com/example/employeefitnessapplication/DashboardView.fxml"));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        employeeComboBox.getSelectionModel().clearSelection();
        activityTypeComboBox.getSelectionModel().clearSelection();
        measurementField.clear();
        datePicker.setValue(LocalDate.now());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}