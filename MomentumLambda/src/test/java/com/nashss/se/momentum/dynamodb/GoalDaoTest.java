package com.nashss.se.momentum.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GoalDaoTest {

    @Mock
    private DynamoDBMapper mapper;
    @Mock
    private MetricsPublisher metricsPublisher;

    private GoalDao goalDao;

    @BeforeEach
    public void setup() {
        openMocks(this);
        goalDao = new GoalDao(mapper, metricsPublisher);
    }

    @Test
    public void saveGoal_callsMapperWithGoal() {
        //GIVEN
        Goal goal = new Goal();

        //WHEN
        Goal result = goalDao.saveGoal(goal);

        //THEN
        verify(mapper).save(goal);
        assertEquals(goal, result);
    }

    @Test
    public void getGoal_goalFound_returnsGoalModel() {
        String userId = "userId";
        String goalName = "goalName";
        when(mapper.load(Goal.class, userId, goalName)).thenReturn(new Goal());

        Goal goal = goalDao.getGoal(userId, goalName);

        assertNotNull(goal);
        verify(mapper).load(Goal.class, userId, goalName);
    }
}