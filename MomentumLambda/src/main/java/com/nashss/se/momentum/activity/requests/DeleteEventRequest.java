package com.nashss.se.momentum.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;


@JsonDeserialize(builder = DeleteEventRequest.Builder.class)
public class DeleteEventRequest {

    private final String goalId;
    private final String eventId;

    private DeleteEventRequest(String goalId, String eventId) {
        this.goalId = goalId;
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getGoalId() {return goalId; }

    @Override
    public String toString() {
        return "DeleteEventRequest{" +
                "goalId='" + goalId + '\'' +
                ", eventId='" + eventId +
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

        public Builder withGoalId(String goalId) {
            this.goalId = goalId;
            return this;
        }

        public Builder withEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public DeleteEventRequest build() {
            return new DeleteEventRequest(goalId, eventId);
        }
    }
}

