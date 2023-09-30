package com.nashss.se.momentum.models;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.nashss.se.momentum.utils.StatusEnum;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class GoalModel {

    private final String goalName;
    private final String userId;
    private final String goalId;
    private final String units;

    // List of GoalCriteria, which can be updated. One item for each update. Includes an "effectiveDate"
    private final List<GoalCriteria> goalCriteriaList;
    private final List<EventModel> rawEvents;
    private final LocalDate effectiveDate;

    //  C A L C U L A T E D   A T T R I B U T E S
    private final GoalCriteria currentGoalCriterion;
    private final Map<LocalDate, Integer> eventSummaryMap = new TreeMap<>();
    private final Map<LocalDate, GoalCriteria> goalCriteriaMap = new TreeMap<>();
    private final Map<LocalDate, Boolean> momentumBoolMap = new TreeMap<>();
    private Status todaysStatus;
    private final StreakData streakData;


    public GoalModel(String goalName, String userId, String goalId, String units, List<GoalCriteria> goalCriteriaList, LocalDate effectiveDate, List<EventModel> rawEvents) {
        this.goalName = goalName;
        this.userId = userId;
        this.goalId = goalId;
        this.units = units;
        this.goalCriteriaList = goalCriteriaList;
        this.effectiveDate = effectiveDate;
        this.rawEvents = rawEvents;
        //  C A L C U L A T E D   A T T R I B U T E S
        this.currentGoalCriterion = goalCriteriaList.get(goalCriteriaList.size()-1);
        createEventSummaryMap();
        createGoalCriteriaMap();
        createMomentumBoolMap();
        calculateStatus();
        this.streakData = new StreakData(momentumBoolMap);
    }

    //  C A L C U L A T E D   A T T R I B U T E   M E T H O D S

    private void createEventSummaryMap() {
        LocalDate date = LocalDate.now();
        while(date.isAfter(effectiveDate.minusDays(1))) {
            int sum = 0;
            for (EventModel event : rawEvents) {
                if (date.equals(event.getDateOfEvent())) {
                    sum += event.getMeasurement();
                }
            }
            eventSummaryMap.put(date, sum);
            date = date.minusDays(1);
        }
    }

    private void createGoalCriteriaMap() {
        LocalDate date = effectiveDate;
        int currentIndex = 0;
        while(date.isBefore(LocalDate.now().plusDays(1))) {
            if (goalCriteriaList.get(currentIndex+1).getEffectiveDate().isEqual(date)) {
                currentIndex++;
            }
            goalCriteriaMap.put(date, goalCriteriaList.get(currentIndex));
            date = date.plusDays(1);
        }
    }

    private void createMomentumBoolMap() {
        LocalDate date = effectiveDate;
        while(date.isBefore(LocalDate.now().plusDays(1))) {
            int sum = iterateThroughMap(date, goalCriteriaMap.get(date).getTimeFrame());
            if (sum >= goalCriteriaMap.get(date).getTarget()) {
                momentumBoolMap.put(date, true);
            } else {
                momentumBoolMap.put(date, false);
            }
            date = date.plusDays(1);
        }
    }

    private void calculateStatus() {
        int timeFrame = currentGoalCriterion.getTimeFrame();
        LocalDate today = LocalDate.now();
        double todaysTotalSum = iterateThroughMap(LocalDate.now(), timeFrame);
        boolean yesterdayInMomentum = momentumBoolMap.get(today.minusDays(1));
        double todaysTotalSumMinusLast = todaysTotalSum - eventSummaryMap.get(today.minusDays(timeFrame-1));

        StatusEnum statusEnum = calculateStatusEnum(todaysTotalSum, yesterdayInMomentum, todaysTotalSumMinusLast);
        String message = createStatusMessage(currentGoalCriterion, todaysTotalSum, todaysTotalSumMinusLast, statusEnum);
        todaysStatus = new Status(statusEnum, message, todaysTotalSum);
    }

    private int iterateThroughMap(LocalDate endDate, int numberOfDays) {
        LocalDate date = endDate;
        int sum = 0;
        for (int i = 0; i < numberOfDays; i++) {
            if (!eventSummaryMap.containsKey(date)) {
                break;
            }
            sum += eventSummaryMap.get(date);
            date = date.minusDays(1);
        }
        return sum;
    }

    //  C A L C U L A T E   S T A T U S   E N U M
    private StatusEnum calculateStatusEnum(double todaysTotal, boolean yesterdayInMomentum, double todaysTotalMinusLast) {
        int target = currentGoalCriterion.getTarget();
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
        return statusEnum;
    }

    //  C R E A T E   M E S S A G E
    private String createStatusMessage(GoalCriteria goalCriteria, double todaysTotal, double todaysTotalMinusLast, StatusEnum statusEnum) {
        String message;
        int timePeriod = goalCriteria.getTimeFrame();
        int target = goalCriteria.getTarget();

        // diff is difference between target and Todays Total
        double diff = Math.abs(todaysTotal - target); // positive number represents surplus, negative is building
        String formatUnits = units;
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
                    message = String.format("You have hit your target of %d %s exactly. Great work!", target, formatUnits);
                } else {
                    message = String.format("You have a surplus of %s %s. Keep it up!", diffFormatted, formatUnits);
                }
                break;
            case HAD_MOMENTUM_YESTERDAY:
                message = String.format("Hit %s %s today to stay in momentum.", diffFormatted, formatUnits);
                break;
            case NO_MOMENTUM:
                if (rawEvents.size() == 0) {
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
        LocalDate date = LocalDate.now();
        while (eventSummaryMap.get(date).equals(0)) {
            daysStale++;
            date = date.minusDays(1);
        }
        return daysStale;
    }

    private int calculateDaysSinceMomentum() {
        int daysSinceMomentum = 0;
        LocalDate date = LocalDate.now();
        while (!momentumBoolMap.get(date)) {
            daysSinceMomentum++;
            date = date.minusDays(1);
        }
        return daysSinceMomentum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalModel goalModel = (GoalModel) o;
        return Objects.equals(goalId, goalModel.goalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalId);
    }

    //  G E T T E R S
    public String getGoalName() {
        return goalName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUnits() {
        return units;
    }

    public List<GoalCriteria> getGoalCriteriaList() {
        return goalCriteriaList;
    }

    public List<EventModel> getRawEvents() {
        return rawEvents;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public GoalCriteria getCurrentGoalCriterion() {
        return currentGoalCriterion;
    }

    public Map<LocalDate, Integer> getEventSummaryMap() {
        return eventSummaryMap;
    }

    public Map<LocalDate, GoalCriteria> getGoalCriteriaMap() {
        return goalCriteriaMap;
    }

    public Map<LocalDate, Boolean> getMomentumBoolMap() {
        return momentumBoolMap;
    }

    public Status getTodaysStatus() {
        return todaysStatus;
    }

    public StreakData getStreakData() {
        return streakData;
    }

}
