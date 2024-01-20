package com.nashss.se.momentum.models;

import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.utils.StatusEnum;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;
import java.util.SortedMap;

public class Status {

    private StatusEnum statusEnum;
    private String statusString;
    private String statusMessage;
    private double sum;
    private double target;
    private int targetPercent;
    private Map<LocalDate, Double> statusEventSummaries;

    // for calculated values
    private final GoalModel goal;
    private final LocalDate date;
    SortedMap<LocalDate, CriteriaStatusContainer> criteriaStatusContainerMap;
    SortedMap<LocalDate, Double> eventSummaryMap;
    GoalCriteriaModel currentGoalCriterion;

    public Status(GoalModel goal) {
        this(goal, LocalDate.now());
    }

    public Status(GoalModel goal, LocalDate date) {
        this.goal = goal;
        this.date = date;
        this.target = goal.getCurrentGoalCriterion().getTarget();
        this.criteriaStatusContainerMap = goal.getCriteriaStatusContainerMap();
        this.eventSummaryMap = goal.getEventSummaryMap();
        this.currentGoalCriterion = goal.getCurrentGoalCriterion();

        calculateStatus();
        calculateTargetPercent();
    }

    //  S T A T U S   M E T H O D S
    private void calculateStatus() {
        CriteriaStatusContainer container = criteriaStatusContainerMap.get(date);
        System.out.println(" - - - A  - - - ");
        System.out.println(date);
        int timeFrame = container.getGoalCriteria().getTimeFrame();
        sum = container.getSumNMeasurements();
        boolean yesterdayInMomentumBool = criteriaStatusContainerMap.get(date.minusDays(1)).getInMomentum();
        double todaysTotalSumMinusLast = sum - eventSummaryMap.get(date.minusDays(timeFrame-1));

        statusEnum = calculateStatusEnum(sum, yesterdayInMomentumBool, todaysTotalSumMinusLast);
        statusMessage = createStatusMessage(currentGoalCriterion, sum, todaysTotalSumMinusLast, statusEnum);
        statusEventSummaries = createSubMap(date, currentGoalCriterion);
    }

    private Map<LocalDate, Double> createSubMap(LocalDate date, GoalCriteriaModel goalCriteria) {
        return eventSummaryMap.headMap(date.minusDays(goalCriteria.getTimeFrame()+1));
    }

    //  C A L C U L A T E   S T A T U S   E N U M
    private StatusEnum calculateStatusEnum(double todaysTotal, boolean yesterdayInMomentum, double todaysTotalMinusLast) {
        double target = currentGoalCriterion.getTarget();
        StatusEnum statusEnum;
        if (todaysTotal >= target && todaysTotalMinusLast < target) {
            statusEnum = StatusEnum.IN_MOMENTUM_HIT_TOMORROW;
        } else if (todaysTotal >= target){
            statusEnum = StatusEnum.IN_MOMENTUM;
        } else if (yesterdayInMomentum) {
            statusEnum = StatusEnum.HAD_MOMENTUM_YESTERDAY;
        } else if (todaysTotal <= 0) {
            statusEnum = StatusEnum.NO_MOMENTUM;
        } else if (calculateDaysStale() >= target/2 && target > 3) {
            statusEnum = StatusEnum.STALE_MOMENTUM;
        } else if (calculateDaysStale() >= calculateDaysSinceMomentum()) {
            statusEnum = StatusEnum.LOSING_MOMENTUM;
        } else {
            statusEnum = StatusEnum.GAINING_MOMENTUM;
        }
        statusString = statusEnum.toString();
        return statusEnum;
    }

    //  C R E A T E   M E S S A G E
    private String createStatusMessage(GoalCriteriaModel goalCriteria, double todaysTotal, double todaysTotalMinusLast, StatusEnum statusEnum) {
        String message;
        int timePeriod = goalCriteria.getTimeFrame();
        double target = goalCriteria.getTarget();

        // diff is difference between target and Today's Total
        double diff = Math.abs(todaysTotal - target); // positive number represents surplus, negative is building
        String formatUnits = currentGoalCriterion.getUnits();
        // make units singular if necessary
        if (0 < diff && diff <= 1 &&  formatUnits.endsWith("s")) {
            formatUnits = formatUnits.substring(0,formatUnits.length()-1);
        }

        // format diff - always positive and with 1 or 0 decimal places
        DecimalFormat df = new DecimalFormat("#.#"); // Format to a maximum of 3 decimal places
        df.setDecimalSeparatorAlwaysShown(false); // Hide decimal separator if there are no decimal digits
        String diffFormatted = df.format(diff);
        String hitTomorrowAmountFormatted = df.format(Math.abs(todaysTotalMinusLast - target));

        switch (statusEnum) {
            case IN_MOMENTUM_HIT_TOMORROW:
                message = String.format("Hit %s %s tomorrow to stay in momentum.", hitTomorrowAmountFormatted, formatUnits);
                break;
            case IN_MOMENTUM:
                if (diff == 0) {
                    message = String.format("You have hit your target of %f %s exactly. Great work!", target, formatUnits);
                } else {
                    message = String.format("You have a surplus of %s %s. Keep it up!", diffFormatted, formatUnits);
                }
                break;
            case HAD_MOMENTUM_YESTERDAY:
                message = String.format("Hit %s %s today to stay in momentum.", diffFormatted, formatUnits);
                break;
            case NO_MOMENTUM:
                if (goal.getEventEntries().size() == 0) {
                    message = "This goal has no entries, lets get started today!";
                } else if (timePeriod <= 2) {
                    message = "The best time to plant a tree was yesterday. The second best time is today!";
                } else {
                    message = String.format("The best time to plant a tree was %d days ago. The second best time is today!", timePeriod);
                }
                break;
            case STALE_MOMENTUM:
                message = String.format("You haven't had an entry in the last %d days. Get back to it!", calculateDaysStale());
                break;
            case LOSING_MOMENTUM:
                message = String.format("You had momentum %d days ago and lost it. Get back to it!", calculateDaysSinceMomentum());
                break;
            case GAINING_MOMENTUM:
                message = String.format("Add %s more %s to be in momentum.", diffFormatted, formatUnits);
                break;
            default:
                message = "";
        }
        return message;
    }

    private int calculateDaysStale() {
        int daysStale = 0;
        LocalDate dateIter = this.date;
        while (eventSummaryMap.get(dateIter) == 0) {
            daysStale++;
            dateIter = dateIter.minusDays(1);
        }
        return daysStale;
    }

    private int calculateDaysSinceMomentum() {
        int daysSinceMomentum = 0;
        LocalDate dateIter = this.date;
        while (criteriaStatusContainerMap.get(dateIter) != null &&
                !criteriaStatusContainerMap.get(dateIter).getInMomentum()) {
            daysSinceMomentum++;
            dateIter = dateIter.minusDays(1);
        }
        return daysSinceMomentum;
    }

    private void calculateTargetPercent() {
        targetPercent = (int) Math.round(sum/currentGoalCriterion.getTarget()*100);
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public double getSum() {
        return sum;
    }

    public Map<LocalDate, Double> getStatusEventSummaries() {
        return statusEventSummaries;
    }

    public double getTargetPercent() {
        return targetPercent;
    }

    public double getTarget() {
        return target;
    }

    public String getStatusString() {
        return statusString;
    }

    public String getDate() {
        return date.toString();
    }
}

