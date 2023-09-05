package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.StatusEnum;

import java.util.ArrayList;
import java.util.List;

public class Status {

    private StatusEnum statusEnum;
    private String statusMessage;
    private List<EventSummary> eventSummaryList;

    public Status(StatusEnum statusEnum, String statusMessage, List<EventSummary> eventSummaryList) {
        this.statusEnum = statusEnum;
        this.statusMessage = statusMessage;
        this.eventSummaryList = eventSummaryList;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public List<EventSummary> getEventSummaryList() {
        return new ArrayList<>(eventSummaryList);
    }
}

