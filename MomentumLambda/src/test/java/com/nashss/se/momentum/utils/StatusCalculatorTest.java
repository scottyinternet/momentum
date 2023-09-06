package com.nashss.se.momentum.utils;

import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusCalculatorTest {
    Goal goal;
    List<Event> eventList;

    @BeforeEach
    void setUp() {
        goal = new Goal();
        goal.setUserId("user1@fakemail.com");
        goal.setGoalName("Cardio");
        goal.setGoalId();
    }

    @Test
    void calculateStatus() {
    }
}