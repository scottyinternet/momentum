package com.nashss.se.momentum.models;

import com.nashss.se.momentum.utils.TestDataProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreakDataTest {
    TestDataProvider testDataProvider = new TestDataProvider();

    @Test
    void x() {
        GoalModel goalModel = testDataProvider.provideGoalModel();
        StreakData streakData = new StreakData(goalModel.getCriteriaStatusContainerMap());
        System.out.println(streakData.getCurrentStreak());
    }
}