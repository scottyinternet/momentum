package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.StatusEnum;

import java.util.Objects;

public class GoalSummary {

    private String goalName;

    private String goalStatus;

    public String getGoalName() {
        return goalName;
    }

    public String getGoalStatus() {
        return goalStatus;
    }

    public GoalSummary(String goalName, StatusEnum goalStatus) {
        this.goalName = goalName;
        this.goalStatus = goalStatus.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalSummary that = (GoalSummary) o;
        return Objects.equals(getGoalName(), that.getGoalName()) && Objects.equals(getGoalStatus(), that.getGoalStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGoalName(), getGoalStatus());
    }
}