package com.nashss.se.momentum.models;

import java.util.ArrayList;
import java.util.List;

public class GoalDetailsModel {

    private final Status status;
    private final List<EventModel> eventModelList;
    private final String goalSummaryMessage;
    private final String goalName;

    public GoalDetailsModel(Status status, List<EventModel> eventModelList, String goalSummaryMessage, String goalName) {
        this.status = status;
        this.eventModelList = new ArrayList<>(eventModelList);
        this.goalSummaryMessage = goalSummaryMessage;
        this.goalName = goalName;
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
}
