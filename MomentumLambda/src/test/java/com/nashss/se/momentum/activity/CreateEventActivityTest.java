package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.CreateEventRequest;
import com.nashss.se.momentum.activity.results.CreateEventResult;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class CreateEventActivityTest {
    @Mock
    private EventDao eventDao;

    private CreateEventActivity createEventActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.createEventActivity = new CreateEventActivity(eventDao);
    }

    @Test
    void handleRequest_validRequest_createsAndSavesEvent() {
        String goalName = "myGoal";
        String userId = "1234@fakemail.com";
        Double measurement = 12.0;
        String dateOfEvent = "2023-01-01";

        CreateEventRequest request = CreateEventRequest.builder()
                .withGoalName(goalName)
                .withUserId(userId)
                .withMeasurement(measurement)
                .withDateOfEvent(dateOfEvent)
                .build();

        CreateEventResult result = createEventActivity.handleRequest(request);

        assertNotNull(result.getEventModel().getEventId());
        assertEquals(userId+goalName, result.getEventModel().getGoalId());
        assertEquals(measurement, result.getEventModel().getMeasurement());
        assertEquals(LocalDate.parse(dateOfEvent), result.getEventModel().getDateOfEvent());
        verify(eventDao).saveEvent(any(Event.class));
    }
}