package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalSummary;

import java.util.List;


public class GetAllGoalsSummaryResult {

    private final List<GoalSummary> goalSummaryList;

    private GetAllGoalsSummaryResult( List<GoalSummary> goalModelList){
        this.goalSummaryList = goalModelList;
    }

    public List<GoalSummary> getGoalSummaryList() {
        return goalSummaryList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<GoalSummary> goalModelList;

        public Builder withGoalSummaryList(List<GoalSummary> goalModelList) {
            this.goalModelList = goalModelList;
            return this;
        }

        public GetAllGoalsSummaryResult build() {
            return new GetAllGoalsSummaryResult(goalModelList);
        }
    }

}
