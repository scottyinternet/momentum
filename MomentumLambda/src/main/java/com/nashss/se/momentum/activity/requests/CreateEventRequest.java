package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;

@JsonDeserialize(builder = CreateEventRequest.Builder.class)
public class CreateEventRequest {
    private final String goalId;
    private final String eventId;
    private final LocalDate dateOfEvent;
    private final Double measurement;

    private CreateEventRequest(String goalId, String eventId, LocalDate dateOfEvent, Double measurement) {
        this.goalId = goalId;
        this.eventId = eventId;
        this.dateOfEvent = dateOfEvent;
        this.measurement = measurement;
    }


    @Override
    public String toString() {
        return "CreateEventRequest{" +
                "goalId='" + goalId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", dateOfEvent='" + dateOfEvent.toString() + '\'' +
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
        private String eventId;
        private LocalDate dateOfEvent;
        private Double measurement;

        public Builder withGoalID(String goalId) {
            this.goalId = goalId;
            return this;
        }

        public Builder withEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder withDateOfEvent(LocalDate dateOfEvent) {
            this.dateOfEvent = dateOfEvent;
            return this;
        }

        public Builder withMeasurement(Double measurement) {
            this.measurement = measurement;
            return this;
        }

        public CreateEventRequest build() {
            return new CreateEventRequest(goalId, eventId, dateOfEvent, measurement);
        }
    }
}
