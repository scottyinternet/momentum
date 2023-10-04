package com.nashss.se.momentum.models;

import java.time.LocalDate;

public class EventSummary {
    private final LocalDate date;
    private final double SummedMeasurement;

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
