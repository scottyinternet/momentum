package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.GetGoalDetailsRequest;
import com.nashss.se.momentum.activity.results.GetGoalDetailsResult;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventSummary;
import com.nashss.se.momentum.models.Status;
import com.nashss.se.momentum.utils.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetGoalDetailsActivityTest {

    @Mock
    private GoalDao goalDao;
    @Mock
    private EventDao eventDao;
    private GetGoalDetailsActivity getGoalDetailsActivity;

    @BeforeEach
    public void setup() {
        openMocks(this);
        getGoalDetailsActivity = new GetGoalDetailsActivity(goalDao, eventDao);
    }
/*
    @Test
    public void handleRequest_goalFoundNoEvents_returnsEmptyGetGoalDetailsResult() {
        String expectedUserId = "expectedId";
        String expectedGoalName = "expectedName";
        int expectedSize = 8;
        Goal goal = new Goal();
        goal.setGoalId(expectedGoalName);
        goal.setGoalName(expectedGoalName);
        goal.setTarget(8);
        goal.setTimePeriod(7);

        List<Event> eventList = new ArrayList<>();

        StatusEnum statusEnum = StatusEnum.NO_MOMENTUM;
        String statusMessage = "nice";
        Double sum = 0.0;
        List<EventSummary> eventSummaryList = new ArrayList<>();

        Status status = new Status(statusEnum, statusMessage, sum);

        when(goalDao.getGoal(expectedUserId, expectedGoalName)).thenReturn(goal);
        when(eventDao.getEventsBetweenDates(goal)).thenReturn(eventList);

        GetGoalDetailsRequest request = GetGoalDetailsRequest.builder()
                .withUserId(expectedUserId)
                .withGoalName(expectedGoalName)
                .build();

        GetGoalDetailsResult result = getGoalDetailsActivity.handleRequest(request);

        assertEquals(expectedGoalName, result.getGoalDetailsModel().getGoalName());
        assertEquals(status.getStatusEnum(), result.getGoalDetailsModel().getStatus().getStatusEnum());
        assertEquals(sum, result.getGoalDetailsModel().getStatus().getSum());
    }*/
}
