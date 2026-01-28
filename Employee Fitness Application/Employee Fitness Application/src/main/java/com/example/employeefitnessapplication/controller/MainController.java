package com.example.employeefitnessapplication.controller;

import com.example.employeefitnessapplication.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private void initialize() {
        // Load the Dashboard view by default when the application starts
        showDashboardView();
    }

    @FXML
    void showDashboardView() {
        loadView("DashboardView.fxml");
    }

    @FXML
    void showAddActivityView() {
        loadView("AddActivityView.fxml");
    }

    @FXML
    void showViewActivities() {
        loadView("ViewActivities.fxml");
    }

    @FXML
    void showLeaderboard() {
        loadView("LeaderboardView.fxml");
    }

    @FXML
    void handleClose(ActionEvent event) {
        System.exit(0);
    }

    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(MainApplication.class.getResource("/com/example/employeefitnessapplication/" + fxmlFile));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}