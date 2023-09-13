package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;


@JsonDeserialize(builder = CreateEventRequest.Builder.class)
public class CreateEventRequest {
    private final String goalName;
    private final String userId;
    private final String dateOfEvent;
    private final Double measurement;

    private CreateEventRequest(String goalName, String userId, String dateOfEvent, Double measurement) {
        this.goalName = goalName;
        this.userId = userId;
        this.dateOfEvent = dateOfEvent;
        this.measurement = measurement;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDateOfEvent() {
        return dateOfEvent;
    }

    public Double getMeasurement() {
        return measurement;
    }

    @Override
    public String toString() {
        return "CreateEventRequest{" +
                "goalName='" + goalName + '\'' +
                ", userId='" + userId + '\'' +
                ", dateOfEvent='" + dateOfEvent + '\'' +
                ", measurement=" + measurement +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String goalName;
        private String userId;

        private String dateOfEvent;
        private Double measurement;

        public Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withDateOfEvent(String dateOfEvent) {
            this.dateOfEvent = dateOfEvent;
            return this;
        }

        public Builder withMeasurement(Double measurement) {
            this.measurement = measurement;
            return this;
        }

        public CreateEventRequest build() {
            return new CreateEventRequest(goalName, userId, dateOfEvent, measurement);
        }
    }
}
