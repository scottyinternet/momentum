package com.nashss.se.momentum.activity.requests;

public class GetAllGoalsSummaryRequest {

    private String userId;

    private GetAllGoalsSummaryRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }


    @Override
    public String  toString() {
        return "GetAllGoalsSummaryRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetAllGoalsSummaryRequest build() {
            return new GetAllGoalsSummaryRequest(userId);
        }
    }
}
