package com.nashss.se.momentum.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.momentum.converters.LocalDateToStringConverter;
import com.nashss.se.momentum.models.GoalCriteria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = "goals")
public class Goal {

    private String userId;
    private String goalName;
    private String goalId;
    private LocalDate startDate;
    private List<GoalCriteria> goalCriteriaList;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @DynamoDBRangeKey(attributeName = "goalName")
    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }


    @DynamoDBAttribute(attributeName = "goalId")

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    @DynamoDBAttribute(attributeName = "startDate")
    @DynamoDBTypeConverted(converter = LocalDateToStringConverter.class)
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    // NEED TO DEAL WITH LIST OF OBJECTS CONTAINING LOCAL DATES!!!!
    public List<GoalCriteria> getGoalCriteriaList() {
        return new ArrayList<>(goalCriteriaList);
    }
    public void setGoalCriteriaList(List<GoalCriteria> goalCriteriaList) {
        this.goalCriteriaList = goalCriteriaList;
    }

}
