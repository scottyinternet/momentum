package com.nashss.se.momentum.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nashss.se.momentum.dynamodb.models.GoalCriteria;

import java.util.List;

public class GoalCriteriaListToStringConverter implements DynamoDBTypeConverter<String, List<GoalCriteria>> {

    private ObjectMapper objectMapper = new ObjectMapper();
    public GoalCriteriaListToStringConverter() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public String convert(List<GoalCriteria> goalCriteriaList) {
        try {
            return objectMapper.writeValueAsString(goalCriteriaList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GoalCriteria> unconvert(String goalCriteriaListJSON) {
        try {
            return objectMapper.readValue(goalCriteriaListJSON, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}