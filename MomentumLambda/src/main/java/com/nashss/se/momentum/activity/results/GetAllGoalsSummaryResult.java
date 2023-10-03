package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalModel;
import com.nashss.se.momentum.models.GoalSummary;

import java.util.List;
import java.util.Objects;


public class GetAllGoalsSummaryResult {

    private final List<GoalModel> goalModelList;

    private GetAllGoalsSummaryResult( List<GoalModel> goalModelList){
        this.goalModelList = goalModelList;
    }

    public List<GoalModel> getGoalModelList() {
        return goalModelList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<GoalModel> goalModelList;

        public Builder withGoalModelList(List<GoalModel> goalModelList) {
            this.goalModelList = goalModelList;
            return this;
        }

        public GetAllGoalsSummaryResult build() {
            return new GetAllGoalsSummaryResult(goalModelList);
        }
    }

}
