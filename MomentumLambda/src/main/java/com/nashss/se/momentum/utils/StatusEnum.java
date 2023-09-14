package com.nashss.se.momentum.utils;

public enum StatusEnum {


    GAINING_MOMENTUM("Gaining Momentum"),
    LOSING_MOMENTUM("Losing Momentum"),
    NO_MOMENTUM("No Momentum"),
    IN_MOMENTUM("In Momentum"),
    IN_MOMENTUM_HIT_TOMORROW("In Momentum"),
    IN_MOMENTUM_HIT_TODAY("Keep Momentum");



    private final String message;

    StatusEnum(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
