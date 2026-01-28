package com.example.employeefitnessapplication.model;

public class LeaderboardEntry {
    private final int rank;
    private final String employeeName;
    private final double totalPoints;

    public LeaderboardEntry(int rank, String employeeName, double totalPoints) {
        this.rank = rank;
        this.employeeName = employeeName;
        this.totalPoints = totalPoints;
    }

    // Getters
    public int getRank() {
        return rank;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public double getTotalPoints() {
        return totalPoints;
    }
}