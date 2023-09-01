package com.nashss.se.momentum.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.momentum.converters.LocalDateToStringConverter;

import java.time.LocalDate;

@DynamoDBTable(tableName = "events")
public class Event {

    public static final String GSI_TABLE_NAME = "EventDateIndex";
    private String goalId;
    private String eventId;
    private LocalDate dateOfEvent;
    private Double measurement;

    @DynamoDBHashKey(attributeName = "goalId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = GSI_TABLE_NAME)
    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    @DynamoDBRangeKey(attributeName = "eventId")
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = GSI_TABLE_NAME, attributeName = "dateOfEvent")
    @DynamoDBTypeConverted(converter = LocalDateToStringConverter.class)
    public LocalDate getDate() {
        return dateOfEvent;
    }

    public void setDate(LocalDate date) {
        this.dateOfEvent = date;
    }

    @DynamoDBAttribute(attributeName = "measurement")
    public Double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Double measurement) {
        this.measurement = measurement;
    }
}
