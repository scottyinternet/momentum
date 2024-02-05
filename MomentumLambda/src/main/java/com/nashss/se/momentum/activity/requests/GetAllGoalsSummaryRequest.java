package com.nashss.se.momentum.activity.requests;

import java.time.LocalDate;

public class GetAllGoalsSummaryRequest {

    private String userId;
    private String date;

    public GetAllGoalsSummaryRequest(String userId, String date) {
        this.userId = userId;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "GetAllGoalsSummaryRequest{" +
                "userId='" + userId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String date;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public GetAllGoalsSummaryRequest build() {
            String nullCheckDate = date != null ? date : LocalDate.now().toString();
            return new GetAllGoalsSummaryRequest(userId, nullCheckDate);
        }
    }
}
