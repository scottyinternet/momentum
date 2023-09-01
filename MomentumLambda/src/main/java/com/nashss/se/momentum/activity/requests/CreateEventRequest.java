package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;

@JsonDeserialize(builder = CreateEventRequest.Builder.class)
public class CreateEventRequest {
    private final String goalId;
    private final String dateOfEvent;
    private final Double measurement;

    private CreateEventRequest(String goalId, String dateOfEvent, Double measurement) {
        this.goalId = goalId;
        this.dateOfEvent = dateOfEvent;
        this.measurement = measurement;
    }

    public String getGoalId() {
        return goalId;
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
                "goalId='" + goalId + '\'' +
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

        private String goalId;
        private String dateOfEvent;
        private Double measurement;

        public Builder withGoalID(String goalId) {
            this.goalId = goalId;
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
            return new CreateEventRequest(goalId, dateOfEvent, measurement);
        }
    }
}
