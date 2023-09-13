package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;


@JsonDeserialize(builder = CreateGoalRequest.Builder.class)
public class DeleteGoalRequest {

    private final String userId;
    private final String goalName;

    public DeleteGoalRequest(String userId, String goalName) {
        this.userId = userId;
        this.goalName = goalName;
    }


    public String getUserId() {
        return userId;
    }

    public String getGoalName() {
        return goalName;
    }

    @Override
    public String toString() {
        return "CreateGoalRequest{" +
                ", userId='" + userId + '\'' +
                ", goalName='" + goalName + '\'' +
                '}';
    }

    public static DeleteGoalRequest.Builder builder() {
        return new DeleteGoalRequest.Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String userId;
        private String goalName;


        public DeleteGoalRequest.Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public DeleteGoalRequest.Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public DeleteGoalRequest build() {
            return new DeleteGoalRequest(userId, goalName);
        }
    }

}
