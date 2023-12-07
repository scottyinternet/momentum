package com.nashss.se.momentum.models;

import java.time.LocalDate;

public class GoalInfo {
    private final String goalName;
    private final String userId;
    private final String goalId;
    private final LocalDate startDate;

    public GoalInfo(String goalName, String userId, String goalId, LocalDate startDate) {
        this.goalName = goalName;
        this.userId = userId;
        this.goalId = goalId;
        this.startDate = startDate;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getUserId() {
        return userId;
    }

    public String getGoalId() {
        return goalId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
