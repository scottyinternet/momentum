package com.nashss.se.momentum.utils;

import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventSummary;
import com.nashss.se.momentum.models.Status;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatusCalculator {

    public static Status calculateStatus(Goal goal, List<Event> eventList) {

        int timePeriod = goal.getTimePeriod();

        List<EventSummary> eventSummaryList = createEventSummaryList(eventList, timePeriod);

        //  C A L C U L A T E   S U M S
        double middleSum = 0.0;
        double recentSum = eventSummaryList.get(0).getSummedMeasurement();

        for (int i = 1; i < timePeriod; i++) {
            double measurement = eventSummaryList.get(i).getSummedMeasurement();
            middleSum += measurement;
            if (i <= timePeriod/2) {
                recentSum += measurement;
            }
        }

        double todaysTotals = middleSum + eventSummaryList.get(0).getSummedMeasurement();
        double yesterdaysTotals = eventSummaryList.get(timePeriod).getSummedMeasurement() + middleSum;
        double todaysTotalMinusLast = todaysTotals - eventSummaryList.get(timePeriod-1).getSummedMeasurement();


        StatusEnum statusEnum = calculateStatusEnum(goal.getTarget(), todaysTotals, yesterdaysTotals, recentSum, todaysTotalMinusLast);
        String message = createStatusMessage(goal, todaysTotals, todaysTotalMinusLast, statusEnum);

        return new Status(statusEnum, message, eventSummaryList, todaysTotals);
    }




    //  - - - - -  H E L P E R S  - - - - -

    //  C R E A T E   E V E N T   S U M M A R Y   L I S T
    private static List<EventSummary> createEventSummaryList(List<Event> eventList, int timePeriod) {
        List<EventSummary> eventSummaryList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < timePeriod +1; i++) {
            LocalDate date = today.minusDays(i);
            Double sum = 0.0;
            for (Event event : eventList) {
                if (date.equals(event.getDate())) {
                    sum += event.getMeasurement();
                }
            }
            eventSummaryList.add(new EventSummary(date, sum));
        }
        return eventSummaryList;
    }


    //  C A L C U L A T E   S T A T U S   E N U M
    private static StatusEnum calculateStatusEnum(int target, double todaysTotals, double yesterdaysTotals, double recentSum, double todaysTotalMinusLast) {
        StatusEnum statusEnum;
        if (todaysTotals >= target && todaysTotalMinusLast < target) {
            statusEnum = StatusEnum.IN_MOMENTUM_HIT_TOMORROW;
        } else if (todaysTotals >= target){
            statusEnum = StatusEnum.IN_MOMENTUM;
        } else if (yesterdaysTotals >= target) {
            statusEnum = StatusEnum.IN_MOMENTUM_HIT_TODAY;
        } else if (todaysTotals <= 0) {
            statusEnum = StatusEnum.NO_MOMENTUM;
        } else if (recentSum <= 0 && target > 3) {
            statusEnum = StatusEnum.LOSING_MOMENTUM;
        } else {
            statusEnum = StatusEnum.GAINING_MOMENTUM;
        }
        return statusEnum;
    }


    //  C R E A T E   M E S S A G E
    private static String createStatusMessage(Goal goal, double todaysTotals, double todaysTotalMinusLast, StatusEnum statusEnum) {
        String message;
        int timeperiod = goal.getTimePeriod();
        int target = goal.getTarget();

        // diff is difference between target and Todays Total
        double diff = Math.abs(todaysTotals - target); // positive number represents surplus, negative is building

        String units = goal.getUnit();
        // make units singular if necessary
        if (0 < diff && diff <= 1 &&  units.endsWith("s")) {
            units = units.substring(0,units.length()-1);
        }

        // format diff - always positive and with 1 or 0 decimal places
        DecimalFormat df = new DecimalFormat("#.#"); // Format to a maximum of 3 decimal places
        df.setDecimalSeparatorAlwaysShown(false); // Hide decimal separator if there are no decimal digits
        String diffFormatted = df.format(diff);
        String hitTomorrowAmountFormatted = df.format(Math.abs(todaysTotalMinusLast - target));

        switch (statusEnum) {
            case IN_MOMENTUM_HIT_TOMORROW:
                message = String.format("Hit %s %s tomorrow to stay in momentum.", hitTomorrowAmountFormatted, units);
                break;
            case IN_MOMENTUM:
                if (diff == 0) {
                    message = String.format("You have hit your target of %d %s exactly. Great work!", target, units);
                } else {
                    message = String.format("You have a surplus of %s %s. Keep it up!", diffFormatted, units);
                }
                break;
            case IN_MOMENTUM_HIT_TODAY:
                message = String.format("Hit %s %s today to stay in momentum.", diffFormatted, units);
                break;
            case NO_MOMENTUM:
                if (timePeriod <= 2) {
                    message = "The best time to plant a tree was yesterday. The second best time is today!";
                } else {
                    message = String.format("The best time to plant a tree was %d days ago. The second best time is today!", timePeriod);
                }
                break;
            case LOSING_MOMENTUM:
                message = String.format("You haven't had an entry in the last %d days. Get back to it!", timePeriod /2);
                break;
            case GAINING_MOMENTUM:
                message = String.format("Add %s more %s to be in momentum.", diffFormatted, units);
                break;
            default:
                message = "";
        }
        return message;
    }
}
