package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalModel;

public class CreateGoalResult {

    private final GoalModel goal;

    private CreateGoalResult(GoalModel goal) {
        this.goal = goal;
    }

    public GoalModel getGoal() {
        return goal;
    }

    @Override
    public String toString() {
        return "CreateGoalResult{" +
                "goal=" + goal +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private GoalModel goal;

        public Builder withGoal(GoalModel goal) {
            this.goal = goal;
            return this;
        }

        public CreateGoalResult build() {
            return new CreateGoalResult(goal);
        }
    }
}
