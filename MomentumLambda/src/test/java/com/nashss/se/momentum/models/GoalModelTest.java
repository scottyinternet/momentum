package com.nashss.se.momentum.models;

import java.time.LocalDate;

import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.dynamodb.models.GoalCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class GoalModelTest {
    Goal goal1;
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

        goal1 = new Goal();
        goal1.setGoalId(goalId1);
        goal1.setGoalName(goalName1);
        goal1.setGoalCriteriaList(goalCriteriaList);
        goal1.setUserId(userId1);
        goal1.setStartDate(startDate1);
    }

    @Test
    void x() {
        GoalModel goal = new GoalModel(goal1, rawEvents);
        printAllGoalInfo(goal);
    }

    private void printAllGoalInfo(GoalModel goal) {
        printGoal(goal);
        printGoalCriteria(goal);
        printStatus(goal);
        printStreak(goal);
        printAllEventSummaries(goal);
        printRawEvents(goal);
    }

    void printGoal(GoalModel goal) {
        System.out.println("\n - - - - - - - -  P R I N T   G O A L   I N F O  - - - - - - - - - - - - - - ");
        System.out.printf("Goal Name: %s%n", goal.getGoalInfo().getGoalName());
        System.out.printf("User Name: %s%n", goal.getGoalInfo().getUserId());
        System.out.printf("GoalID: %s%n", goal.getGoalInfo().getGoalId());
        System.out.printf("Start Date: %s%n", goal.getGoalInfo().getStartDate());
    }

    private void printGoalCriteria(GoalModel goal) {
        System.out.println("\n - - - - - - - -  P R I N T   G O A L   C R I T E R I A   L I S T  - - - - - - - - - - - - - - ");
        List<GoalCriteriaModel> goalCriteriaList = goal.getGoalCriteriaList();
        for (int i = 0; i < goalCriteriaList.size(); i++) {
            System.out.printf("Goal Criteria %d: %s | Effective Date: %s%n", i+1, goalCriteriaList.get(i).getGoalCriteriaMessage(), goalCriteriaList.get(i).getEffectiveDate().toString());
        }
    }
    private void printStatus(GoalModel goal) {
        System.out.println("\n - - - - - - - -  P R I N T   S T A T U S  - - - - - - - - - - - - - - ");
        System.out.println("Status: " + goal.getStatus().getStatusEnum().toString());
        System.out.println("Sum: " + goal.getStatus().getSum());
        System.out.println(String.format("Status Message: %s", goal.getStatus().getStatusMessage()));
        int index = 1;
        for (Map.Entry<LocalDate, Double> entry: goal.getStatus().getStatusEventSummaries().entrySet()) {
            StringBuilder message = new StringBuilder(String.format("Date: %s  |  Measurement: %s %s", entry.getKey(), entry.getValue(), goal.getCurrentGoalCriterion().getUnits()));
            if (index == goal.getStatus().getStatusEventSummaries().size()) {
                message.insert(0, "       - ");
            }
            System.out.println(message);
            index++;
        }
    }
    private void printStreak(GoalModel goal) {
        System.out.println("\n - - - - - - - -  P R I N T   S T R E A K  - - - - - - - - - - - - - - ");
     StreakData streakData = goal.getStreakData();
        System.out.println("Current Streak: " + streakData.getCurrentStreak());
        System.out.println("Longest Streak: " + streakData.getLongestStreak());
        System.out.println(String.format("%s days in momentum out of %s days tracked", streakData.getTotalDaysInMomentum(), streakData.getTotalDays()));
        System.out.println("Percentage Of Time In Momentum: " + streakData.getPercentString());
    }
    private void printAllEventSummaries(GoalModel goal) {
        System.out.println("\n - - - - - - - -  P R I N T   E V E N T   S U M A R R I E S  - - - - - - - - - - - - - - ");
        for (Map.Entry<LocalDate, Double> entry : goal.getEventSummaryMap().entrySet()) {
            System.out.println(String.format("Date: %s  |  Measurement: %s %s  |  Momentum: %s  |  Criteria: %s", entry.getKey(), entry.getValue(), goal.getCurrentGoalCriterion().getUnits(), goal.getCriteriaStatusContainerMap().get(entry.getKey()).getInMomentumBool(), goal.getCriteriaStatusContainerMap().get(entry.getKey()).getGoalCriteria().getGoalCriteriaMessage()));
        }
    }

    private void printRawEvents(GoalModel goal) {
        System.out.println("\n - - - - - - - -  P R I N T   E V E N T   E N T R I E S  - - - - - - - - - - - - - - ");
        for (EventModel event : goal.getEventEntries()) {
            System.out.println(String.format("Date: %s  |  Measurement: %s", event.getDateOfEvent(),event.getMeasurement()));
        }
    }
}