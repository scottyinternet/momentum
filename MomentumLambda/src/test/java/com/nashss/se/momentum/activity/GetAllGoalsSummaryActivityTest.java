package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.GetAllGoalsSummaryRequest;
import com.nashss.se.momentum.activity.requests.GetGoalDetailsRequest;
import com.nashss.se.momentum.activity.results.GetAllGoalsSummaryResult;
import com.nashss.se.momentum.activity.results.GetGoalDetailsResult;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventSummary;
import com.nashss.se.momentum.models.GoalSummary;
import com.nashss.se.momentum.models.Status;
import com.nashss.se.momentum.utils.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetAllGoalsSummaryActivityTest {

    @Mock
    private GoalDao goalDao;
    @Mock
    private EventDao eventDao;
    private GetAllGoalsSummaryActivity getAllGoalsSummaryActivity;

    @BeforeEach
    public void setup() {
        openMocks(this);
        getAllGoalsSummaryActivity = new GetAllGoalsSummaryActivity(goalDao, eventDao);
    }
/*
    @Test
    public void handleRequest_userHasTwoGoals_returnsResultWithCorrectGoalsSummary() {
        String UserId = "expectedId";
        String firstGoalName = "FirstGoal";
        String secondGoalName = "SecondGoal";

        Goal goal = new Goal();
        goal.setUserId(UserId);
        goal.setGoalId(firstGoalName);
        goal.setGoalName(firstGoalName);
        goal.setTarget(7);
        goal.setTimePeriod(7);
        goal.setUnit("units");

        Goal secondGoal = new Goal();
        secondGoal.setUserId(UserId);
        secondGoal.setGoalId(secondGoalName);
        secondGoal.setGoalName(secondGoalName);
        secondGoal.setTarget(7);
        secondGoal.setTimePeriod(7);
        secondGoal.setUnit("units");

        Event event = new Event();
        event.setEventId("1234");
        event.setGoalId(secondGoalName);
        event.setDate(LocalDate.now());
        event.setMeasurement(8.0);


        List<Goal> goalList = new ArrayList<>();
        goalList.add(goal);
        goalList.add(secondGoal);

        List<Event> firstGoalEventList = new ArrayList<>();
        List<Event> secondGoalEventList = new ArrayList<>();
        secondGoalEventList.add(event);

        GoalSummary goalSummary = new GoalSummary(firstGoalName,StatusEnum.NO_MOMENTUM);
        GoalSummary secondGoalSummary = new GoalSummary(secondGoalName,StatusEnum.IN_MOMENTUM);
        List<GoalSummary> goalSummaryList = new ArrayList<>();
        goalSummaryList.add(goalSummary);
        goalSummaryList.add(secondGoalSummary);


        when(goalDao.getGoals(UserId)).thenReturn(goalList);
        when(eventDao.getEventsBetweenDates(goal)).thenReturn(firstGoalEventList);
        when(eventDao.getEventsBetweenDates(secondGoal)).thenReturn(secondGoalEventList);

        GetAllGoalsSummaryRequest request = GetAllGoalsSummaryRequest.builder()
                .withUserId(UserId)
                .build();

        GetAllGoalsSummaryResult result = getAllGoalsSummaryActivity.handleRequest(request);

        assertEquals(goalSummaryList, result.getGoalSummary());

    }*/
}
