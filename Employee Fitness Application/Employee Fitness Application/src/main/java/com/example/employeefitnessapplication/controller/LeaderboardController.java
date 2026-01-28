package com.example.employeefitnessapplication.controller;

import com.example.employeefitnessapplication.MainApplication;
import com.example.employeefitnessapplication.dao.ActivityDAO;
import com.example.employeefitnessapplication.model.LeaderboardEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class LeaderboardController {

    @FXML private AnchorPane leaderboardPane;

    @FXML private TableView<LeaderboardEntry> leaderboardTableView;
    @FXML private TableColumn<LeaderboardEntry, Integer> rankColumn;
    @FXML private TableColumn<LeaderboardEntry, String> employeeNameColumn;
    @FXML private TableColumn<LeaderboardEntry, Double> totalPointsColumn;

    private final ActivityDAO activityDAO = new ActivityDAO();

    @FXML
    public void initialize() {
        // Set up the columns in the table
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        totalPointsColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));

        // Load the leaderboard data
        loadLeaderboard();
    }

    private void loadLeaderboard() {
        List<LeaderboardEntry> leaderboardList = activityDAO.getLeaderboard();
        ObservableList<LeaderboardEntry> observableList = FXCollections.observableArrayList(leaderboardList);
        leaderboardTableView.setItems(observableList);
    }

    @FXML
    private void navigateToDashboard() {
        try {
            BorderPane mainPane = (BorderPane) leaderboardPane.getScene().getRoot();
            Parent view = FXMLLoader.load(MainApplication.class.getResource("/com/example/employeefitnessapplication/DashboardView.fxml"));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}