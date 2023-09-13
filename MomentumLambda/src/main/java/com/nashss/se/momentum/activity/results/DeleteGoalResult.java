package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalModel;

public class DeleteGoalResult {

    private final GoalModel goal;

    private DeleteGoalResult(GoalModel goal){this.goal = goal;}

    public GoalModel getGoalModel(){return goal;}

    @Override
    public String toString(){
        return "DeleteGoalResult{" +
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

        public DeleteGoalResult build() {return new DeleteGoalResult(goal);}
    }
}
