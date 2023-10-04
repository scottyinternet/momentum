package com.nashss.se.momentum.models;

public class CriteriaStatusContainer {
    GoalCriteriaModel goalCriteria;
    Double sumNMeasurements;
    Boolean inMomentumBool;

    public CriteriaStatusContainer(GoalCriteriaModel goalCriteria, Double sumNMeasurements, Boolean inMomentumBool) {
        this.goalCriteria = goalCriteria;
        this.sumNMeasurements = sumNMeasurements;
        this.inMomentumBool = inMomentumBool;
    }

    public GoalCriteriaModel getGoalCriteria() {
        return goalCriteria;
    }

    public void setGoalCriteria(GoalCriteriaModel goalCriteria) {
        this.goalCriteria = goalCriteria;
    }

    public Double getSumNMeasurements() {
        return sumNMeasurements;
    }

    public void setSumNMeasurements(Double sumNMeasurements) {
        this.sumNMeasurements = sumNMeasurements;
    }

    public Boolean getInMomentumBool() {
        return inMomentumBool;
    }

    public void setInMomentumBool(Boolean inMomentumBool) {
        this.inMomentumBool = inMomentumBool;
    }
}
