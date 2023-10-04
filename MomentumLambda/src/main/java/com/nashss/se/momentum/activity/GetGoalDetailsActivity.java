package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.GetGoalDetailsRequest;
import com.nashss.se.momentum.activity.results.GetGoalDetailsResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.EventDao;
import com.nashss.se.momentum.dynamodb.GoalDao;
import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.GoalModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetGoalDetailsActivity {

    private final GoalDao goalDao;
    private final EventDao eventDao;

    @Inject
    public GetGoalDetailsActivity(GoalDao goalDao, EventDao eventDao) {
        this.goalDao = goalDao;
        this.eventDao = eventDao;
    }

    public GetGoalDetailsResult handleRequest(final GetGoalDetailsRequest getGoalDetailsRequest) {
        String requestedUserId = getGoalDetailsRequest.getUserId();
        String requestedGoalName = getGoalDetailsRequest.getGoalName();
        Goal goal = goalDao.getGoal(requestedUserId, requestedGoalName);

        List<Event> eventList = eventDao.getEvents(goal.getGoalId());
        ModelConverter modelConverter = new ModelConverter();
        List<EventModel> eventModels = new ArrayList<>();

        for (Event event : eventList) {
            eventModels.add(modelConverter.toEventModel(event));
        }

        GoalModel goalModel = new GoalModel(goal, eventModels);
        return GetGoalDetailsResult.builder()
                .withGoalDetailModel(goalModel)
                .build();
    }
}

