package com.nashss.se.momentum.models;

import java.time.LocalDate;
import java.util.Objects;


public class EventModel {

    private String goalId;
    private String eventId;
    private LocalDate dateOfEvent;
    private Double measurement;

    private EventModel(String goalId, String eventId, LocalDate dateOfEvent, Double measurement){
        this.goalId = goalId;
        this.eventId = eventId;
        this.dateOfEvent = dateOfEvent;
        this.measurement = measurement;
    }

    public String getGoalId() {
        return goalId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDate getDateOfEvent() {
        return dateOfEvent;
    }

    public Double getMeasurement() {
        return measurement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventModel that = (EventModel) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String goalId;
        private String eventId;
        private LocalDate dateOfEvent;
        private Double measurement;

        public Builder withGoalId(String goalId) {
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

        public EventModel build() {return new EventModel(goalId,eventId,dateOfEvent,measurement);}
    }
}
