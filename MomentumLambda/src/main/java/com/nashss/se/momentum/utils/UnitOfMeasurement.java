package com.nashss.se.momentum.utils;

public enum UnitOfMeasurement {

    MINUTES("minutes"),
    HOURS("hours"),
    MILES("miles"),
    KILOMETERS("kilometers"),
    COUNT("count"),
    UNITS("units");

   private final String name;
    UnitOfMeasurement(String name) {
        this.name=name;

    }

    @Override
    public String toString() {
        return this.name;
    }
}
