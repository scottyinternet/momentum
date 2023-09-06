package com.nashss.se.momentum.utils;

import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventSummary;
import com.nashss.se.momentum.models.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatusCalculator {

    public static Status calculateStatus(Goal goal, List<Event> eventList) {

        int timePeriod = goal.getTimePeriod();
        int target = goal.getTarget();

        // C R E A T E   S U M M A R Y   L I S T
        List<EventSummary> eventSummaryList = new ArrayList<>();

        for (int i = 0; i < goal.getTimePeriod()+1; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            Double sum = 0.0;
            for (Event event : eventList) {
                if (date.equals(event.getDate())) {
                    sum += event.getMeasurement();
                }
            }
            eventSummaryList.add(new EventSummary(date, sum));
        }

        //  C A L C U L A T E   S U M S
        double middleSum = 0.0;
        double recentSum = eventSummaryList.get(0).getSummedMeasurement();

        for (int i = 1; i < goal.getTimePeriod(); i++) {
            double measurement = eventSummaryList.get(i).getSummedMeasurement();
            middleSum += measurement;
            if (i <= timePeriod/2) {
                recentSum += measurement;
            }
        }

        double todaysTotals = middleSum + eventSummaryList.get(0).getSummedMeasurement();
        double yesterdaysTotals = eventSummaryList.get(timePeriod+1).getSummedMeasurement();
        EventSummary lastDaySummary = eventSummaryList.get(timePeriod-1);
        double todaysTotalMinusLast = todaysTotals - lastDaySummary.getSummedMeasurement();

        //  C A L C U L A T E   S T A T U S
        StatusEnum statusEnum;
        if (todaysTotals >= target && todaysTotalMinusLast < target) {
            statusEnum = StatusEnum.IN_MOMENTUM_HIT_TOMORROW;
        } else if (todaysTotals >= target){
            statusEnum = StatusEnum.IN_MOMENTUM;
        } else if (yesterdaysTotals >= target) {
            statusEnum = StatusEnum.IN_MOMENTUM_HIT_TODAY;
        } else if (todaysTotals <= 0) {
            statusEnum = StatusEnum.NO_MOMENTUM;
        } else if (recentSum <= 0) {
            statusEnum = StatusEnum.LOSING_MOMENTUM;
        } else {
            statusEnum = StatusEnum.GAINING_MOMENTUM;
        }


        //  C R E A T E   M E S S A G E
        double diff = todaysTotals - target; // positive number represents surplus, negative is building
        String message;
        String units = goal.getUnit();
        switch (statusEnum) {
            case IN_MOMENTUM_HIT_TOMORROW:
                message = String.format("Hit %f %s tomorrow to stay in momentum.", todaysTotalMinusLast-target, units);
                break;
            case IN_MOMENTUM:
                message = String.format("You have a surplus of %f %s. Keep it up!", diff, units);
                break;
            case IN_MOMENTUM_HIT_TODAY:
                message = String.format("Hit %f %s today to stay in momentum.", diff, units);
                break;
            case NO_MOMENTUM:
                message = String.format("The best time to plant a tree was %d days ago. The second best time is today!", target);
                break;
            case LOSING_MOMENTUM:
                message = String.format("You haven't had an entry in the last %d days. Get back to it!", target/2);
                break;
            case GAINING_MOMENTUM:
                message = String.format("Add %f more %s to be in momentum.", diff*1, units);
                break;
            default:
                message = "";
        }

        return new Status(statusEnum, message, eventSummaryList, todaysTotals);
    }
}
