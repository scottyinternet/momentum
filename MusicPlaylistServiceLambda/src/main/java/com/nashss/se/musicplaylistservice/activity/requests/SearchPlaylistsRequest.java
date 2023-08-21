package com.nashss.se.musicplaylistservice.activity.requests;

public class SearchPlaylistsRequest {
    private final String criteria;

    private SearchPlaylistsRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchPlaylistsRequest{" +
                "criteria='" + criteria + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String criteria;

        public Builder withCriteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        public SearchPlaylistsRequest build() {
            return new SearchPlaylistsRequest(criteria);
        }
    }
}
