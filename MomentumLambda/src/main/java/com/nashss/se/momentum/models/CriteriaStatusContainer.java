package com.nashss.se.momentum.models;

public class CriteriaStatusContainer {
    GoalCriteria goalCriteria;
    Double sumOfTimePeriodsMeasurements;
    Boolean inMomentumBool;

    public CriteriaStatusContainer(GoalCriteria goalCriteria, Double sumOfTimePeriodsMeasurements, Boolean inMomentumBool) {
        this.goalCriteria = goalCriteria;
        this.sumOfTimePeriodsMeasurements = sumOfTimePeriodsMeasurements;
        this.inMomentumBool = inMomentumBool;
    }

    public GoalCriteria getGoalCriteria() {
        return goalCriteria;
    }

    public void setGoalCriteria(GoalCriteria goalCriteria) {
        this.goalCriteria = goalCriteria;
    }

    public Double getSumOfTimePeriodsMeasurements() {
        return sumOfTimePeriodsMeasurements;
    }

    public void setSumOfTimePeriodsMeasurements(Double sumOfTimePeriodsMeasurements) {
        this.sumOfTimePeriodsMeasurements = sumOfTimePeriodsMeasurements;
    }

    public Boolean getInMomentumBool() {
        return inMomentumBool;
    }

    public void setInMomentumBool(Boolean inMomentumBool) {
        this.inMomentumBool = inMomentumBool;
    }
}
