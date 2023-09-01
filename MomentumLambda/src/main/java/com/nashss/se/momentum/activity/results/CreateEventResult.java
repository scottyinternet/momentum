package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.EventModel;

public class CreateEventResult {

    private final EventModel event;

    private CreateEventResult(EventModel eventModel){this.event = eventModel;}

    public EventModel getEventModel(){return event;}

    @Override
    public String toString(){
        return "CreateEventResult{" +
                "event=" + event +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private EventModel event;

        public Builder withEvent(EventModel event) {
            this.event = event;
            return this;
        }

        public CreateEventResult build() {return new CreateEventResult(event);}
    }
}
