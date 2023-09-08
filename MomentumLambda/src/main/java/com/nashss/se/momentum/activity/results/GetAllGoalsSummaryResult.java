package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.GoalDetailsModel;
import com.nashss.se.momentum.models.Status;

import java.util.List;

public class GetAllGoalsSummaryResult {

    private final GoalDetailsModel goalDetailsModel;

    private GetAllGoalsSummaryResult(GoalDetailsModel goalDetailsModel){
        this.goalDetailsModel = goalDetailsModel;
    }

    public GoalDetailsModel getGoalDetailsModel() {
        return goalDetailsModel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private GoalDetailsModel goalDetailsModel;

        public Builder withGoalDetailModel(GoalDetailsModel goalDetailsModel) {
            this.goalDetailsModel = goalDetailsModel;
            return this;
        }

        public GetAllGoalsSummaryResult build() {
            return new GetAllGoalsSummaryResult(goalDetailsModel);
        }
    }
}
