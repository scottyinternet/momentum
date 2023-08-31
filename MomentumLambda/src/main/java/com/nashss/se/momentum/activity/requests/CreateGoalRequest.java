package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;
@JsonDeserialize(builder = CreateGoalRequest.Builder.class)
public class CreateGoalRequest {

    private final String goalId;
    private final String userId;
    private final Integer timePeriod;
    private final Integer target;

    private final String unit;
    private final String goalName;

    public CreateGoalRequest(String goalId, String userId, Integer timePeriod, Integer target, String unit, String goalName) {
        this.goalId = goalId;
        this.userId = userId;
        this.timePeriod = timePeriod;
        this.target = target;
        this.unit = unit;
        this.goalName = goalName;
    }

    public String getGoalId() {
        return goalId;
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
                "goalId='" + goalId + '\'' +
                ", userId='" + userId + '\'' +
                ", timePeriod=" + timePeriod +
                ", target=" + target +
                ", unit='" + unit + '\'' +
                ", goalName='" + goalName + '\'' +
                '}';
    }

    public static CreatePlaylistRequest.Builder builder() {
        return new CreatePlaylistRequest.Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String goalId;
        private String userId;
        private  Integer timePeriod;
        private  Integer target;

        private  String unit;
        private  String goalName;

        public CreateGoalRequest.Builder withGoalId(String goalId) {
            this.goalId = goalId;
            return this;
        }

        public CreateGoalRequest.Builder withUserId(String userId) {
            this.userId  = userId;
            return this;
        }

        public CreateGoalRequest.Builder withTimePeriod(Integer timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        public CreateGoalRequest.Builder withTarget(Integer target) {
            this.target= target;
            return this;
        }
        public CreateGoalRequest.Builder withUnit(String unit) {
            this.unit= unit;
            return this;
        }
        public CreateGoalRequest.Builder withGoalName(String goalName) {
            this.goalName= goalName;
            return this;
        }

        public CreateGoalRequest build() {
            return new CreateGoalRequest(goalId,userId, timePeriod,target,unit,goalName);
        }
    }

}
