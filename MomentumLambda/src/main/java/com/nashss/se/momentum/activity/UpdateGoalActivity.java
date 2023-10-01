package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.UpdateGoalRequest;
import com.nashss.se.momentum.activity.results.UpdateGoalResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.GoalDao;

import com.nashss.se.momentum.dynamodb.models.Goal;
import com.nashss.se.momentum.exceptions.InvalidAttributeValueException;
import com.nashss.se.momentum.metrics.MetricsConstants;

import com.nashss.se.momentum.models.GoalModel;
import com.nashss.se.projectresources.music.playlist.servic.util.MusicPlaylistServiceUtils;

import javax.inject.Inject;

public class UpdateGoalActivity {



    private final GoalDao goalDao;

    /**
     * Instantiates a new UpdatePlaylistActivity object.
     *
     * @param goalDao PlaylistDao to access the playlist table.
     */
    @Inject
    public UpdateGoalActivity(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    /**
     * This method handles the incoming request by retrieving the playlist, updating it,
     * and persisting the playlist.
     * <p>
     * It then returns the updated playlist.
     * <p>
     * If the playlist does not exist, this should throw a PlaylistNotFoundException.
     * <p>
     * If the provided playlist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     * <p>
     * If the request tries to update the customer ID,
     * this should throw an InvalidAttributeChangeException
     *
     * @param updateGoalRequest request object containing the playlist ID, playlist name, and customer ID
     *                              associated with it
     * @return updateGoalResult result object containing the API defined {@link GoalModel}
     */
    public UpdateGoalResult handleRequest(final UpdateGoalRequest updateGoalRequest) {




        Goal goal= goalDao.getGoal(updateGoalRequest.getUserId(),updateGoalRequest.getGoalName());



        goal.setTarget(updateGoalRequest.getTarget());
        goal.setTimePeriod(updateGoalRequest.getTimePeriod());
        goal = goalDao.saveGoal(goal);

      //  publishExceptionMetrics(false, false);
        return UpdateGoalResult.builder()
                .withGoalModel(new ModelConverter().toGoalModel(goal))
                .build();
    }



}
