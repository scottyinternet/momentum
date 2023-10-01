package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.UpdateGoalRequest;
import com.nashss.se.momentum.activity.results.UpdateGoalResult;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateGoalActivityTests {

    @Mock
    private GoalDao goalDao;



    private UpdateGoalActivity updateGoalActivity;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        updateGoalActivity = new UpdateGoalActivity(goalDao);
    }

    @Test
    public void handleRequest_goodRequest_updatesGoalName() {
        // GIVEN
        String id = "joehailem1@gmail.com";
        String goalName = "study";



        UpdateGoalRequest request = UpdateGoalRequest.builder()
                .withUserId(id)
                .withGoalName(goalName)
                .withTarget(2)
                .build();

        Goal startingGoal = new Goal();
        startingGoal.setTimePeriod(2);
        startingGoal.setGoalName("study");


        when(goalDao.getGoal(id,goalName)).thenReturn(startingGoal);
        when(goalDao.saveGoal(startingGoal)).thenReturn(startingGoal);

        // WHEN
        UpdateGoalResult result = updateGoalActivity.handleRequest(request);

        // THEN
        assertEquals(goalName, result.getGoal().getGoalName());
        assertEquals(2, result.getGoal().getCurrentGoalCriterion().getTarget());

    }
}
