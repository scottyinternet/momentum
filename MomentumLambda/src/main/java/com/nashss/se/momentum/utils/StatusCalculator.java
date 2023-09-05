package com.nashss.se.momentum.utils;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventSummary;
import com.nashss.se.momentum.models.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class StatusCalculator {

    public static Status calculateStatus(Goal goal, List<Event> eventList) {

        int timePeriod = goal.getTimePeriod();
        int target = goal.getTarget();
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

        Double middleSum = 0.0;
        Double recentSum = eventSummaryList.get(0).getSummedMeasurement();

        for (int i = 1; i < goal.getTimePeriod(); i++) {
            double measurement = eventSummaryList.get(i).getSummedMeasurement();
            middleSum += measurement;
            if (i <= timePeriod/2) {
                recentSum += measurement;
            }
        }

        Double todaysTotals = middleSum + eventSummaryList.get(0).getSummedMeasurement();
        Double yesterdaysTotals = eventSummaryList.get(timePeriod+1).getSummedMeasurement();

        //calculate status
        // IN MOMENTUM
        StatusEnum statusEnum;
        if (todaysTotals >= target || yesterdaysTotals >= target) {
            statusEnum = StatusEnum.IN_MOMENTUM;
        } else if (todaysTotals <= 0) {
            statusEnum = StatusEnum.NO_MOMENTUM;
        } else if (recentSum <= 0) {
            statusEnum = StatusEnum.LOSING_MOMENTUM;
        } else {
            statusEnum = StatusEnum.GAINING_MOMENTUM;
        }

        //create status message



        return new Status(statusEnum, "a very helpful status message", eventSummaryList);

    }
}
