package com.nashss.se.momentum.models;

import com.nashss.se.momentum.dynamodb.models.Goal;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GoalModel {

    private final String goalName;
    private final String userId;
    private final String goalId;
    private final List<GoalCriteria> goalCriteriaList;
    private List<EventModel> rawEvents;
    private final LocalDate startDate;

    //  C A L C U L A T E D   A T T R I B U T E S
    private final GoalCriteria currentGoalCriterion;
    private final TreeMap<LocalDate, Double> eventSummaryMap;
    private final TreeMap<LocalDate, CriteriaStatusContainer> criteriaStatusContainerMap;
    private Status todaysStatus;
    private final StreakData streakData;


    public GoalModel(Goal goal, List<EventModel> rawEvents) {
        this.goalName = goal.getGoalName();
        this.userId = goal.getUserId();
        this.goalId = goal.getGoalId();
        this.goalCriteriaList = goal.getGoalCriteriaList();
        this.startDate = goal.getStartDate();
        this.rawEvents = rawEvents;
        sortRawEvents();
        //  C A L C U L A T E D   A T T R I B U T E S
        this.currentGoalCriterion = goalCriteriaList.get(goalCriteriaList.size()-1);
        this.eventSummaryMap = new TreeMap<>(Collections.reverseOrder());
        this.criteriaStatusContainerMap = new TreeMap<>(Collections.reverseOrder());
        createEventSummaryMap();
        createCriteriaStatusContainerMap();
        todaysStatus = new Status(this, LocalDate.now());
        this.streakData = new StreakData(criteriaStatusContainerMap);
    }

    public GoalModel(Goal goal) {
        this.goalName = goal.getGoalName();
        this.userId = goal.getUserId();
        this.goalId = goal.getGoalId();
        this.goalCriteriaList = goal.getGoalCriteriaList();
        this.startDate = goal.getStartDate();
        this.currentGoalCriterion = goalCriteriaList.get(0);
        this.rawEvents = new ArrayList<>();
        this.eventSummaryMap = new TreeMap<>(Collections.reverseOrder());
        this.criteriaStatusContainerMap = new TreeMap<>(Collections.reverseOrder());
        this.streakData = new StreakData();
    }

    //  C A L C U L A T E D   A T T R I B U T E   M E T H O D S

    private void sortRawEvents() {
        rawEvents = rawEvents.stream()
                .sorted(Comparator.comparing(EventModel::getDateOfEvent))
                .collect(Collectors.toList());
    }

    private void createEventSummaryMap() {
        LocalDate date = LocalDate.now();
        while(date.isAfter(startDate.minusDays(1))) {
            double sum = 0;
            for (EventModel event : rawEvents) {
                if (date.equals(event.getDateOfEvent())) {
                    sum += event.getMeasurement();
                }
            }
            eventSummaryMap.put(date, sum);
            date = date.minusDays(1);
        }
    }

    private void createCriteriaStatusContainerMap() {
        LocalDate date = startDate;
        int currentIndex = 0;
        while(date.isBefore(LocalDate.now().plusDays(1))) {
            if (currentIndex + 1 < goalCriteriaList.size()
                    && goalCriteriaList.get(currentIndex+1).getEffectiveDate().isEqual(date)) {
                currentIndex++;
            }
            CriteriaStatusContainer container = makeNewCriteriaStatusContainer(date, currentIndex);
            criteriaStatusContainerMap.put(date, container);
            date = date.plusDays(1);
        }
    }

    private CriteriaStatusContainer makeNewCriteriaStatusContainer(LocalDate date, int currentIndex) {
        GoalCriteria goalCriteria = goalCriteriaList.get(currentIndex);
        double sum = sumNDays(date, goalCriteria.getTimeFrame());
        Boolean inMomentumBool = isInMomentum(goalCriteria, sum);
        return new CriteriaStatusContainer(
                goalCriteria,
                sum,
                inMomentumBool
        );
    }

    private Boolean isInMomentum(GoalCriteria goalCriteria, double sum) {
        return sum >= goalCriteria.getTarget();
    }

    private double sumNDays(LocalDate mostRecentDate, int numberOfDays) {
        LocalDate date = mostRecentDate;
        double sum = 0;
        for (int i = 0; i < numberOfDays; i++) {
            if (!eventSummaryMap.containsKey(date)) {
                break;
            }
            sum += eventSummaryMap.get(date);
            date = date.minusDays(1);
        }
        return sum;
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

    public List<GoalCriteria> getGoalCriteriaList() {
        return goalCriteriaList;
    }

    public List<EventModel> getRawEvents() {
        return rawEvents;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public GoalCriteria getCurrentGoalCriterion() {
        return currentGoalCriterion;
    }

    public Status getTodaysStatus() {
        return todaysStatus;
    }

    public StreakData getStreakData() {
        return streakData;
    }

    public String getGoalId() {
        return goalId;
    }

    public TreeMap<LocalDate, Double> getEventSummaryMap() {
        return eventSummaryMap;
    }

    public TreeMap<LocalDate, CriteriaStatusContainer> getCriteriaStatusContainerMap() {
        return criteriaStatusContainerMap;
    }
}
