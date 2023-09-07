package com.nashss.se.momentum.activity;


import com.nashss.se.momentum.activity.requests.DeleteEventRequest;
import com.nashss.se.momentum.activity.results.DeleteEventResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.models.EventModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the DeleteEventActivity for Momentum's DeleteEvent API.
 * <p>
 * This API allows the customer to delete an existing event.
 */
public class DeleteEventActivity {

    private final Logger log = LogManager.getLogger();
    private final EventDao eventDao;

    /**
     * Instantiates a new DeleteEventActivity object.
     *
     * @param eventDao EventDao to access the events table.
     */
    @Inject
    public DeleteEventActivity(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    /**
     * This method handles the incoming request by deleting the event
     * <p>
     * It then builds the result with an EventModel.
     * @param DeleteEventRequest request object containing the event's goal ID,
     *                           and event ID.
     * @return DeleteEventResult result object containing the API defined {@link EventModel}
     */

    public DeleteEventResult handleRequest(final DeleteEventRequest DeleteEventRequest) {
        log.info("Received DeleteEventRequest {}", DeleteEventRequest);

        Event newEvent = new Event();

        newEvent.setGoalId(DeleteEventRequest.getGoalId());
        newEvent.setEventId(DeleteEventRequest.getEventId());

        eventDao.deleteEvent(newEvent);

        EventModel eventModel = new ModelConverter().toEventModel(newEvent);
        return DeleteEventResult.builder()
                .withEvent(eventModel)
                .build();
    }
}
