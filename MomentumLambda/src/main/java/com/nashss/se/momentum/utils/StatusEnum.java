package com.nashss.se.momentum.utils;

public enum StatusEnum {


    GAINING_MOMENTUM("You are gaining momentum"),
    LOSING_MOMENTUM("You are losing momentum"),
    NO_MOMENTUM("You have lost momentum"),
    IN_MOMENTUM("You are in momentum");

    private final String message;

    StatusEnum(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
