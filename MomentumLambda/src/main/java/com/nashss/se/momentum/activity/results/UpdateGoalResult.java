package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalModel;

public class UpdateGoalResult {

    private final GoalModel goalModel;

    private UpdateGoalResult(GoalModel goalModel) {

        this.goalModel= goalModel;
    }

    public GoalModel  getGoal() {
        return goalModel;
    }

    @Override
    public String toString() {
        return "UpdateGoalResult{" +
                "goalModel=" + goalModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static UpdateGoalResult.Builder builder() {
        return new UpdateGoalResult.Builder();
    }

    public static class Builder {
        private GoalModel  goalModel;

        public Builder withGoalModel(GoalModel goalModel) {
            this.goalModel = goalModel;
            return this;
        }

        public UpdateGoalResult build() {
            return new UpdateGoalResult(goalModel);
        }
    }


}
