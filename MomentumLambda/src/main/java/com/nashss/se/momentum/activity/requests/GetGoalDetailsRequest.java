package com.nashss.se.momentum.activity.requests;

public class GetGoalDetailsRequest {

    private String userId;
    private String goalName;
    private String date;

    private GetGoalDetailsRequest(String userId, String goalName, String date) {
        this.userId = userId;
        this.goalName = goalName;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "GetGoalDetailsRequest{" +
                "userId='" + userId + '\'' +
                ", goalName='" + goalName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String goalName;
        private String date;


        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withGoalName(String goalName) {
            this.goalName = goalName;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public GetGoalDetailsRequest build() {
            return new GetGoalDetailsRequest(userId, goalName, date);
        }
    }
}
