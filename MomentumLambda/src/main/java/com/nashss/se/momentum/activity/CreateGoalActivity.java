package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.CreateGoalRequest;
import com.nashss.se.momentum.activity.results.CreateGoalResult;

import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Goal;

import com.nashss.se.momentum.models.GoalCriteria;
import com.nashss.se.momentum.models.GoalModel;


import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CreateGoalActivity {

    private final GoalDao goalDao;

    @Inject
    public CreateGoalActivity(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public CreateGoalResult handleRequest(final CreateGoalRequest createGoalRequest) {

        Goal newGoal = makeNewGoal(createGoalRequest);
        goalDao.saveGoal(newGoal);

        GoalModel goalModel = new ModelConverter().toGoalModel(newGoal);
        return CreateGoalResult.builder()
                .withGoal(goalModel)
                .build();
    }

    private Goal makeNewGoal(CreateGoalRequest createGoalRequest) {
        Goal newGoal = new Goal();
        newGoal.setUserId(createGoalRequest.getUserId());
        newGoal.setGoalName(createGoalRequest.getGoalName());
        newGoal.setGoalId(createGoalRequest.getUserId()+createGoalRequest.getGoalName());
        newGoal.setStartDate(LocalDate.parse(createGoalRequest.getStartDate()));
        newGoal.setGoalCriteriaList(makeGoalCriteriaList(createGoalRequest));
        return newGoal;
    }

    private List<GoalCriteria> makeGoalCriteriaList(CreateGoalRequest createGoalRequest) {
        List<GoalCriteria> goalCriteriaList = new ArrayList<>();
        GoalCriteria goalCriteria = new GoalCriteria(
                createGoalRequest.getGoalCritTarget(),
                createGoalRequest.getGoalCritUnit(),
                createGoalRequest.getGoalCritTimeperiod(),
                LocalDate.parse(createGoalRequest.getGoalCritEffectiveDate()));
        goalCriteriaList.add(goalCriteria);
        return goalCriteriaList;
    }

}
