package com.example.employeefitnessapplication.model;

import java.time.LocalDate;

public class Running extends Activity {
    private int steps;

    public Running(int employeeId, LocalDate date, int steps) {
        super(employeeId, "Running", date);
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public double getCalculatedPoints() {
        return (double) steps / 10.0;
    }

    @Override
    public double getMeasurement() {
        return steps;
    }
}