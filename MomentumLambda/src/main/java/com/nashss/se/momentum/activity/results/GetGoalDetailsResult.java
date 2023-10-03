package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.GoalDetailsModel;
import com.nashss.se.momentum.models.GoalModel;
import com.nashss.se.momentum.models.Status;

import java.util.List;

public class GetGoalDetailsResult {

    private final GoalModel goalModel;

    private GetGoalDetailsResult(GoalModel goalModel){
        this.goalModel = goalModel;
    }

    public GoalModel getGoalModel() {
        return goalModel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private GoalModel goalModel;

        public Builder withGoalDetailModel(GoalModel goalModel) {
            this.goalModel = goalModel;
            return this;
        }

        public GetGoalDetailsResult build() {
            return new GetGoalDetailsResult(goalModel);
        }
    }
}
