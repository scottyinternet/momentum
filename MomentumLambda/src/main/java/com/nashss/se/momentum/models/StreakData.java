package com.nashss.se.momentum.models;

import java.time.LocalDate;
import java.util.Map;

public class StreakData {
    private final Map<LocalDate, Boolean> momentumBoolMap;

    // C A L C U L A T E D   A T T R I B U T E S
    private int currentStreak;
    private int longestStreak;
    private int totalDaysInMomentum;
    private int totalDays;

    public StreakData(Map<LocalDate, Boolean> momentumBoolMap) {
        this.momentumBoolMap = momentumBoolMap;
        calculateCurrentStreak();
        calculateLongestStreak();
        countDaysInMomentum();
        totalDays = momentumBoolMap.size();
    }

    private void calculateCurrentStreak() {
        LocalDate date = LocalDate.now();
        currentStreak = 0;
        while(momentumBoolMap.get(date)) {
            currentStreak++;
            date = date.minusDays(1);
        }
    }

    private void calculateLongestStreak() {
        longestStreak = 0;
        int localStreak = 0;
        for (Map.Entry<LocalDate, Boolean> entry : momentumBoolMap.entrySet()) {
            if(entry.getValue()) {
                localStreak++;
                if (localStreak > longestStreak) {
                    longestStreak = localStreak;
                }
            } else {
                localStreak = 0;
            }
        }
    }

    private void countDaysInMomentum() {
        for (Map.Entry<LocalDate, Boolean> entry : momentumBoolMap.entrySet()) {
            if (entry.getValue()) {
                totalDaysInMomentum++;
            }
        }
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public int getTotalDaysInMomentum() {
        return totalDaysInMomentum;
    }

    public int getTotalDays() {
        return totalDays;
    }
}
