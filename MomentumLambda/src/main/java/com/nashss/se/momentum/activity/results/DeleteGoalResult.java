package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalInfo;

public class DeleteGoalResult {

    private final GoalInfo goalInfo;

    private DeleteGoalResult(GoalInfo goalInfo) {
        this.goalInfo = goalInfo;
    }

    public GoalInfo getGoalInfo() {
        return goalInfo;
    }

    @Override
    public String toString(){
        return "DeleteGoalResult{" +
                "goalInfo=" + goalInfo +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private GoalInfo goalInfo;

        public Builder withGoalInfo(GoalInfo goalInfo) {
            this.goalInfo = goalInfo;
            return this;
        }

        public DeleteGoalResult build() {
            return new DeleteGoalResult(goalInfo);
        }
    }
}
