package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.StatusEnum;

public class GoalSummary {

    private String goalName;

    private String goalStatus;

    public String getGoalName() {
        return goalName;
    }

    public String getGoalStatus() {
        return goalStatus.toString();
    }

    public GoalSummary(String goalName, StatusEnum goalStatus) {
        this.goalName = goalName;
        this.goalStatus = goalStatus.toString();
    }
}
