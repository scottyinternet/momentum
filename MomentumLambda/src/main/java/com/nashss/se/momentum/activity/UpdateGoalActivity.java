package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.UpdateGoalRequest;
import com.nashss.se.momentum.activity.results.UpdateGoalResult;
import com.nashss.se.momentum.converters.ModelConverter;
import com.nashss.se.momentum.dynamodb.GoalDao;

import com.nashss.se.momentum.dynamodb.models.Goal;

import com.nashss.se.momentum.dynamodb.models.GoalCriteria;
import com.nashss.se.momentum.models.GoalModel;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UpdateGoalActivity {
    private final GoalDao goalDao;
    private List<GoalCriteria> goalCriteriaList;
    private GoalCriteria newGoalCriteria;

    /**
     * Instantiates a new UpdatePlaylistActivity object.
     *
     * @param goalDao PlaylistDao to access the playlist table.
     */
    @Inject
    public UpdateGoalActivity(GoalDao goalDao) {
        this.goalDao = goalDao;
        this.goalCriteriaList = new ArrayList<>();
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
        goalCriteriaList  = goal.getGoalCriteriaList();

        newGoalCriteria = new GoalCriteria(updateGoalRequest.getTarget(), updateGoalRequest.getUnit(), updateGoalRequest.getTimePeriod(), updateGoalRequest.getEffectiveDate());
        addNewGoalCriteriaToList();

        goal.setGoalCriteriaList(goalCriteriaList);
        goal = goalDao.saveGoal(goal);

        return UpdateGoalResult.builder()
                .withGoalModel(new ModelConverter().toGoalModel(goal))
                .build();
    }

    /**
     * removes all `GoalCriteria` objects with an effetive date equal to or after the new effective date
     * adds new `GoalCriteria` to end of list
     *
     */
    private void addNewGoalCriteriaToList() {
        int indexToReplace = getIndexToReplace();
        if (indexToReplace >= 0) {
            goalCriteriaList.subList(indexToReplace, goalCriteriaList.size()).clear();
        }
        goalCriteriaList.add(newGoalCriteria);
    }

    private int getIndexToReplace() {
        LocalDate effectiveDate = newGoalCriteria.getEffectiveDate();
        int indexToReplace = -1;
        for (int i = 0; i < this.goalCriteriaList.size(); i++){
            if(goalCriteriaList.get(i).getEffectiveDate().isEqual(effectiveDate) ||
                    goalCriteriaList.get(i).getEffectiveDate().isAfter(effectiveDate)) {
                indexToReplace = i;
                break;
            }
        }
        return indexToReplace;
    }



}
