package com.nashss.se.momentum.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GoalDao {

    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    @Inject
    public GoalDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    public Goal saveGoal(Goal goal) {
        this.dynamoDBMapper.save(goal);
        return goal;
    }

    public Goal getGoal(String userId, String goalName) {
        return this.dynamoDBMapper.load(Goal.class, userId, goalName);
    }

    public List<Goal> getGoals(String userId) {
        Goal goalPartition = new Goal();
        goalPartition.setUserId(userId);

        DynamoDBQueryExpression<Goal> dynamoDBQueryExpression = new DynamoDBQueryExpression<Goal>()
                .withHashKeyValues(goalPartition);

        return this.dynamoDBMapper.query(Goal.class, dynamoDBQueryExpression);
    }
    
    public Goal updateGoal(Goal goal){
        this.dynamoDBMapper.save(goal);
        return goal;

    }
}
