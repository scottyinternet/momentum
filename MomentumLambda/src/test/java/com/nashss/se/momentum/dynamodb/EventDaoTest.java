package com.nashss.se.momentum.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.nashss.se.momentum.dependency.DaoModule;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.metrics.MetricsPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class EventDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;

    private PaginatedQueryList<Event> queryList;
    private EventDao eventDao;
    private Event event;

    @BeforeEach
    void setUp() {
        openMocks(this);
        eventDao = new EventDao(dynamoDBMapper, metricsPublisher);
        event = new Event();
        event.setGoalId("user69@fakemail.com:ThrowingRocks");
        event.setEventId("uuid6969");
        event.setMeasurement(12.0);
        event.setDate(LocalDate.parse("2023-01-01"));
    }
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
}