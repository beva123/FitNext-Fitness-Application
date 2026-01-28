package com.example.employeefitnessapplication.dao;

import com.example.employeefitnessapplication.model.Activity;
import com.example.employeefitnessapplication.model.ActivityView;
import com.example.employeefitnessapplication.model.Employee;
import com.example.employeefitnessapplication.model.LeaderboardEntry;
import com.example.employeefitnessapplication.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {


    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY name";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                employees.add(new Employee(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }


    public boolean addActivity(Activity activity) {
        String sql = "INSERT INTO activities (employee_id, activity_type, measurement, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, activity.getEmployeeId());
            pstmt.setString(2, activity.getActivityType());
            pstmt.setDouble(3, activity.getMeasurement());
            pstmt.setDate(4, java.sql.Date.valueOf(activity.getDate()));

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<ActivityView> getAllActivitiesForView() {
        List<ActivityView> activityViews = new ArrayList<>();
        // SQL JOIN query to get employee name along with activity details
        String sql = "SELECT e.name, a.activity_type, a.measurement, a.date " +
                "FROM activities a " +
                "JOIN employees e ON a.employee_id = e.id " +
                "ORDER BY a.date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String employeeName = rs.getString("name");
                String activityType = rs.getString("activity_type");
                double measurement = rs.getDouble("measurement");
                LocalDate date = rs.getDate("date").toLocalDate();

                // Calculate points based on the activity type
                double points = 0;
                switch (activityType) {
                    case "Running":
                        points = (measurement / 10.0); // 10 steps = 1 point
                        break;
                    case "Swimming":
                        points = (measurement / 0.5) * 1200; // 0.5 hours = 1200 points
                        break;
                    case "Workout":
                        points = measurement * 1500; // 1 hour = 1500 points
                        break;
                }

                activityViews.add(new ActivityView(employeeName, activityType, measurement, date, points));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activityViews;
    }


    public List<ActivityView> getFilteredActivitiesForView(Integer employeeId, String activityType) {
        List<ActivityView> activityViews = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT e.name, a.activity_type, a.measurement, a.date " +
                        "FROM activities a JOIN employees e ON a.employee_id = e.id"
        );

        List<String> conditions = new ArrayList<>();
        if (employeeId != null) {
            conditions.add("a.employee_id = " + employeeId);
        }
        if (activityType != null && !activityType.isEmpty()) {
            conditions.add("a.activity_type = '" + activityType + "'");
        }

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        sql.append(" ORDER BY a.date DESC");

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {

            while (rs.next()) {
                String employeeName = rs.getString("name");
                String dbActivityType = rs.getString("activity_type");
                double measurement = rs.getDouble("measurement");
                LocalDate date = rs.getDate("date").toLocalDate();

                double points = 0;
                switch (dbActivityType) {
                    case "Running": points = (measurement / 10.0); break;
                    case "Swimming": points = (measurement / 0.5) * 1200; break;
                    case "Workout": points = measurement * 1500; break;
                }
                activityViews.add(new ActivityView(employeeName, dbActivityType, measurement, date, points));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activityViews;
    }


    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        // This SQL query calculates points for each activity, sums them up for each employee,
        // orders the results, and picks the top 10.
        String sql = "SELECT " +
                "    e.name, " +
                "    SUM(" +
                "        CASE " +
                "            WHEN a.activity_type = 'Running' THEN a.measurement / 10.0 " +
                "            WHEN a.activity_type = 'Swimming' THEN (a.measurement / 0.5) * 1200 " +
                "            WHEN a.activity_type = 'Workout' THEN a.measurement * 1500 " +
                "            ELSE 0 " +
                "        END" +
                "    ) AS total_points " +
                "FROM employees e " +
                "JOIN activities a ON e.id = a.employee_id " +
                "GROUP BY e.id, e.name " +
                "ORDER BY total_points DESC " +
                "LIMIT 10";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rank = 1;
            while (rs.next()) {
                String employeeName = rs.getString("name");
                double totalPoints = rs.getDouble("total_points");
                leaderboard.add(new LeaderboardEntry(rank++, employeeName, totalPoints));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }
}