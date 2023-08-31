package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.StatusEnum;

public class Status {

    private StatusEnum statusEnum;
    private String statusMessage;

    public Status(StatusEnum statusEnum, String statusMessage) {
        this.statusEnum = statusEnum;
        this.statusMessage = statusMessage;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}

