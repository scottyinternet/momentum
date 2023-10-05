package com.nashss.se.momentum.utils;

import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.dynamodb.models.GoalCriteria;
import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.GoalModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestDataProvider {
    // GOAL
    String goalName1 = "Cardio";
    String userId1 = "griffin.scott881@gmail.com";
    String goalId1 = userId1+goalName1;
    LocalDate todayMinus30 = LocalDate.now().minusDays(30);
    LocalDate todayMinus7 = LocalDate.now().minusDays(7);
    LocalDate todayMinus5 = LocalDate.now().minusDays(5);
    LocalDate todayMinus3 = LocalDate.now().minusDays(3);
    LocalDate todayMinus1 = LocalDate.now().minusDays(1);
    LocalDate todays = LocalDate.now();
    List<EventModel> rawEvents = new ArrayList<>();
    GoalCriteria goalCriteria1 = new GoalCriteria(150,"minutes", 7, todayMinus30);
    GoalCriteria goalCriteria2 = new GoalCriteria(30, "minutes", 7, todayMinus5);

    EventModel event1 = EventModel.builder()
            .withGoalId(goalId1)
            .withDateOfEvent(todayMinus7)
            .withEventId("0001")
            .withMeasurement(130.0)
            .build();
    EventModel event2 = EventModel.builder()
            .withGoalId(goalId1)
            .withDateOfEvent(todayMinus3)
            .withEventId("0002")
            .withMeasurement(110.0)
            .build();
    EventModel event3 = EventModel.builder()
            .withGoalId(goalId1)
            .withDateOfEvent(LocalDate.now().minusDays(7))
            .withEventId("0003")
            .withMeasurement(90.0)
            .build();
    EventModel event4 = EventModel.builder()
            .withGoalId(goalId1)
            .withDateOfEvent(LocalDate.now().minusDays(20))
            .withEventId("0004")
            .withMeasurement(200.0)
            .build();
    EventModel event5 = EventModel.builder()
            .withGoalId(goalId1)
            .withDateOfEvent(LocalDate.now().minusDays(15))
            .withEventId("0005")
            .withMeasurement(150.0)
            .build();

    public Goal provideGoalWithFullDataAnd2GC() {
        Goal goal1 = new Goal();
        goal1.setGoalId(goalId1);
        goal1.setGoalName(goalName1);
        List<GoalCriteria> goalCriteriaList = new ArrayList<>();
        goalCriteriaList.add(goalCriteria1);
        goalCriteriaList.add(goalCriteria2);
        goal1.setGoalCriteriaList(goalCriteriaList);
        goal1.setUserId(userId1);
        goal1.setStartDate(todayMinus30);
        return goal1;
    }

    public Goal provideGoalWithNoGC() {
        Goal goal1 = new Goal();
        goal1.setGoalId(goalId1);
        goal1.setGoalName(goalName1);
        goal1.setGoalCriteriaList(new ArrayList<>());
        goal1.setUserId(userId1);
        goal1.setStartDate(todayMinus30);
        return goal1;
    }

    public List<EventModel> provideEventList5Events() {
        List<EventModel> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);
        eventList.add(event4);
        eventList.add(event5);
        return eventList;
    }

    public List<EventModel> provideEventListEmtpy() {
        return new ArrayList<>();
    }

    public GoalModel provideGoalModel() {
        return new GoalModel(provideGoalWithFullDataAnd2GC(), provideEventList5Events());
    }

    public Event provideEvent() {
        Event event = new Event();
                event.setGoalId(goalId1);
                event.setDate(todayMinus7);
                event.setEventId("UUID0005");
                event.setMeasurement(90.0);
                return event;
    }

}
