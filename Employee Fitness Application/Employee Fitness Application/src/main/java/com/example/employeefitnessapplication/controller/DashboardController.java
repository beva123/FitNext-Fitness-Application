package com.example.employeefitnessapplication.controller;

import com.example.employeefitnessapplication.MainApplication;
import com.example.employeefitnessapplication.dao.ActivityDAO;
import com.example.employeefitnessapplication.dao.DashboardDAO;
import com.example.employeefitnessapplication.model.ActivityView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;

public class DashboardController {

    @FXML private AnchorPane dashboardPane;

    @FXML private Label totalActivitiesLabel;
    @FXML private Label totalEmployeesLabel;
    @FXML private Label topPerformerLabel;
    @FXML private BarChart<String, Integer> activitiesBarChart;
    @FXML private TableView<ActivityView> recentActivitiesTable;
    @FXML private TableColumn<ActivityView, String> employeeNameColumn;
    @FXML private TableColumn<ActivityView, String> activityTypeColumn;
    @FXML private TableColumn<ActivityView, LocalDate> dateColumn;

    private final DashboardDAO dashboardDAO = new DashboardDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();

    @FXML
    public void initialize() {
        loadStatsCards();
        loadBarChart();
        loadRecentActivitiesTable();
    }

    private void loadStatsCards() {
        totalActivitiesLabel.setText(String.valueOf(dashboardDAO.getTotalActivityCount()));
        totalEmployeesLabel.setText(String.valueOf(activityDAO.getAllEmployees().size()));
        topPerformerLabel.setText(dashboardDAO.getTopPerformer());
    }

    private void loadBarChart() {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Integer>> data = series.getData();
        dashboardDAO.getActivityCountsByType().forEach((type, count) -> data.add(new XYChart.Data<>(type, count)));
        activitiesBarChart.getData().add(series);
    }

    private void loadRecentActivitiesTable() {
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        recentActivitiesTable.getItems().setAll(dashboardDAO.getRecentActivities(5));
    }


    @FXML
    private void navigateToAddActivity() {
        loadViewInMain("AddActivityView.fxml");
    }

    @FXML
    private void navigateToViewActivities() {
        loadViewInMain("ViewActivities.fxml");
    }

    @FXML
    private void navigateToLeaderboard() {
        loadViewInMain("LeaderboardView.fxml");
    }


    private void loadViewInMain(String fxmlFile) {
        try {
            BorderPane mainPane = (BorderPane) dashboardPane.getScene().getRoot();

            Parent view = FXMLLoader.load(MainApplication.class.getResource("/com/example/employeefitnessapplication/" + fxmlFile));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}