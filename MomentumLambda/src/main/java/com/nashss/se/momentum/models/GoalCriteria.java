package com.nashss.se.momentum.models;

import java.time.LocalDate;

public class GoalCriteria {
    private final double target;
    private final int timeFrame;
    private final String units;
    private final LocalDate effectiveDate;
    private String goalCriteriaMessage;

    public GoalCriteria(double target, String units, int timeFrame, LocalDate effectiveDate) {
        this.target = target;
        this.units = units;
        this.timeFrame = timeFrame;
        this.effectiveDate = effectiveDate;
        createGoalCriteriaMessage();
    }

    private void createGoalCriteriaMessage() {
        goalCriteriaMessage = String
                .format("%f %s in a rolling %d day period.",
                        target,
                        units,
                        timeFrame);
    }

    public double getTarget() {
        return target;
    }

    public int getTimeFrame() {
        return timeFrame;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }
    public String getUnits() {
        return units;
    }

    public String getGoalCriteriaMessage() {
        return goalCriteriaMessage;
    }

}
