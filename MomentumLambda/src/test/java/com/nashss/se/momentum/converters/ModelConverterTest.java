package com.nashss.se.momentum.converters;


import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.GoalModel;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.models.EventModel;

import com.google.common.collect.Sets;
import com.nashss.se.momentum.utils.UnitOfMeasurement;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static com.nashss.se.momentum.utils.CollectionUtils.copyToSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelConverterTest {
    private ModelConverter modelConverter = new ModelConverter();
/*
    @Test
    void toGoalModel_validInput_convertsToModel() {
        Goal goal = new Goal();
        goal.setGoalName("name");
        goal.setUserId("userId");
        goal.setGoalId(goal.getUserId() + goal.getGoalName());
        goal.setTimePeriod(0);
        goal.setTarget(0);
        goal.setUnit("Hour");

        GoalModel goalModel = modelConverter.toGoalModel(goal);
        assertEquals(goal.getGoalName(), goalModel.getGoalName());
        assertEquals(goal.getUserId(), goalModel.getUserId());
        assertEquals(goal.getGoalId(), goalModel.getGoalId());
        assertEquals(goal.getTimePeriod(), goalModel.getTimePeriod());
        assertEquals(goal.getTarget(), goalModel.getTarget());
        assertEquals(goal.getUnit(), goalModel.getUnit());
    }
    */

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
