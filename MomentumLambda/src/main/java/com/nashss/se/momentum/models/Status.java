package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.StatusEnum;

import java.util.ArrayList;
import java.util.List;

public class Status {

    private final StatusEnum statusEnum;
    private final String statusMessage;
    private final double sum;

    public Status(StatusEnum statusEnum, String statusMessage, double sum) {
        this.statusEnum = statusEnum;
        this.statusMessage = statusMessage;
        this.sum = sum;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public double getSum() {
        return sum;
    }
}

