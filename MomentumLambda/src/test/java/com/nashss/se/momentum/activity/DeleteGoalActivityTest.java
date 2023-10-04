package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.DeleteGoalRequest;
import com.nashss.se.momentum.activity.results.DeleteGoalResult;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.dynamodb.models.GoalCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DeleteGoalActivityTest {
    @Mock
    private GoalDao goalDao;

    @Mock
    private EventDao eventDao;

    private DeleteGoalActivity deleteGoalActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.deleteGoalActivity = new DeleteGoalActivity(goalDao, eventDao);
    }

    @Test
    void handleRequest_validRequest_deletesGoalAndEvents() {
        String goalName = "Cardio";
        String userId = "user1@fakemail.com";
        String goalId = userId + goalName;
        GoalCriteria goalCriteria = new GoalCriteria(10, "minutes", 7, LocalDate.now().minusDays(10));
        List<GoalCriteria> goalCriteriaList = new ArrayList<>();
        goalCriteriaList.add(goalCriteria);

        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setGoalName(goalName);
        goal.setGoalCriteriaList(goalCriteriaList);

        Event event = new Event();
        event.setEventId("1234");
        event.setGoalId(goalId);

        List<Event> goalEventList = new ArrayList<>();
        goalEventList.add(event);

        when(eventDao.getEvents(goalId)).thenReturn(goalEventList);

        DeleteGoalRequest request = DeleteGoalRequest.builder()
                .withUserId(userId)
                .withGoalName(goalName)
                .build();

        DeleteGoalResult result = deleteGoalActivity.handleRequest(request);

        assertEquals(goalName, result.getGoalInfo().getGoalName());
        assertEquals(userId, result.getGoalInfo().getUserId());
        verify(goalDao).deleteGoal(any(Goal.class));
        verify(eventDao).deleteEvent(any(Event.class));
    }
}