package com.nashss.se.momentum.utils;

import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventSummary;
import com.nashss.se.momentum.models.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusCalculatorTest {
    String USERNAME = "user1@fakemail.com";
    String GOAL_NAME = "Cardio";
    String G2_USERNAME = "user2@fakemail.com";
    String G2_GOAL_NAME = "Study";
    private Goal goal1;
    private Goal goal2;
    private Goal shortGoal;
    private List<Event> eventList;

    @BeforeEach
    void setUp() {
        goal1 = new Goal();
        goal1.setUserId(USERNAME);
        goal1.setGoalName(GOAL_NAME);
        goal1.setGoalId(USERNAME+GOAL_NAME);
        goal1.setTarget(150);
        goal1.setUnit("minutes");
        goal1.setTimePeriod(7);

        goal2 = new Goal();
        goal2.setUserId(G2_USERNAME);
        goal2.setGoalName(G2_GOAL_NAME);
        goal2.setGoalId(G2_USERNAME+G2_GOAL_NAME);
        goal2.setTarget(40);
        goal2.setUnit("hours");
        goal2.setTimePeriod(30);

        shortGoal = new Goal();
        shortGoal.setUserId(G2_USERNAME);
        shortGoal.setGoalName(GOAL_NAME);
        shortGoal.setGoalId(G2_USERNAME+GOAL_NAME);
        shortGoal.setTarget(3);
        shortGoal.setUnit("times");
        shortGoal.setTimePeriod(3);

        eventList = new ArrayList<>();
    }

    //  N O   M O M E N T U M :   G O A L   1
    @Test
    void calculateStatus_goal1NoEntries_returnNoMomentum() {
        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.NO_MOMENTUM, status.getStatusEnum());
        assertEquals(String.format("The best time to plant a tree was %d days ago. The second best time is today!", goal1.getTimePeriod()), status.getStatusMessage());
        assertEquals(0.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  N O   M O M E N T U M :   G O A L   2
    @Test
    void calculateStatus_goal2NoEntries_returnNoMomentum() {
        Status status = StatusCalculator.calculateStatus(goal2, eventList);

        assertEquals(StatusEnum.NO_MOMENTUM, status.getStatusEnum());
        assertEquals(String.format("The best time to plant a tree was %d days ago. The second best time is today!", goal2.getTimePeriod()), status.getStatusMessage());
        assertEquals(0.0, status.getSum());
        assertEquals(goal2.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  I N   M O M E N T U M :   G O A L   1
    @Test
    void calculateStatus_goal1InMomentumEntries_returnInMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(75.0);
        event1.setEventId("1234");

        Event event2 = new Event();
        event2.setGoalId(goal1.getGoalId());
        event2.setDate(LocalDate.now().minusDays(1));
        event2.setMeasurement(80.0);
        event2.setEventId("2234");

        eventList.add(event1);
        eventList.add(event2);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.IN_MOMENTUM, status.getStatusEnum());
        assertEquals("You have a surplus of 5 minutes. Keep it up!", status.getStatusMessage());
        assertEquals(155.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  I N   M O M E N T U M :   G O A L   2
    @Test
    void calculateStatus_goal2InMomentumEntries_returnInMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal2.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(10.0);
        event1.setEventId("2234");

        Event event2 = new Event();
        event2.setGoalId(goal2.getGoalId());
        event2.setDate(LocalDate.now().minusDays(10));
        event2.setMeasurement(20.0);
        event2.setEventId("3234");

        Event event3 = new Event();
        event3.setGoalId(goal2.getGoalId());
        event3.setDate(LocalDate.now().minusDays(25));
        event3.setMeasurement(30.0);
        event3.setEventId("1234");


        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);

        Status status = StatusCalculator.calculateStatus(goal2, eventList);

        assertEquals(StatusEnum.IN_MOMENTUM, status.getStatusEnum());
        assertEquals("You have a surplus of 20 hours. Keep it up!", status.getStatusMessage());
        assertEquals(60.0, status.getSum());
        assertEquals(goal2.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  I N   M O M E N T U M   H I T   T O D A Y :   G O A L   1
    @Test
    void calculateStatus_goal1MomentumHitToday_returnInMomomentumHitToday() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now().minusDays(1));
        event1.setMeasurement(100.0);
        event1.setEventId("1234");

        Event event2 = new Event();
        event2.setGoalId(goal1.getGoalId());
        event2.setDate(LocalDate.now().minusDays(goal1.getTimePeriod()));
        event2.setMeasurement(80.0);
        event2.setEventId("2234");

        eventList.add(event1);
        eventList.add(event2);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        printer(status, goal1);

        assertEquals(StatusEnum.IN_MOMENTUM_HIT_TODAY, status.getStatusEnum());
        assertEquals("Hit 50 minutes today to stay in momentum.", status.getStatusMessage());
        assertEquals(100.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  I N   M O M E N T U M   H I T   T O D A Y :   G O A L   2
    @Test
    void calculateStatus_goal2MomentumHitToday_returnInMomomentumHitToday() {
        Event event1 = new Event();
        event1.setGoalId(goal2.getGoalId());
        event1.setDate(LocalDate.now().minusDays(1));
        event1.setMeasurement(30.0);
        event1.setEventId("1234");

        Event event2 = new Event();
        event2.setGoalId(goal2.getGoalId());
        event2.setDate(LocalDate.now().minusDays(goal2.getTimePeriod()));
        event2.setMeasurement(15.0);
        event2.setEventId("2234");

        eventList.add(event1);
        eventList.add(event2);

        Status status = StatusCalculator.calculateStatus(goal2, eventList);

        assertEquals(StatusEnum.IN_MOMENTUM_HIT_TODAY, status.getStatusEnum());
        assertEquals("Hit 10 hours today to stay in momentum.", status.getStatusMessage());
        assertEquals(30.0, status.getSum());
        assertEquals(goal2.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  I N   M O M E N T U M   H I T   T O M O R R O W :   G O A L   1
    @Test
    void calculateStatus_goal1MomentumHitTomorrow_returnInMomentumHitTomorrow() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(100.0);
        event1.setEventId("1234");

        Event event2 = new Event();
        event2.setGoalId(goal1.getGoalId());
        event2.setDate(LocalDate.now().minusDays(goal1.getTimePeriod()-1));
        event2.setMeasurement(80.0);
        event2.setEventId("2234");

        eventList.add(event1);
        eventList.add(event2);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.IN_MOMENTUM_HIT_TOMORROW, status.getStatusEnum());
        assertEquals("Hit 50 minutes tomorrow to stay in momentum.", status.getStatusMessage());
        assertEquals(180.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  I N   M O M E N T U M   H I T   T O M O R R O W :   G O A L   2
    @Test
    void calculateStatus_goal2MomentumHitTomorrow_returnInMomentumHitTomrrow() {
        Event event1 = new Event();
        event1.setGoalId(goal2.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(38.0);
        event1.setEventId("1234");

        Event event2 = new Event();
        event2.setGoalId(goal2.getGoalId());
        event2.setDate(LocalDate.now().minusDays(goal2.getTimePeriod()-1));
        event2.setMeasurement(4.0);
        event2.setEventId("2234");

        eventList.add(event1);
        eventList.add(event2);

        Status status = StatusCalculator.calculateStatus(goal2, eventList);

        assertEquals(StatusEnum.IN_MOMENTUM_HIT_TOMORROW, status.getStatusEnum());
        assertEquals("Hit 2 hours tomorrow to stay in momentum.", status.getStatusMessage());
        assertEquals(42.0, status.getSum());
        assertEquals(goal2.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  G A I N I N G   M O M E N T U M :   G O A L   1
    @Test
    void calculateStatus_goal1GainingMomentum_returnGainingMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(75.0);
        event1.setEventId("1234");

        eventList.add(event1);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.GAINING_MOMENTUM, status.getStatusEnum());
        assertEquals("Add 75 more minutes to be in momentum.", status.getStatusMessage());
        assertEquals(75.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  G A I N I N G   M O M E N T U M :   G O A L   2
    @Test
    void calculateStatus_goal2GainingMomentum_returnGainingMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal2.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(10.0);
        event1.setEventId("2234");

        eventList.add(event1);

        Status status = StatusCalculator.calculateStatus(goal2, eventList);

        assertEquals(StatusEnum.GAINING_MOMENTUM, status.getStatusEnum());
        assertEquals("Add 30 more hours to be in momentum.", status.getStatusMessage());
        assertEquals(10.0, status.getSum());
        assertEquals(goal2.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  L O S I N G   M O M E N T U M :   G O A L   1
    @Test
    void calculateStatus_goal1LosingMomentum_returnLosingMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now().minusDays(goal1.getTimePeriod()-1));
        event1.setMeasurement(75.0);
        event1.setEventId("1234");

        eventList.add(event1);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.LOSING_MOMENTUM, status.getStatusEnum());
        assertEquals("You haven't had an entry in the last 3 days. Get back to it!", status.getStatusMessage());
        assertEquals(75.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  L O S I N G   M O M E N T U M :   G O A L   2
    @Test
    void calculateStatus_goal2LosingMomentum_returnLosingMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal2.getGoalId());
        event1.setDate(LocalDate.now().minusDays(goal2.getTimePeriod()-1));
        event1.setMeasurement(10.0);
        event1.setEventId("2234");

        eventList.add(event1);

        Status status = StatusCalculator.calculateStatus(goal2, eventList);

        assertEquals(StatusEnum.LOSING_MOMENTUM, status.getStatusEnum());
        assertEquals("You haven't had an entry in the last 15 days. Get back to it!", status.getStatusMessage());
        assertEquals(10.0, status.getSum());
        assertEquals(goal2.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  EDGE CASES
    //  D A T E S   N O T   I N   R A N G E
    @Test
    void calculateStatus_dateNotInRange_eventsNotFactoredIn() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now().minusDays(goal1.getTimePeriod()+2));
        event1.setMeasurement(10.0);
        event1.setEventId("2234");

        eventList.add(event1);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.NO_MOMENTUM, status.getStatusEnum());
        assertEquals(String.format("The best time to plant a tree was %d days ago. The second best time is today!", goal1.getTimePeriod()), status.getStatusMessage());
        assertEquals(0.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    //  IN MOMENTUM Equal to target
    @Test
    void calculateStatus_goal1InMomentumEqualToTarget_returnInMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(75.0);
        event1.setEventId("1234");

        Event event2 = new Event();
        event2.setGoalId(goal1.getGoalId());
        event2.setDate(LocalDate.now().minusDays(1));
        event2.setMeasurement(75.0);
        event2.setEventId("2234");

        eventList.add(event1);
        eventList.add(event2);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.IN_MOMENTUM, status.getStatusEnum());
        assertEquals("You have hit your target of 150 minutes exactly. Great work!", status.getStatusMessage());
        assertEquals(150.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    // IN MOMENTUM singular unit drops s
    @Test
    void calculateStatus_goal1InMomentumSingularUnit_returnInMomentumStatus() {
        Event event1 = new Event();
        event1.setGoalId(goal1.getGoalId());
        event1.setDate(LocalDate.now());
        event1.setMeasurement(75.0);
        event1.setEventId("1234");

        Event event2 = new Event();
        event2.setGoalId(goal1.getGoalId());
        event2.setDate(LocalDate.now().minusDays(1));
        event2.setMeasurement(76.0);
        event2.setEventId("2234");

        eventList.add(event1);
        eventList.add(event2);

        Status status = StatusCalculator.calculateStatus(goal1, eventList);

        assertEquals(StatusEnum.IN_MOMENTUM, status.getStatusEnum());
        assertEquals("You have a surplus of 1 minute. Keep it up!", status.getStatusMessage());
        assertEquals(151.0, status.getSum());
        assertEquals(goal1.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    // G A I N I N G   M O M E N T U M :   S H O R T   G O A L
    // verifies Losing Momentum is skipped for goals with time periods of 3 days or less
    @Test
    void calculateStatus_goal3_skipsLosingMomentum() {
        Event event1 = new Event();
        event1.setGoalId(shortGoal.getGoalId());
        event1.setDate(LocalDate.now().minusDays(2));
        event1.setMeasurement(1.0);
        event1.setEventId("1234");

        eventList.add(event1);

        Status status = StatusCalculator.calculateStatus(shortGoal, eventList);

        assertEquals(StatusEnum.GAINING_MOMENTUM, status.getStatusEnum());
        assertEquals("Add 2 more times to be in momentum.", status.getStatusMessage());
        assertEquals(1, status.getSum());
        assertEquals(shortGoal.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    @Test
    void calculateStatus_goal3_NoMomentumShortGoalMessage() {
        shortGoal.setTimePeriod(2);
        Status status = StatusCalculator.calculateStatus(shortGoal, eventList);

        assertEquals(StatusEnum.NO_MOMENTUM, status.getStatusEnum());
        assertEquals("The best time to plant a tree was yesterday. The second best time is today!", status.getStatusMessage());
        assertEquals(0, status.getSum());
        assertEquals(shortGoal.getTimePeriod()+1, status.getEventSummaryList().size());
    }

    private void printer(Status status, Goal goal) {
        System.out.println(" - - - - -  S T A T U S   P R I N T E R  - - - - -  ");
        System.out.println("Status: " + status.getStatusEnum());
        System.out.println("Goal Name: " + goal.getGoalName());
        System.out.println("Target: " + goal.getTarget() + " " + goal.getUnit() + " within a rolling " + goal.getTimePeriod() + " day period.");
        System.out.println("Your Total: " + status.getSum());
        System.out.println("Message: " + status.getStatusMessage());

        // print time period
        for (int i = 0; i < goal.getTimePeriod(); i++) {
            EventSummary eventSummary = status.getEventSummaryList().get(i);
            System.out.println(" | " + eventSummary.getDate() + " | " + eventSummary.getSummedMeasurement() + " | ");
        }
        EventSummary lastEventSummary = status.getEventSummaryList().get(goal.getTimePeriod());
        System.out.println("   - | " + lastEventSummary.getDate() + " | " + lastEventSummary.getSummedMeasurement() + " | dropping off board today");
        System.out.println("");
    }
}
