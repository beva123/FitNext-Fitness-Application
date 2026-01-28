package com.example.employeefitnessapplication.dao;

import com.example.employeefitnessapplication.model.ActivityView;
import com.example.employeefitnessapplication.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    public int getTotalActivityCount() {
        String sql = "SELECT COUNT(*) FROM activities";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getTopPerformer() {
        String sql = "SELECT e.name FROM employees e JOIN activities a ON e.id = a.employee_id GROUP BY e.id, e.name ORDER BY SUM(CASE WHEN a.activity_type = 'Running' THEN a.measurement / 10.0 WHEN a.activity_type = 'Swimming' THEN (a.measurement / 0.5) * 1200 WHEN a.activity_type = 'Workout' THEN a.measurement * 1500 ELSE 0 END) DESC LIMIT 1";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public ObservableMap<String, Integer> getActivityCountsByType() {
        ObservableMap<String, Integer> data = FXCollections.observableHashMap();
        String sql = "SELECT activity_type, COUNT(*) as count FROM activities GROUP BY activity_type";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.put(rs.getString("activity_type"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<ActivityView> getRecentActivities(int limit) {
        List<ActivityView> recentActivities = new ArrayList<>();
        String sql = "SELECT e.name, a.activity_type, a.measurement, a.date FROM activities a JOIN employees e ON a.employee_id = e.id ORDER BY a.date DESC, a.activity_id DESC LIMIT " + limit;
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String employeeName = rs.getString("name");
                String activityType = rs.getString("activity_type");
                double measurement = rs.getDouble("measurement");
                LocalDate date = rs.getDate("date").toLocalDate();
                double points = 0;
                recentActivities.add(new ActivityView(employeeName, activityType, measurement, date, points));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recentActivities;
    }
}