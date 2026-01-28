package com.example.employeefitnessapplication.model;

import java.time.LocalDate;

public class  Swimming extends Activity {
    private double hours;

    public Swimming(int employeeId, LocalDate date, double hours) {
        super(employeeId, "Swimming", date);
        this.hours = hours;
    }

    public double getHours() {
        return hours;
    }

    @Override
    public double getCalculatedPoints() {
        return (hours / 0.5) * 1200;
    }

    @Override
    public double getMeasurement() {
        return hours;
    }
}
