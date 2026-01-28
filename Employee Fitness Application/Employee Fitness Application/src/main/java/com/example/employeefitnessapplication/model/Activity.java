package com.example.employeefitnessapplication.model;

import java.time.LocalDate;

public abstract class Activity {
    private int activityId;
    private int employeeId;
    private String activityType;
    private LocalDate date;

    public Activity(int employeeId, String activityType, LocalDate date) {
        this.employeeId = employeeId;
        this.activityType = activityType;
        this.date = date;
    }

    // Abstract method to be implemented by subclasses
    public abstract double getCalculatedPoints();
    public abstract double getMeasurement();


    // Getters and Setters
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}