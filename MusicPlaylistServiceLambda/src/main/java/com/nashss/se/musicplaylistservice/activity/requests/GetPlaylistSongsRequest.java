package com.nashss.se.musicplaylistservice.activity.requests;

public class GetPlaylistSongsRequest {
    private final String id;
    private final String order;

    private GetPlaylistSongsRequest(String id, String order) {
        this.id = id;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public String getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "GetPlaylistSongsRequest{" +
                "id='" + id + '\'' +
                ", order='" + order + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String order;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withOrder(String order) {
            this.order = order;
            return this;
        }

        public GetPlaylistSongsRequest build() {
            return new GetPlaylistSongsRequest(id, order);
        }
    }
}
