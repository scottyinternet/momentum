package com.nashss.se.momentum.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.exceptions.EventNotFoundException;
import com.nashss.se.momentum.metrics.MetricsConstants;
import com.nashss.se.momentum.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.*;

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

    public List<Event> getEvents(String goalId) {
        Event event = new Event();
        event.setGoalId(goalId);

        DynamoDBQueryExpression<Event> dynamoDBQueryExpression = new DynamoDBQueryExpression<Event>()
                .withHashKeyValues(event);

        QueryResultPage<Event> eventQueryResultPage = this.dynamoDBMapper.queryPage(Event.class, dynamoDBQueryExpression);

        List<Event> eventList = new ArrayList<>(eventQueryResultPage.getResults());

        return eventList;
    }

    public Event getEvent(String goalId, String eventId) {

        Event event = dynamoDBMapper.load(Event.class, goalId, eventId);

        if (event == null) {
            metricsPublisher.addCount(MetricsConstants.GETEVENT_EVENTNOTFOUND_COUNT, 1);
            throw new EventNotFoundException("Could not find event with hash " + goalId + "sort " + eventId);
        }
        metricsPublisher.addCount(MetricsConstants.GETEVENT_EVENTNOTFOUND_COUNT, 0);
        return event;
    }

    /**
     * Saves the given event.
     *
     * @param event The event to save
     */
    public void saveEvent(Event event) {
        this.dynamoDBMapper.save(event);
    }

    /**
     * Deletes the given event.
     *
     * @param event The event to delete
     */
    public void deleteEvent(Event event) {
       dynamoDBMapper.delete(event);
    }

    /**
     * @param goal
     * @return List<Events>, if no data found, returns null
     */
    public List<Event> getEventsBetweenDates(Goal goal) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(goal.getTimePeriod() + 1);

        //query GSI dates between today and start date... will return a list of Events
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":goalId", new AttributeValue().withS(goal.getGoalId()));
        valueMap.put(":today", new AttributeValue().withS(today.toString()));
        valueMap.put(":startDate", new AttributeValue().withS(startDate.toString()));

        DynamoDBQueryExpression<Event> queryExpression = new DynamoDBQueryExpression<Event>()
                .withIndexName(Event.GSI_TABLE_NAME)
                .withConsistentRead(false)
                .withKeyConditionExpression("goalId = :goalId AND dateOfEvent BETWEEN :startDate AND :today")
                .withExpressionAttributeValues(valueMap);

        PaginatedQueryList<Event> events = dynamoDBMapper.query(Event.class, queryExpression);
        return events == null ? new ArrayList<>() : events;
    }
}
