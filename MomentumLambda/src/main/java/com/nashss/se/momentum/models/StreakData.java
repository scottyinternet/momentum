package com.nashss.se.momentum.models;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class StreakData {
    private final Map<LocalDate, CriteriaStatusContainer> criteriaStatusContainerMap;

    // C A L C U L A T E D   A T T R I B U T E S
    private int currentStreak;
    private int longestStreak;
    private int totalDaysInMomentum;
    private int totalDays;
    private double percentInMomentum;
    private String streakMessage;

    public StreakData(Map<LocalDate, CriteriaStatusContainer> criteriaStatusContainerMap) {
        this.criteriaStatusContainerMap = criteriaStatusContainerMap;
        calculateCurrentStreak();
        calculateLongestStreak();
        countDaysInMomentum();
        totalDays = criteriaStatusContainerMap.size();
        percentInMomentum = (double) totalDaysInMomentum/totalDays;
        createStreakMessage();
    }

    public StreakData() {
        this.criteriaStatusContainerMap = new TreeMap<>();
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.totalDaysInMomentum = 0;
        this.totalDays = 0;
        this.percentInMomentum = 0;
        this.streakMessage = "";
    }

    /**
     * calculates current streak
     * positive if currenet streak is In Momentum
     * negative if currently not In Momentum - represents days since In Momentum
     */
    private void calculateCurrentStreak() {
        LocalDate date = LocalDate.now();
        currentStreak = 0;
        final boolean currentStatus = criteriaStatusContainerMap.get(date).getInMomentumBool();
        while(criteriaStatusContainerMap.get(date) != null
                && criteriaStatusContainerMap.get(date).inMomentumBool == currentStatus) {
            currentStreak++;
            date = date.minusDays(1);
        }
        if (!currentStatus) {
            currentStreak = -currentStreak;
        }
    }

    /**
     * calculates longest streak since the Goals Inception
     */
    private void calculateLongestStreak() {
        longestStreak = 0;
        int localStreak = 0;
        for (Map.Entry<LocalDate, CriteriaStatusContainer> entry : criteriaStatusContainerMap.entrySet()) {
            if(entry.getValue().getInMomentumBool()) {
                localStreak++;
                if (localStreak > longestStreak) {
                    longestStreak = localStreak;
                }
            } else {
                localStreak = 0;
            }
        }
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
    private void countDaysInMomentum() {
        for (Map.Entry<LocalDate, CriteriaStatusContainer> entry : criteriaStatusContainerMap.entrySet()) {
            if (entry.getValue().getInMomentumBool()) {
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

    public double getPercentInMomentum() {
        return percentInMomentum;
    }

    public String getStreakMessage() {
        return streakMessage;
    }

    public String getPercentString() {
        return (int) (percentInMomentum * 100) + "%";
    }
}
