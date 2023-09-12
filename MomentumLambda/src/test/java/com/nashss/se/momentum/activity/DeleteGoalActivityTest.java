package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.DeleteGoalRequest;
import com.nashss.se.momentum.activity.results.DeleteGoalResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.GoalModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

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
        String goalName = "goal";
        String userId = "user";
        String goalId = userId + goalName;

        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setGoalName(goalName);

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
        GoalModel goalModel = new ModelConverter().toGoalModel(goal);

        Assertions.assertEquals(goalModel,result.getGoalModel());
        verify(goalDao).deleteGoal(any(Goal.class));
        verify(eventDao).deleteEvent(any(Event.class));
    }
}