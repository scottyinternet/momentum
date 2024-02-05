package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.GetAllGoalsSummaryRequest;
import com.nashss.se.momentum.activity.results.GetAllGoalsSummaryResult;
import com.nashss.se.momentum.cache.CacheClient;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.converters.RedisConverters;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.GoalModel;
import com.nashss.se.momentum.models.GoalSummary;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the GetAllGoalsSummaryActivity for Momentum's GetAllGoalsSummary API.
 * <p>
 * This API allows the customer to get a summary of the momentum of all goals.
 */
public class GetAllGoalsSummaryActivity {

    private final GoalDao goalDao;
    private final EventDao eventDao;
    //private final CacheClient cache;
    private final RedisConverters redisConverters;

    /**
     * Instantiates a new GetAllGoalsSummaryActivity object.
     *
     * @param goalDao GoalDao to access the goals table.
     * @param eventDao EventDao to access the events table.
     */
    @Inject
    public GetAllGoalsSummaryActivity(GoalDao goalDao, EventDao eventDao) { //, CacheClient cache
        this.goalDao = goalDao;
        this.eventDao = eventDao;
//        this.cache = cache;
        this.redisConverters = new RedisConverters();
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

        // check cache
//        String allGoalsJsonString = cache.getValue("AllGoals::"+getAllGoalsSummaryRequest.getUserId());
//
//        // if found in cache, return
//        if (allGoalsJsonString != null) {
//
//            System.out.println(" - - cache path - - ");
//
//            return GetAllGoalsSummaryResult.builder()
//                    .withGoalSummaryList(redisConverters.unConvertGoalSummaryListToJson(allGoalsJsonString))
//                    .build();
//        }

        System.out.println(" x x NOT cache path x x ");

        List<Goal> goalsList = goalDao.getGoals(getAllGoalsSummaryRequest.getUserId());

        List<GoalSummary> goalSummaries = new ArrayList<>();

        for(Goal goal: goalsList){
            List<Event> eventList = eventDao.getEvents(goal.getGoalId());
            ModelConverter modelConverter = new ModelConverter();
            List<EventModel> eventModels = new ArrayList<>();
            for (Event event : eventList) {
                eventModels.add(modelConverter.toEventModel(event));
            }
            GoalModel goalModel = new GoalModel(goal, eventModels, getAllGoalsSummaryRequest.getDate());

            goalSummaries.add(modelConverter.toGoalSummary(goalModel));
        }

//        // add to cache
//        cache.setValueWithDefaultExpiration("AllGoals::"+getAllGoalsSummaryRequest.getUserId(),
//                redisConverters.convertGoalSummaryListToJson(goalSummaries));

        return GetAllGoalsSummaryResult.builder()
                .withGoalSummaryList(goalSummaries)
                .build();
    }
}
