package com.nashss.se.momentum.models;

import java.time.LocalDate;

import com.nashss.se.momentum.utils.TestDataProvider;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class GoalModelTest {
    TestDataProvider testDataProvider = new TestDataProvider();

    @Test
    void happyPath() {
        GoalModel goal = new GoalModel(testDataProvider.provideGoalWithFullDataAnd2GC(), testDataProvider.provideEventList5Events());
        printAllGoalInfo(goal);
    }

    @Test
    void mimickData() {
        EventModel event1 = EventModel.builder()
                .withGoalId("griffin.scott88@gmail.comCardio")
                .withDateOfEvent(LocalDate.now())
                .withEventId("0001")
                .withMeasurement(100.0)
                .build();
        EventModel event2 = EventModel.builder()
                .withGoalId("griffin.scott88@gmail.comCardio")
                .withDateOfEvent(LocalDate.now().minusDays(7))
                .withEventId("0002")
                .withMeasurement(150.0)
                .build();
        EventModel event3 = EventModel.builder()
                .withGoalId("griffin.scott88@gmail.comCardio")
                .withDateOfEvent(LocalDate.now().minusDays(6))
                .withEventId("0003")
                .withMeasurement(110.0)
                .build();
        List<EventModel> eventModelList = new ArrayList<>();
        eventModelList.add(event1);
        eventModelList.add(event2);
        eventModelList.add(event3);


        GoalModel goal = new GoalModel(testDataProvider.provideGoalWithFullDataAnd2GC(), eventModelList, LocalDate.now().toString());
        printAllGoalInfo(goal);
    }

    @Test
    void noEvents() {
        GoalModel goal = new GoalModel(testDataProvider.provideGoalWithFullDataAnd2GC(), new ArrayList<>());
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
        System.out.println("Target: " + goal.getStatus().getTarget());
        System.out.println("Sum: " + goal.getStatus().getSum());
        System.out.println(("Percent of Target: " + goal.getStatus().getTargetPercent()));
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
        System.out.println("\n - - - - - - - -  P R I N T   E V E N T   S U M M A R I E S  - - - - - - - - - - - - - - ");
        for (Map.Entry<LocalDate, Double> entry : goal.getEventSummaryMap().entrySet()) {
            System.out.println(String.format("Date: %s  |  Measurement: %s %s  |  Momentum: %s  |  Criteria: %s", entry.getKey(), entry.getValue(), goal.getCurrentGoalCriterion().getUnits(), goal.getCriteriaStatusContainerMap().get(entry.getKey()).getInMomentum(), goal.getCriteriaStatusContainerMap().get(entry.getKey()).getGoalCriteria().getGoalCriteriaMessage()));
        }
    }

    private void printRawEvents(GoalModel goal) {
        System.out.println("\n - - - - - - - -  P R I N T   E V E N T   E N T R I E S  - - - - - - - - - - - - - - ");
        for (EventModel event : goal.getEventEntries()) {
            System.out.println(String.format("Date: %s  |  Measurement: %s", event.getDateOfEvent(),event.getMeasurement()));
        }
    }
}