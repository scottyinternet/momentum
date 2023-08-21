package com.nashss.se.musicplaylistservice.activity.requests;

public class GetPlaylistRequest {
    private final String id;

    private GetPlaylistRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GetPlaylistRequest{" +
                "id='" + id + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public GetPlaylistRequest build() {
            return new GetPlaylistRequest(id);
        }
    }
}
