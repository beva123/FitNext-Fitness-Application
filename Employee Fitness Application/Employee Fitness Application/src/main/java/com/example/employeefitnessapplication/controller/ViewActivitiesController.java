package com.example.employeefitnessapplication.controller;

import com.example.employeefitnessapplication.MainApplication;
import com.example.employeefitnessapplication.dao.ActivityDAO;
import com.example.employeefitnessapplication.model.ActivityView;
import com.example.employeefitnessapplication.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ViewActivitiesController {

    @FXML private AnchorPane viewActivitiesPane;

    // Table elements
    @FXML private TableView<ActivityView> activitiesTableView;
    @FXML private TableColumn<ActivityView, String> employeeNameColumn;
    @FXML private TableColumn<ActivityView, String> activityTypeColumn;
    @FXML private TableColumn<ActivityView, Double> measurementColumn;
    @FXML private TableColumn<ActivityView, LocalDate> dateColumn;
    @FXML private TableColumn<ActivityView, Double> pointsColumn;

    // Filter elements
    @FXML private ComboBox<Employee> employeeFilterComboBox;
    @FXML private ComboBox<String> activityFilterComboBox;
    @FXML private Button filterButton;
    @FXML private Button clearButton;

    private final ActivityDAO activityDAO = new ActivityDAO();

    @FXML
    public void initialize() {
        // Setup table columns
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        measurementColumn.setCellValueFactory(new PropertyValueFactory<>("measurement"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("calculatedPoints"));

        populateFilterComboBoxes();
        loadActivities(null, null);
    }

    private void populateFilterComboBoxes() {
        List<Employee> employees = activityDAO.getAllEmployees();
        employeeFilterComboBox.setItems(FXCollections.observableArrayList(employees));
        activityFilterComboBox.setItems(FXCollections.observableArrayList("Running", "Swimming", "Workout"));
    }

    private void loadActivities(Integer employeeId, String activityType) {
        List<ActivityView> activityList = activityDAO.getFilteredActivitiesForView(employeeId, activityType);
        ObservableList<ActivityView> observableList = FXCollections.observableArrayList(activityList);
        activitiesTableView.setItems(observableList);
    }

    @FXML
    private void handleFilter() {
        Employee selectedEmployee = employeeFilterComboBox.getValue();
        String selectedActivity = activityFilterComboBox.getValue();
        Integer employeeId = (selectedEmployee != null) ? selectedEmployee.getId() : null;
        loadActivities(employeeId, selectedActivity);
    }

    @FXML
    private void handleClearFilter() {
        employeeFilterComboBox.getSelectionModel().clearSelection();
        activityFilterComboBox.getSelectionModel().clearSelection();
        loadActivities(null, null);
    }

    @FXML
    private void navigateToDashboard() {
        try {
            BorderPane mainPane = (BorderPane) viewActivitiesPane.getScene().getRoot();
            Parent view = FXMLLoader.load(MainApplication.class.getResource("/com/example/employeefitnessapplication/DashboardView.fxml"));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}