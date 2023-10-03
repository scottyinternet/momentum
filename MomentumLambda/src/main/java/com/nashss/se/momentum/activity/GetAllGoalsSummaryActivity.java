package com.nashss.se.momentum.activity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.nashss.se.momentum.activity.requests.DeleteEventRequest;
import com.nashss.se.momentum.activity.requests.GetAllGoalsSummaryRequest;
import com.nashss.se.momentum.activity.results.DeleteEventResult;
import com.nashss.se.momentum.activity.results.GetAllGoalsSummaryResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.GoalModel;
import com.nashss.se.momentum.models.GoalSummary;
import com.nashss.se.momentum.models.Status;
import com.nashss.se.momentum.utils.StatusCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Implementation of the GetAllGoalsSummaryActivity for Momentum's GetAllGoalsSummary API.
 * <p>
 * This API allows the customer to get a summary of the momentum of all goals.
 */
public class GetAllGoalsSummaryActivity {

    private final Logger log = LogManager.getLogger();
    private final GoalDao goalDao;
    private final EventDao eventDao;

    /**
     * Instantiates a new GetAllGoalsSummaryActivity object.
     *
     * @param goalDao GoalDao to access the goals table.
     * @param eventDao EventDao to access the events table.
     */
    @Inject
    public GetAllGoalsSummaryActivity(GoalDao goalDao, EventDao eventDao) {
        this.goalDao = goalDao;
        this.eventDao = eventDao;
    }

    /**
     * This method handles the incoming request by retrieivng the necessary information to construct GoalSummary objects
     * for all the user's goals
     * <p>
     * It then builds the result with the list of GoalSummary objects.
     * @param getAllGoalsSummaryRequest request object containing the goal ID,
     * @return GetAllGoalsSummaryResult result object
     */

    public GetAllGoalsSummaryResult handleRequest(final GetAllGoalsSummaryRequest getAllGoalsSummaryRequest) {
        log.info("Received GetAllGoalsSummaryRequest {}", getAllGoalsSummaryRequest);

        List<Goal> goalsList = goalDao.getGoals(getAllGoalsSummaryRequest.getUserId());

        List<GoalModel> goalModelList = new ArrayList<>();

        for(Goal goal: goalsList){
            List<Event> eventList = eventDao.getEventsBetweenDates(goal);
            ModelConverter modelConverter = new ModelConverter();
            List<EventModel> eventModels = new ArrayList<>();
            for (Event event : eventList) {
                eventModels.add(modelConverter.toEventModel(event));
            }
            GoalModel goalModel = new GoalModel(goal, eventModels);
            goalModelList.add(goalModel);
        }

        return GetAllGoalsSummaryResult.builder()
                .withGoalModelList(goalModelList)
                .build();
    }
}
