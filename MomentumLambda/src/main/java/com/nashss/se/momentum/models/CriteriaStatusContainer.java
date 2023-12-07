package com.nashss.se.momentum.models;

public class CriteriaStatusContainer {
    GoalCriteriaModel goalCriteria;
    Double sumNMeasurements;
    Boolean inMomentum;

    public CriteriaStatusContainer(GoalCriteriaModel goalCriteria, Double sumNMeasurements, Boolean inMomentum) {
        this.goalCriteria = goalCriteria;
        this.sumNMeasurements = sumNMeasurements;
        this.inMomentum = inMomentum;
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

    public Boolean getInMomentum() {
        return inMomentum;
    }

    public void setInMomentum(Boolean inMomentum) {
        this.inMomentum = inMomentum;
    }
}
