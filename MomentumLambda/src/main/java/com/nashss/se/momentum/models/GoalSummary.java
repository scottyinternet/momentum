package com.nashss.se.momentum.models;

public class GoalSummary {

    private String goalName;

    private String status;
    private String statusMessage;
    private String currentStreak;
    public GoalSummary(GoalModel goal) {
        this.goalName = goal.getGoalInfo().getGoalName();
        this.status = goal.getStatus().getStatusEnum().toString();
        this.statusMessage = goal.getStatus().getStatusMessage();
        this.currentStreak = Integer.toString(goal.getStreakData().getCurrentStreak());
    }


    public String getGoalName() {
        return goalName;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getCurrentStreak() {
        return currentStreak;
    }
}