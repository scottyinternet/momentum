package com.nashss.se.momentum.models;

import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.models.Goal;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GoalModel {

    private final GoalInfo goalInfo;
    private final List<GoalCriteriaModel> goalCriteriaList;
    private List<EventModel> eventEntries;

    //  C A L C U L A T E D   A T T R I B U T E S
    private final GoalCriteriaModel currentGoalCriterion;
    private final TreeMap<LocalDate, Double> eventSummaryMap;
    private final TreeMap<LocalDate, CriteriaStatusContainer> criteriaStatusContainerMap;
    private Status status;
    private final StreakData streakData;


    public GoalModel(Goal goal, List<EventModel> eventEntries) {
        goalInfo = new GoalInfo(
                goal.getGoalName(),
                goal.getUserId(),
                goal.getGoalId(),
                goal.getStartDate()
        );
        ModelConverter modelConverter = new ModelConverter();
        if (goal.getGoalCriteriaList().isEmpty()) {
            throw new RuntimeException("Goal Criteria List Empty");
        }
        this.goalCriteriaList = modelConverter.toGoalCriteriaModelList(goal.getGoalCriteriaList());
        this.eventEntries = eventEntries;
        sortEventEntries();
        //  C A L C U L A T E D   A T T R I B U T E S
        this.currentGoalCriterion = goalCriteriaList.get(goalCriteriaList.size()-1);
        this.eventSummaryMap = new TreeMap<>(Collections.reverseOrder());
        createEventSummaryMap();
        this.criteriaStatusContainerMap = new TreeMap<>(Collections.reverseOrder());
        makeCriteriaStatusContainerMap();
        status = new Status(this, LocalDate.now());
        this.streakData = calculateStreakData();
    }

    private StreakData calculateStreakData() {
        if (eventEntries.size() == 0) {
            return new StreakData();
        } else {
            return new StreakData(criteriaStatusContainerMap);
        }
    }
    //  C A L C U L A T E D   A T T R I B U T E   M E T H O D S
    private void sortEventEntries() {
        eventEntries = eventEntries.stream()
                .sorted(Comparator.comparing(EventModel::getDateOfEvent))
                .collect(Collectors.toList());
    }

    private void createEventSummaryMap() {
        LocalDate date = LocalDate.now();
        while(date.isAfter(goalInfo.getStartDate().minusDays(1))) {
            double sum = 0;
            for (EventModel event : eventEntries) {
                if (date.equals(event.getDateOfEvent())) {
                    sum += event.getMeasurement();
                }
            }
            eventSummaryMap.put(date, sum);
            date = date.minusDays(1);
        }
    }

    private void makeCriteriaStatusContainerMap() {
        LocalDate date = goalInfo.getStartDate();
        int currentIndex = 0;
        while(date.isBefore(LocalDate.now().plusDays(1))) {
            if (currentIndex + 1 < goalCriteriaList.size()
                    && goalCriteriaList.get(currentIndex+1).getEffectiveDate().isEqual(date)) {
                currentIndex++;
            }
            CriteriaStatusContainer container = makeContainer(date, currentIndex);
            criteriaStatusContainerMap.put(date, container);
            date = date.plusDays(1);
        }
    }

    private CriteriaStatusContainer makeContainer(LocalDate date, int currentIndex) {
        GoalCriteriaModel goalCriteria = goalCriteriaList.get(currentIndex);
        double sum = sumNDays(date, goalCriteria.getTimeFrame());
        Boolean inMomentumBool = isInMomentum(goalCriteria, sum);
        return new CriteriaStatusContainer(
                goalCriteria,
                sum,
                inMomentumBool
        );
    }

    private Boolean isInMomentum(GoalCriteriaModel goalCriteria, double sum) {
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
        return Objects.equals(goalInfo, goalModel.goalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalInfo);
    }

    //  G E T T E R S
    public GoalInfo getGoalInfo() {
        return this.goalInfo;
    }
    public List<GoalCriteriaModel> getGoalCriteriaList() {
        return goalCriteriaList;
    }

    public List<EventModel> getEventEntries() {
        return eventEntries;
    }

    public GoalCriteriaModel getCurrentGoalCriterion() {
        return currentGoalCriterion;
    }

    public Status getStatus() {
        return status;
    }

    public StreakData getStreakData() {
        return streakData;
    }

    public TreeMap<LocalDate, Double> getEventSummaryMap() {
        return eventSummaryMap;
    }

    public TreeMap<LocalDate, CriteriaStatusContainer> getCriteriaStatusContainerMap() {
        return criteriaStatusContainerMap;
    }
}
