package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.GoalSummary;

import java.util.List;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetAllGoalsSummaryResult)) return false;
        GetAllGoalsSummaryResult that = (GetAllGoalsSummaryResult) o;
        return Objects.equals(this.goalSummaryList, that.goalSummaryList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalSummaryList);
    }

}
