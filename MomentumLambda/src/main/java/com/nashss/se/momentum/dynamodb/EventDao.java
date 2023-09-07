package com.nashss.se.momentum.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Playlist;
import com.nashss.se.momentum.exceptions.EventNotFoundException;
import com.nashss.se.momentum.metrics.MetricsConstants;
import com.nashss.se.momentum.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Accesses data for a events using {@link Event} to represent the model in DynamoDB.
 */
@Singleton
public class EventDao {

    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a EventDao object.
     *
     * @param dynamoDBMapper   the {@link DynamoDBMapper} used to interact with the events table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public EventDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    public Queue<Event> getEvents(String goalId) {
        Event event = new Event();
        event.setEventId(goalId);

        DynamoDBQueryExpression<Event> dynamoDBQueryExpression= new DynamoDBQueryExpression<Event>()
                .withHashKeyValues(event);

        QueryResultPage<Event> eventQueryResultPage =  this.dynamoDBMapper.queryPage(Event.class,dynamoDBQueryExpression);

        Queue<Event> eventList = new LinkedList<>(eventQueryResultPage.getResults());

        return eventList;
    }

    public Event getEvent(String goalId, String eventId) {

        Event event = dynamoDBMapper.load(Event.class,goalId,eventId);

        if(event==null){
            metricsPublisher.addCount(MetricsConstants.GETEVENT_EVENTNOTFOUND_COUNT, 1);
            throw new EventNotFoundException("Could not find event with hash " + goalId + "sort " + eventId);
        }
        metricsPublisher.addCount(MetricsConstants.GETEVENT_EVENTNOTFOUND_COUNT,0);
        return event;
    }

    /**
     * Saves the given event.
     *
     * @param event The event to save
     * @return The Event object that was saved
     */
    public Event saveEvent(Event event) {
        this.dynamoDBMapper.save(event);
        return event;
    }

    /**
     * Deletes the given event.
     *
     * @param event The event to delete
     */
    public void deleteEvent(Event event) {
       dynamoDBMapper.delete(event);
    }

}
