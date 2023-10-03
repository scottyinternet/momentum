package com.nashss.se.momentum.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.metrics.MetricsPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class EventDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    @Mock
    private PaginatedQueryList<Event> queryList;
    private EventDao eventDao;

    private Event event;

    @BeforeEach
    void setUp() {
        openMocks(this);
        eventDao = new EventDao(dynamoDBMapper, metricsPublisher);
        event = new Event();
        event.setGoalId("gid1234");
        event.setEventId("eid1234");
        event.setMeasurement(12.0);
        event.setDate(LocalDate.parse("2023-01-01"));
    }
/*
    @Test
    void saveEvent() {
        eventDao.saveEvent(event);
        verify(dynamoDBMapper).save(event);
    }

    @Test
    void deleteEvent(){
         eventDao.deleteEvent(event);
         verify(dynamoDBMapper).delete(event);
    }

    public void getEventsBetweenDates_validInput_returnsListOfEvents() {
        Goal goal = new Goal();
        goal.setUserId("userId");
        goal.setGoalName("goalName");
        goal.setTimePeriod(7);

        DynamoDBQueryExpression<Event> queryExpression = new DynamoDBQueryExpression<Event>();
        when(dynamoDBMapper.query(Event.class, queryExpression)).thenReturn(queryList);

        List<Event> events = eventDao.getEventsBetweenDates(goal);
        assertNotNull(events);
        verify(dynamoDBMapper).query(any(), any());

    }*/
}