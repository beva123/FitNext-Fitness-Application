package com.example.employeefitnessapplication.model;

import java.time.LocalDate;

public class Workout extends Activity {
    private double hours;

    public Workout(int employeeId, LocalDate date, double hours) {
        super(employeeId, "Workout", date);
        this.hours = hours;
    }

    public double getHours() {
        return hours;
    }

    @Override
    public double getCalculatedPoints() {
        return hours * 1500;
    }

    @Override
    public double getMeasurement() {
        return hours;
    }
}