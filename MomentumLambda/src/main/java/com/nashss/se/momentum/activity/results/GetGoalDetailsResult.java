package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.Status;

import java.util.List;

public class GetGoalDetailsResult {

    private final Status status;
    private final List<EventModel> eventList;
    private Integer target;
    private String unit;
    private String goalName;


    private GetGoalDetailsResult(Status status, Integer target, String unit, String goalName, List<EventModel> eventList){
        this.status = status;
        this.goalName = goalName;
        this.target = target;
        this.unit = unit;
        this.eventList = eventList;
    }

    public Status getStatus() {
        return status;
    }

    public List<EventModel> getEventList() {
        return eventList;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Status status;
        private List<EventModel> eventList;
        private Integer target;
        private String unit;
        private String goalName;


        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder withTarget(Integer target) {
            this.target = target;
            return this;
        }

        public Builder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public Builder withEventList(List<EventModel> eventList) {
            this.eventList = eventList;
            return this;
        }

        public GetGoalDetailsResult build() {
            return new GetGoalDetailsResult(status, target, unit, goalName, eventList);
        }
    }
}
