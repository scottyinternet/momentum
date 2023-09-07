package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.Status;

import java.util.List;

public class GetGoalDetailsResult {

    private final Status status;
    private final List<EventModel> eventList;
    private final String message;
    private final String goalName;

    private GetGoalDetailsResult(Status status, List<EventModel> eventList, String message, String goalName){
        this.status = status;
        this.message = message;
        this.eventList = eventList;
        this.goalName = goalName;
    }

    public Status getStatus() {
        return status;
    }

    public List<EventModel> getEventList() {
        return eventList;
    }

    public String getMessage() {
        return message;
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
        private String message;
        private String goalName;


        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder withEventList(List<EventModel> eventList) {
            this.eventList = eventList;
            return this;
        }

        public Builder withMessage(String message) {
            this.eventList = eventList;
            return this;
        }

        public Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public GetGoalDetailsResult build() {
            return new GetGoalDetailsResult(status, eventList, message, goalName);
        }
    }
}
