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
    private double percentInMomentum;
    private String streakMessage;

    public StreakData(Map<LocalDate, Boolean> momentumBoolMap) {
        this.momentumBoolMap = momentumBoolMap;
        calculateCurrentStreak();
        calculateLongestStreak();
        countDaysInMomentum();
        totalDays = momentumBoolMap.size();
        percentInMomentum = (double) totalDaysInMomentum/totalDays;
        createStreakMessage();
    }

    private void createStreakMessage() {
        if (currentStreak > 1 ) {
            streakMessage = String.format("Current Streak: %d Days", currentStreak);
        } else if (currentStreak == 1) {
            streakMessage = "Streak Starts Tomorrow";
        } else if (currentStreak < -1) {
            streakMessage = String.format("Last Streak ended %d days ago.", currentStreak*-1);
        } else {
            streakMessage = "SAVE YOUR STREAK!";
        }
    }

    private void calculateCurrentStreak() {
        LocalDate date = LocalDate.now();
        currentStreak = 0;
        boolean currentStatus = momentumBoolMap.get(date);
        while(momentumBoolMap.get(date) == currentStatus) {
            currentStreak++;
            date = date.minusDays(1);
        }
        if (!currentStatus) {
            currentStreak = -currentStreak;
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
