package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.GetGoalDetailsRequest;
import com.nashss.se.momentum.activity.results.GetGoalDetailsResult;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Goal;

import javax.inject.Inject;

public class GetGoalDetailsActivity {

    private final GoalDao goalDao;
    private final EventDao eventDao;

    @Inject
    public GetGoalDetailsActivity(GoalDao goalDao, EventDao eventDao) {
        this.goalDao = goalDao;
        this.eventDao = eventDao;
    }

    public GetGoalDetailsResult handleRequest(final GetGoalDetailsRequest getGoalDetailsRequest) {
        String requestedUserId = getGoalDetailsRequest.getUserId();
        String requestedGoalName = getGoalDetailsRequest.getGoalName();
        Goal newGoal = goalDao.getGoal(requestedUserId, requestedGoalName);

        eventDao.getEventsBetweenDates(newGoal);

        return GetGoalDetailsResult.builder()

                .build();
    }
}

