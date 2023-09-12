package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalSummary;

import java.util.List;

public class GetAllGoalsSummaryResult {

    private final List<GoalSummary> goalSummaryList;

    private GetAllGoalsSummaryResult( List<GoalSummary> goalSummaryList){
        this.goalSummaryList = goalSummaryList;
    }

    public List<GoalSummary> getGoalSummary() {
        return goalSummaryList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<GoalSummary> goalSummaryList;

        public Builder withGoalSummaryList(List<GoalSummary> goalSummaryList) {
            this.goalSummaryList = goalSummaryList;
            return this;
        }

        public GetAllGoalsSummaryResult build() {
            return new GetAllGoalsSummaryResult(goalSummaryList);
        }
    }
}
