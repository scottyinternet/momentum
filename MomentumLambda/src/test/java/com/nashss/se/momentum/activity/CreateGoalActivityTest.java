package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.CreateGoalRequest;
import com.nashss.se.momentum.activity.results.CreateGoalResult;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateGoalActivityTest {

    @Mock
    private GoalDao goalDao;

    private CreateGoalActivity createGoalActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createGoalActivity = new CreateGoalActivity(goalDao);
    }

    @Test
    public void handleRequest_withFullData_createsAndSavesGoalObject() {
        // GIVEN

        String goalName = "StudyBehaviour";
        String userId ="55555";
        Integer timePeriod = 2;
        Integer target =3;
        String unit ="HOURS";


        CreateGoalRequest request = CreateGoalRequest.builder()
                .withGoalName(goalName)
                .withUserId(userId)
                .withTimePeriod(timePeriod)
                .withTarget(3)
                .withUnit(unit)
                .build();

        // WHEN
        CreateGoalResult  result = createGoalActivity.handleRequest(request);

        // THEN
        verify(goalDao).saveGoal(any(Goal.class));
/*
        assertNotNull(result.getGoal().getGoalId());
        assertEquals(goalName, result.getGoal().getGoalName());
        assertEquals(userId+goalName, result.getGoal().getGoalId());
        assertEquals(userId, result.getGoal().getUserId());
        assertEquals(timePeriod, result.getGoal().getTimePeriod());
        assertEquals(target, result.getGoal().getTarget());
        assertEquals(unit, result.getGoal().getUnit());*/

    }

}
