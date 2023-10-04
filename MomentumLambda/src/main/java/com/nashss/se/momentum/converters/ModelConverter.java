package com.nashss.se.momentum.converters;

import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.dynamodb.models.GoalCriteria;
import com.nashss.se.momentum.models.*;
import com.nashss.se.momentum.dynamodb.models.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts between Data and API models.
 */
public class ModelConverter {

    public GoalInfo toGoalInfo(Goal goal) {
        return new GoalInfo(
                goal.getGoalName(),
                goal.getUserId(),
                goal.getGoalId(),
                goal.getStartDate()
        );
    }
    public GoalModel toGoalModel(Goal goal) {
        return new GoalModel(goal, new ArrayList<>());
    }
    public GoalSummary toGoalSummary(GoalModel goalModel) {
        return new GoalSummary(goalModel);
    }
    public GoalSummary toGoalSummaryFromGoal(Goal goal) {
        return toGoalSummary(toGoalModel(goal));
    }

    public EventModel toEventModel(Event event) {
        EventModel eventModel = EventModel.builder()
                .withGoalId(event.getGoalId())
                .withEventId(event.getEventId())
                .withDateOfEvent(event.getDate())
                .withMeasurement(event.getMeasurement())
                .build();
        return eventModel;
    }
    public GoalCriteriaModel toGoalCriteriaModel(GoalCriteria goalCriteria) {
        return new GoalCriteriaModel(
                goalCriteria.getTarget(),
                goalCriteria.getUnits(),
                goalCriteria.getTimeFrame(),
                goalCriteria.getEffectiveDate()
        );
    }

    public List<GoalCriteriaModel> toGoalCriteriaModelList(List<GoalCriteria> goalCriteriaList) {
        List<GoalCriteriaModel> goalCriteriaModelList = new ArrayList<>();
        for (GoalCriteria goalCriteria : goalCriteriaList) {
            goalCriteriaModelList.add(toGoalCriteriaModel(goalCriteria));
        }
        return goalCriteriaModelList;
    }
}
