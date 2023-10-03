package com.nashss.se.momentum.converters;

import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.GoalModel;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.models.EventModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts between Data and API models.
 */
public class ModelConverter {

    public GoalModel toGoalModel(Goal goal) {
        return new GoalModel(goal);
    }

    /**
     * Converts an Event to an EventModel.
     *
     * @param event The Event to convert to EventModel
     * @return The converted EventModel
     */
    public EventModel toEventModel(Event event) {
        EventModel eventModel = EventModel.builder()
                .withGoalId(event.getGoalId())
                .withEventId(event.getEventId())
                .withDateOfEvent(event.getDate())
                .withMeasurement(event.getMeasurement())
                .build();
        return eventModel;
    }
}
