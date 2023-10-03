package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CreateGoalRequest.Builder.class)
public class CreateGoalRequest {

    private final String goalName;
    private final String userId;
    private final String startDate;
    private final Integer goalCritTarget;
    private final Integer goalCritTimeperiod;
    private final String goalCritUnit;
    private final String goalCritEffectiveDate;

    public CreateGoalRequest(String goalName,
                             String userId,
                             String startDate,
                             Integer goalCritTarget,
                             Integer goalCritTimeperiod,
                             String goalCritUnit,
                             String goalCritEffectiveDate) {
        this.goalName = goalName;
        this.userId = userId;
        this.startDate = startDate;
        this.goalCritTarget = goalCritTarget;
        this.goalCritTimeperiod = goalCritTimeperiod;
        this.goalCritUnit = goalCritUnit;
        this.goalCritEffectiveDate = goalCritEffectiveDate;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getUserId() {
        return userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public Integer getGoalCritTarget() {
        return goalCritTarget;
    }

    public Integer getGoalCritTimeperiod() {
        return goalCritTimeperiod;
    }

    public String getGoalCritUnit() {
        return goalCritUnit;
    }

    public String getGoalCritEffectiveDate() {
        return goalCritEffectiveDate;
    }

    @Override
    public String toString() {
        return "CreateGoalRequest...";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String goalName;
        private String userId;
        private String startDate;
        private Integer goalCritTarget;
        private Integer goalCritTimeperiod;
        private String goalCritUnit;
        private String goalCritEffectiveDate;

        public Builder withGoalName(String goalName){
            this.goalName = goalName;
            return this;
        }

        public Builder withUserId(String userId){
            this.userId = userId;
            return this;
        }

        public Builder withStartDate(String startDate){
            this.startDate = startDate;
            return this;
        }

        public Builder withGoalCritTarget(Integer goalCritTarget){
            this.goalCritTarget = goalCritTarget;
            return this;
        }

        public Builder withGoalCritTimeperiod(Integer goalCritTimeperiod){
            this.goalCritTimeperiod = goalCritTimeperiod;
            return this;
        }

        public Builder withGoalCritUnit(String goalCritUnit){
            this.goalCritUnit = goalCritUnit;
            return this;
        }

        public Builder withGoalCritEffectiveDate(String goalCritEffectiveDate){
            this.goalCritEffectiveDate = goalCritEffectiveDate;
            return this;
        }

        public CreateGoalRequest build() {
            return new CreateGoalRequest(
                    goalName,
                    userId,
                    startDate,
                    goalCritTarget,
                    goalCritTimeperiod,
                    goalCritUnit,
                    goalCritEffectiveDate
            );
        }

    }
}
