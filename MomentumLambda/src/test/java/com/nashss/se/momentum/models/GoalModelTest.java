package com.nashss.se.momentum.models;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoalModelTest {
    String goalName1 = "Cardio";
    String userId1 = "user1@fakemail.com";
    String goalId1 = userId1+goalName1;
    List<GoalCriteria> goalCriteriaList = new ArrayList<>();
    LocalDate startDate1 = LocalDate.of(2023,9,1);
    List<EventModel> rawEvents = new ArrayList<>();
    GoalCriteria goalCriteria1;
    GoalCriteria goalCriteria2;
    EventModel event1;
    EventModel event2;
    EventModel event3;
    EventModel event4;
    EventModel event5;


    @BeforeEach
    void setUp() {
        goalCriteria1 = new GoalCriteria(150,"minutes", 7, startDate1);
        goalCriteria2 = new GoalCriteria(200, "minutes", 7, LocalDate.now().minusDays(5));
        goalCriteriaList.add(goalCriteria1);
        goalCriteriaList.add(goalCriteria2);

        event1 = EventModel.builder()
                .withGoalId(goalId1)
                .withDateOfEvent(LocalDate.now().minusDays(2))
                .withEventId("0001")
                .withMeasurement(130.0)
                .build();
        event2 = EventModel.builder()
                .withGoalId(goalId1)
                .withDateOfEvent(LocalDate.now().minusDays(5))
                .withEventId("0002")
                .withMeasurement(110.0)
                .build();
        event3 = EventModel.builder()
                .withGoalId(goalId1)
                .withDateOfEvent(LocalDate.now().minusDays(7))
                .withEventId("0003")
                .withMeasurement(90.0)
                .build();
        event4 = EventModel.builder()
                .withGoalId(goalId1)
                .withDateOfEvent(LocalDate.now().minusDays(20))
                .withEventId("0004")
                .withMeasurement(200.0)
                .build();
        event5 = EventModel.builder()
                .withGoalId(goalId1)
                .withDateOfEvent(LocalDate.now().minusDays(15))
                .withEventId("0005")
                .withMeasurement(150.0)
                .build();

        rawEvents.add(event1);
        rawEvents.add(event2);
        rawEvents.add(event3);
        rawEvents.add(event4);
        rawEvents.add(event5);



    }

    @Test
    void x() {
        GoalModel goal = new GoalModel(goalName1, userId1, goalId1, "minutes", goalCriteriaList, startDate1, rawEvents);
        printGoal(goal);
        printStatus(goal);
        printStreak(goal);
        //printGoalCriteriaFull(goal);

    }

    private void printGoalCriteriaFull(GoalModel goal) {
        for (GoalCriteria gc : goal.getGoalCriteriaList()) {
            System.out.println(gc.getGoalCriteriaMessage() + gc.getEffectiveDate());
        }
        for (Map.Entry<LocalDate, GoalCriteria> entry : goal.getGoalCriteriaMap().entrySet()) {
            System.out.println(String.format("%s | %s", entry.getKey(), entry.getValue().getGoalCriteriaMessage()));
        }
    }

    void printGoal(GoalModel goal) {
        System.out.println("\n - - - - - - - - - - - - - - - - - - - - - - ");
        System.out.printf("Goal Name: %s%n", goal.getGoalName());
        System.out.printf("User Name: %s%n", goal.getUserId());
        System.out.printf("GoalID: %s%n", goal.getGoalId());
        System.out.printf("Start Date: %s%n", goal.getStartDate());
        printGoalCriteria(goal.getGoalCriteriaList());
    }

    private void printGoalCriteria(List<GoalCriteria> goalCriteriaList) {
        for (int i = 0; i < goalCriteriaList.size(); i++) {
            System.out.printf("Goal Criteria %d: %s | Effective Date: %s%n", i+1, goalCriteriaList.get(i).getGoalCriteriaMessage(), goalCriteriaList.get(i).getEffectiveDate().toString());
        }
    }
    private void printStatus(GoalModel goal) {
        System.out.println("\n - - - - - - - - - - - - - - - - - - - - - - ");
        System.out.println(String.format("Status: %s", goal.getTodaysStatus().getStatusMessage()));
        for (Map.Entry<LocalDate, Integer> entry : goal.getEventSummaryMap().entrySet()) {
            System.out.println(String.format("Date: %s  |  Measurement: %s %s  |  Momentum: %s  |  Criteria: %s", entry.getKey(), entry.getValue(), goal.getUnits(), goal.getMomentumBoolMap().get(entry.getKey()), goal.getGoalCriteriaMap().get(entry.getKey()).getGoalCriteriaMessage()));
        }
        System.out.println("\n - - - - - - - - - - - - - - - - - - - - - - ");
    }
    private void printStreak(GoalModel goal) {
     StreakData streakData = goal.getStreakData();
        System.out.println("Current Streak: " + streakData.getCurrentStreak());
        System.out.println("Longest Streak: " + streakData.getLongestStreak());
        System.out.println(String.format("%s days in momentum out of %s days tracked", streakData.getTotalDaysInMomentum(), streakData.getTotalDays()));
        System.out.println("Percentage Of Time In Momentum: " + streakData.getPercentInMomentum());
    }
}