package com.nashss.se.momentum.converters;


import com.nashss.se.momentum.utils.TestDataProvider;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.GoalCriteria;
import com.nashss.se.momentum.models.EventModel;

import com.nashss.se.momentum.models.GoalModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelConverterTest {
    TestDataProvider testDataProvider = new TestDataProvider();
    private ModelConverter modelConverter = new ModelConverter();
    @Test
    void toGoalModel_validInput_convertsToModel() {
        Goal goal = testDataProvider.provideGoalWithFullDataAnd2GC();

        GoalModel goalModel = modelConverter.toGoalModel(goal);

        assertEquals(goal.getGoalName(), goalModel.getGoalInfo().getGoalName());
        assertEquals(goal.getUserId(), goalModel.getGoalInfo().getUserId());
        assertEquals(goal.getGoalId(), goalModel.getGoalInfo().getGoalId());
        GoalCriteria currentGoalCriteria = goal.getGoalCriteriaList().get(goal.getGoalCriteriaList().size()-1);
        assertEquals(currentGoalCriteria.getTimeFrame(), goal.getGoalCriteriaList().get(goal.getGoalCriteriaList().size()-1).getTimeFrame(), goalModel.getCurrentGoalCriterion().getTimeFrame());
        assertEquals(currentGoalCriteria.getTarget(), goalModel.getCurrentGoalCriterion().getTarget());
        assertEquals(currentGoalCriteria.getUnits(), goalModel.getCurrentGoalCriterion().getUnits());
    }

    @Test
    void toEventModel_withEvent_convertsToEventModel() {
        Event event = new Event();
        event.setEventId("1234");
        event.setGoalId("G1234");
        event.setDate(LocalDate.parse("2023-01-01"));
        event.setMeasurement(12.0);
        EventModel eventModel = modelConverter.toEventModel(event);

        assertEquals(event.getEventId(), eventModel.getEventId());
        assertEquals(event.getGoalId(), eventModel.getGoalId());
        assertEquals(event.getDate(), eventModel.getDateOfEvent());
        assertEquals(event.getMeasurement(), eventModel.getMeasurement());
    }
}
