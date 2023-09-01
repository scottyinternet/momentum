package com.nashss.se.momentum.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.metrics.MetricsPublisher;
import com.nashss.se.momentum.models.EventModel;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

class EventDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    private EventDao eventDao;

    @BeforeEach
    void setUp() {
        initMocks(this);
        eventDao = new EventDao(dynamoDBMapper, metricsPublisher);
    }

    @Test
    void saveEvent() {
        Event event = new Event();
        event.setGoalId("gid1234");
        event.setEventId("eid1234");
        event.setMeasurement(12.0);
        event.setDate(LocalDate.parse("2023-01-01"));
        eventDao.saveEvent(event);
        verify(dynamoDBMapper).save(event);
    }


}