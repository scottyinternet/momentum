package com.nashss.se.momentum.models;

import java.time.LocalDate;

public class GoalCriteria {
    private final int target;
    private final int timeFrame;
    private final String units;
    private final LocalDate effectiveDate;
    private String goalCriteriaMessage;

    public GoalCriteria(int target, String units, int timeFrame, LocalDate effectiveDate) {
        this.target = target;
        this.units = units;
        this.timeFrame = timeFrame;
        this.effectiveDate = effectiveDate;
        createGoalCriteriaMessage();
    }

    private void createGoalCriteriaMessage() {
        goalCriteriaMessage = String
                .format("%d %s in a rolling %d day period.",
                        target,
                        units,
                        timeFrame);
    }

    public int getTarget() {
        return target;
    }

    public int getTimeFrame() {
        return timeFrame;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public String getGoalCriteriaMessage() {
        return goalCriteriaMessage;
    }
}
