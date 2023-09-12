package com.nashss.se.momentum.models;

import java.util.ArrayList;
import java.util.List;

public class GoalDetailsModel {

    private final Status status;
    private final String statusString;
    private final List<EventModel> eventModelList;
    private final String goalSummaryMessage;
    private final String goalName;
    private final String unit;

    public GoalDetailsModel(Status status, List<EventModel> eventModelList, String goalSummaryMessage, String goalName, String unit) {
        this.status = status;
        this.statusString = status.getStatusEnum().toString();
        this.eventModelList = new ArrayList<>(eventModelList);
        this.goalSummaryMessage = goalSummaryMessage;
        this.goalName = goalName;
        this.unit = unit;
    }

    public Status getStatus() {
        return status;
    }

    public List<EventModel> getEventModelList() {
        return new ArrayList<>(eventModelList);
    }

    public String getGoalSummaryMessage() {
        return goalSummaryMessage;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getStatusString() {
        return statusString;
    }

    public String getUnit() {
        return unit;
    }
}
