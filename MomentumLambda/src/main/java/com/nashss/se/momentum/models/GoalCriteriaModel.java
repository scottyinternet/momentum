package com.nashss.se.momentum.models;

import java.time.LocalDate;

public class GoalCriteriaModel {

    private int target;
    private int timeFrame;
    private String units;
    private LocalDate effectiveDate;

    public GoalCriteriaModel(int target, String units, int timeFrame, LocalDate effectiveDate) {
        this.target = target;
        this.units = units;
        this.timeFrame = timeFrame;
        this.effectiveDate = effectiveDate;
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
    public String getUnits() {
        return units;
    }
    public String getGoalCriteriaMessage() {
        return String.format("%d %s in a %d day rolling period",
                target,
                units,
                timeFrame);
    }
}
