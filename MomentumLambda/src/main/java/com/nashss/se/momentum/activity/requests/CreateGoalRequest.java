package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.nashss.se.momentum.utils.UnitOfMeasurement;

import java.time.LocalDate;

@JsonDeserialize(builder = CreateGoalRequest.Builder.class)
public class CreateGoalRequest {


    private final String userId;
    private final Integer timePeriod;
    private final Integer target;
    private final String unit;
    private final String goalName;

    public CreateGoalRequest(String userId, Integer timePeriod, Integer target, String unit, String goalName) {

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

    @Override
    public String toString() {
        return "CreateGoalRequest{" +
                ", userId='" + userId + '\'' +
                ", timePeriod=" + timePeriod +
                ", target=" + target +
                ", unit='" + unit + '\'' +
                ", goalName='" + goalName + '\'' +
                '}';
    }

    public static CreateGoalRequest.Builder builder() {
        return new CreateGoalRequest.Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String userId;
        private Integer timePeriod;
        private Integer target;
        private String unit;
        private String goalName;


        public CreateGoalRequest.Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public CreateGoalRequest.Builder withTimePeriod(Integer timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        public CreateGoalRequest.Builder withTarget(Integer target) {
            this.target = target;
            return this;
        }

        public CreateGoalRequest.Builder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public CreateGoalRequest.Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public CreateGoalRequest build() {
            return new CreateGoalRequest(userId, timePeriod, target, unit, goalName);
        }
    }

}
