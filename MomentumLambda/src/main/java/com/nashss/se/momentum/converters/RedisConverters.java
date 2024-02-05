package com.nashss.se.momentum.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.momentum.models.GoalSummary;

import java.util.List;

public class RedisConverters {

	private ObjectMapper objectMapper;

	public RedisConverters() {
		this.objectMapper = new ObjectMapper();
	}

	public String convertGoalSummaryListToJson(List<GoalSummary> goalSummaryList) {
		try {
			return objectMapper.writeValueAsString(goalSummaryList);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public List<GoalSummary> unConvertGoalSummaryListToJson(String json) {
		try {
			return objectMapper.readValue(json, new TypeReference<List<GoalSummary>>() {});
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
