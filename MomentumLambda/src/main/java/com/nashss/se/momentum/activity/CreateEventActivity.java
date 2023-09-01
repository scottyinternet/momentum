package com.nashss.se.momentum.activity;


import com.nashss.se.momentum.activity.requests.CreateEventRequest;
import com.nashss.se.momentum.activity.results.CreateEventResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.models.EventModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Implementation of the CreateEventActivity for Momentum's CreateEvent API.
 * <p>
 * This API allows the customer to create a new event attached to a specific goal.
 */
public class CreateEventActivity {

    private final Logger log = LogManager.getLogger();
    private final EventDao eventDao;

    /**
     * Instantiates a new CreateEventActivity object.
     *
     * @param eventDao EventDao to access the events table.
     */
    @Inject
    public CreateEventActivity(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    /**
     * This method handles the incoming request by persisting a new event with
     * a goal ID, Date of Event, and Measurement. It also creates a UUID for the event ID.
     * <p>
     * It then returns the newly created event.
     * @param createEventRequest request object containing the new event's goal ID,
     *                           Date of Event, and Measurement.
     * @return createEventResult result object containing the API defined {@link EventModel}
     */

    public CreateEventResult handleRequest(final CreateEventRequest createEventRequest) {
        log.info("Received CreateEventRequest {}", createEventRequest);

        // validate?

        Event newEvent = new Event();

        newEvent.setGoalId(createEventRequest.getGoalId());
        newEvent.setEventId(UUID.randomUUID().toString());
        newEvent.setDate(createEventRequest.getDateOfEvent());
        newEvent.setMeasurement(createEventRequest.getMeasurement());

        eventDao.saveEvent(newEvent);

        EventModel eventModel = new ModelConverter().toEventModel(newEvent);
        return CreateEventResult.builder()
                .withEvent(eventModel)
                .build();
    }
}
