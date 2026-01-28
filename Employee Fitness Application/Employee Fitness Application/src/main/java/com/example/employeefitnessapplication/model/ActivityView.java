package com.example.employeefitnessapplication.model;

import java.time.LocalDate;

public class ActivityView {
    private String employeeName;
    private String activityType;
    private double measurement;
    private LocalDate date;
    private double calculatedPoints;

    public ActivityView(String employeeName, String activityType, double measurement, LocalDate date, double calculatedPoints) {
        this.employeeName = employeeName;
        this.activityType = activityType;
        this.measurement = measurement;
        this.date = date;
        this.calculatedPoints = calculatedPoints;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getActivityType() {
        return activityType;
    }

    public double getMeasurement() {
        return measurement;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCalculatedPoints() {
        return calculatedPoints;
    }
}