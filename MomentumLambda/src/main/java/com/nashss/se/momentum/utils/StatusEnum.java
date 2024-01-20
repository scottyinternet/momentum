package com.nashss.se.momentum.utils;

public enum StatusEnum {


    IN_MOMENTUM("In Momentum"),
    IN_MOMENTUM_HIT_TOMORROW("In Momentum, Hit Tomorrow"),
    HAD_MOMENTUM_YESTERDAY("Had Momentum Yesterday"),
    GAINING_MOMENTUM("Gaining Momentum"),
    LOSING_MOMENTUM("Losing Momentum"),
    STALE_MOMENTUM("Stale Momentum"),
    NO_MOMENTUM("No Momentum");

    private final String message;

    StatusEnum(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
