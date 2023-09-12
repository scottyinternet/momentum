package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.StatusEnum;

public class GoalSummary {

    private String goalName;

    private StatusEnum goalStatus;

    public String getGoalName() {
        return goalName;
    }

    public StatusEnum getGoalStatus() {
        return goalStatus;
    }

    public GoalSummary(String goalName, StatusEnum goalStatus) {
        this.goalName = goalName;
        this.goalStatus = goalStatus;
    }
}
