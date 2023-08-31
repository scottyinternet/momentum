package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.CreateGoalRequest;
import com.nashss.se.momentum.activity.results.CreateGoalResult;

import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Goal;

import com.nashss.se.momentum.models.GoalModel;


import javax.inject.Inject;


public class CreateGoalActivity {

    private final GoalDao goalDao;

    @Inject
    public CreateGoalActivity (GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public CreateGoalResult handleRequest(final CreateGoalRequest createGoalRequest){

        Goal newGoal = new Goal();
        newGoal.setGoalId(createGoalRequest.getGoalId());
        newGoal.setGoalName(createGoalRequest.getGoalName());
        newGoal.setUserId(createGoalRequest.getUserId());
        newGoal.setTimePeriod(createGoalRequest.getTimePeriod());
        newGoal.setTarget(createGoalRequest.getTarget());
        newGoal.setUnit(createGoalRequest.getUnit());
       goalDao.saveGoal(newGoal);

        GoalModel goalModel = new ModelConverter().toGoalModel(newGoal);
        return CreateGoalResult.builder()
                .withGoal(goalModel)
                .build();

    }

}
