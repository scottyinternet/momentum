package com.nashss.se.momentum.models;

import java.time.LocalDate;

public class EventSummary {
    private LocalDate date;
    private double SummedMeasurement;

    public EventSummary(LocalDate date, double summedMeasurement) {
        this.date = date;
        SummedMeasurement = summedMeasurement;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getSummedMeasurement() {
        return SummedMeasurement;
    }
}
