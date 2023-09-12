package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.DeleteGoalRequest;

import com.nashss.se.momentum.activity.results.DeleteGoalResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.dynamodb.models.Event;

import com.nashss.se.momentum.models.GoalModel;


import javax.inject.Inject;
import java.util.List;


public class DeleteGoalActivity {

    private final GoalDao goalDao;
    private final EventDao eventDao;

    @Inject
    public DeleteGoalActivity(GoalDao goalDao, EventDao eventDao) {
        this.goalDao = goalDao;
        this.eventDao = eventDao;
    }

    public DeleteGoalResult handleRequest(final DeleteGoalRequest deleteGoalRequest) {

        Goal goal = new Goal();
        goal.setUserId(deleteGoalRequest.getUserId());
        goal.setGoalName(deleteGoalRequest.getGoalName());


        List<Event> events = eventDao.getEvents(deleteGoalRequest.getUserId()+deleteGoalRequest.getGoalName());
        events.forEach(eventDao::deleteEvent);
        goalDao.deleteGoal(goal);

        GoalModel goalModel = new ModelConverter().toGoalModel(goal);
        return DeleteGoalResult.builder()
                .withGoal(goalModel)
                .build();

    }

}
