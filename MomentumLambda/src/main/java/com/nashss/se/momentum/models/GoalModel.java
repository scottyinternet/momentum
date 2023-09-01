package com.nashss.se.momentum.models;

import java.util.Objects;

public class GoalModel {

    private final String goalId;
    private final String userId;
    private final String goalName;
    private final Integer timePeriod;
    private final Integer target;
    private final String unit;

    public GoalModel(String goalId, String userId, String goalName, Integer timePeriod,
                     Integer target, String unit) {
        this.goalId = goalId;
        this.userId = userId;
        this.goalName = goalName;
        this.timePeriod = timePeriod;
        this.target = target;
        this.unit = unit;
    }

    public String getGoalId() {
        return this.goalId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getGoalName() {
        return this.goalName;
    }

    public Integer getTimePeriod() {
        return this.timePeriod;
    }

    public Integer getTarget() {
        return this.target;
    }

    public String getUnit() {
        return this.unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GoalModel goalModel = (GoalModel) o;

        return Objects.equals(goalId, goalModel.goalId) && Objects.equals(userId, goalModel.userId) &&
                Objects.equals(goalName, goalModel.goalName) &&
                Objects.equals(timePeriod, goalModel.timePeriod) &&
                Objects.equals(target, goalModel.target) &&
                Objects.equals(unit, goalModel.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalId, userId, goalName, timePeriod, target, unit);
    }

    public static GoalModel.Builder builder() {
        return new GoalModel.Builder();
    }

    public static class Builder {
        private String userId;
        private String goalName;
        private String goalId;
        private Integer timePeriod;
        private Integer target;
        private String unit;

        public GoalModel.Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GoalModel.Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public GoalModel.Builder withGoalId(String goalId) {
            this.goalId = goalId;
            return this;
        }

        public GoalModel.Builder withTimePeriod(Integer timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        public GoalModel.Builder withTarget(Integer target) {
            this.target = target;
            return this;
        }

        public GoalModel.Builder withUnit(String unit) {
            this.unit = unit;
            return this;
        }


        public GoalModel build() {
            return new GoalModel(goalId,userId, goalName,  timePeriod, target, unit);
        }
    }
}
