package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.dynamodb.GoalDao;

import javax.inject.Inject;

public class CreateGoalActivity {

    private final GoalDao goalDao;

    @Inject
    public CreateGoalActivity (GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public CreateGoalResult handleRequest(final CreateGoalRequest)

}
