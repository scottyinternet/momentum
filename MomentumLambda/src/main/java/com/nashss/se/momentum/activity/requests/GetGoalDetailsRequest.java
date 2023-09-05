package com.nashss.se.momentum.activity.requests;

public class GetGoalDetailsRequest {

    private String userId;
    private String goalName;


    private GetGoalDetailsRequest(String userId, String goalName) {
        this.userId = userId;
        this.goalName = goalName;
    }

    public String getUserId() {
        return userId;
    }

    public String getGoalName() {
        return goalName;
    }

    @Override
    public String  toString() {
        return "GetGoalDetailsRequest{" +
                "userId='" + userId + '\'' +
                ", goalName='" + goalName + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String goalName;


        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public GetGoalDetailsRequest build() {
            return new GetGoalDetailsRequest(userId, goalName);
        }
    }
}
