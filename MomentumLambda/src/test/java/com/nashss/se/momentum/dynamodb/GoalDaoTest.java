package com.nashss.se.momentum.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.metrics.MetricsPublisher;
import com.nashss.se.momentum.dynamodb.models.GoalCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class GoalDaoTest {
/*

    Goal goal1;
    String goalName1 = "Reading";
    String userId1 = "griffin.scott88@gmail.com";
    String goalId1 = userId1+goalName1;
    List<GoalCriteria> goalCriteriaList = new ArrayList<>();
    LocalDate startDate1 = LocalDate.of(2023,9,1);
    GoalCriteria goalCriteria1;
    GoalCriteria goalCriteria2;

//    @Mock
    private DynamoDBMapper mapper;
    @Mock
    private MetricsPublisher metricsPublisher;

    private GoalDao goalDao;

    @BeforeEach
    public void setup() {
        openMocks(this);
        goalDao = new GoalDao(new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient(Regions.US_EAST_2)), metricsPublisher);
        goalCriteria1 = new GoalCriteria(90,"minutes", 7, startDate1);
        goalCriteria2 = new GoalCriteria(100, "minutes", 7, LocalDate.now().minusDays(5));
        goalCriteriaList.add(goalCriteria1);
        goalCriteriaList.add(goalCriteria2);
        goal1 = new Goal();
        goal1.setGoalId(goalId1);
        goal1.setGoalName(goalName1);
        goal1.setGoalCriteriaList(goalCriteriaList);
        goal1.setUserId(userId1);
        goal1.setStartDate(startDate1);
    }

    @Test
    public void saveGoal_callsMapperWithGoal() {
        Goal result = goalDao.saveGoal(goal1);

        assertEquals(goal1, result);
    }

    @Test
    public void getGoal_goalFound_returnsGoalModel() {
        Goal result = goalDao.getGoal(userId1, goalName1);

        System.out.println(result.getGoalCriteriaList().get(0).getTimeFrame() + " !!!!!!!!! ");
    }
//
//    @Test
//    public void deleteGoal() {
//        goalDao.deleteGoal(goal1);
//    }

    @Test
    public void getAllGoals() {
        List<Goal> goalList = goalDao.getGoals(goal1.getUserId());
        assertEquals(1, goalList.size());
        for (Goal g : goalList) {
            System.out.println(g.getGoalName() + " !!!!!!!!! ");
        }
    }
*/


}