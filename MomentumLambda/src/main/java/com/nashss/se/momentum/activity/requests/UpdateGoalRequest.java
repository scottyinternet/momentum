package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
@JsonDeserialize(builder = UpdateGoalRequest.Builder.class)
public class UpdateGoalRequest {
    private final String userId;
    private final Integer timePeriod;
    private final Integer target;
    private final String unit;
    private final String goalName;

    public UpdateGoalRequest (String userId, Integer timePeriod, Integer target, String unit, String goalName) {

        this.userId = userId;
        this.timePeriod = timePeriod;
        this.target = target;
        this.unit = unit;
        this.goalName = goalName;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getTimePeriod() {
        return timePeriod;
    }

    public Integer getTarget() {
        return target;
    }

    public String getUnit() {
        return unit;
    }

    public String getGoalName() {
        return goalName;
    }

    public static UpdateGoalRequest.Builder builder() {
        return new UpdateGoalRequest.Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String userId;
        private Integer timePeriod;
        private Integer target;
        private String unit;
        private String goalName;

        public UpdateGoalRequest.Builder withUserId(String id) {
            this.userId = id;
            return this;
        }

        public UpdateGoalRequest.Builder withGoalName(String name) {
            this.goalName = name;
            return this;
        }

        public UpdateGoalRequest.Builder withUnit(String unit) {
            this.unit= unit;
            return this;
        }

        public UpdateGoalRequest.Builder withTimePeriod(Integer timePeriod) {
            this.timePeriod= timePeriod;
            return this;
        }

        public UpdateGoalRequest.Builder withTarget(Integer target) {
            this.target= target;
            return this;
        }


        public UpdateGoalRequest build() {
            return new UpdateGoalRequest(userId, timePeriod, target,unit, goalName);
        }
    }

}
