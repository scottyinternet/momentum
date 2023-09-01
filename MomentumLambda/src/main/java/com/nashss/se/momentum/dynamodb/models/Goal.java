package com.nashss.se.momentum.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.momentum.converters.UnitOfMeasurementConverter;
import com.nashss.se.momentum.utils.UnitOfMeasurement;

@DynamoDBTable(tableName = "goals")
public class Goal {

    private String userId;
    private String goalName;
    private String goalId;
    private Integer timePeriod;
    private Integer target;
    private UnitOfMeasurement unit;

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

    @DynamoDBAttribute(attributeName = "timePeriod")
    public Integer getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(Integer timePeriod) {
        this.timePeriod = timePeriod;
    }

    @DynamoDBAttribute(attributeName = "target")
    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    @DynamoDBAttribute(attributeName = "unit")
    @DynamoDBTypeConverted(converter = UnitOfMeasurementConverter.class)
    public UnitOfMeasurement getUnit() {
        return unit;
    }

    public void setUnit(UnitOfMeasurement unit) {
        this.unit = unit;
    }
}
