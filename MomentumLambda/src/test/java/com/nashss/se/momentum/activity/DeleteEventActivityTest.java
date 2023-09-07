package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.CreateEventRequest;
import com.nashss.se.momentum.activity.requests.DeleteEventRequest;
import com.nashss.se.momentum.activity.results.CreateEventResult;
import com.nashss.se.momentum.activity.results.DeleteEventResult;
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

class DeleteEventActivityTest {
    @Mock
    private EventDao eventDao;

    private DeleteEventActivity deleteEventActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.deleteEventActivity = new DeleteEventActivity(eventDao);
    }

    @Test
    void handleRequest_validRequest_deletesEvent() {
        String goalId = "1234";
        String eventId = "1234";

        DeleteEventRequest request = DeleteEventRequest.builder()
                .withGoalId("1234")
                .withEventId("1234")
                .build();

        DeleteEventResult result = deleteEventActivity.handleRequest(request);

        assertNotNull(result.getEventModel().getEventId());
        assertEquals(eventId, result.getEventModel().getEventId());

        verify(eventDao).deleteEvent(any(Event.class));
    }
}
