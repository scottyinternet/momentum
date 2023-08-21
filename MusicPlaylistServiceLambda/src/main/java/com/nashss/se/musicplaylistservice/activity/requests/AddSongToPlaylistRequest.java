package com.nashss.se.musicplaylistservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = AddSongToPlaylistRequest.Builder.class)
public class AddSongToPlaylistRequest {
    private final String id;
    private final String asin;
    private final int trackNumber;
    private final boolean queueNext;
    private final String customerId;

    private AddSongToPlaylistRequest(String id, String asin, int trackNumber, boolean queueNext, String customerId) {
        this.id = id;
        this.asin = asin;
        this.trackNumber = trackNumber;
        this.queueNext = queueNext;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public String getAsin() {
        return asin;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public boolean isQueueNext() {
        return queueNext;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "AddSongToPlaylistRequest{" +
                "id='" + id + '\'' +
                ", asin='" + asin + '\'' +
                ", trackNumber=" + trackNumber +
                ", queueNext=" + queueNext +
                ", customerId='" + customerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String id;
        private String asin;
        private int trackNumber;
        private boolean queueNext;
        private String customerId;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }

        public Builder withTrackNumber(int trackNumber) {
            this.trackNumber = trackNumber;
            return this;
        }

        public Builder withQueueNext(boolean queueNext) {
            this.queueNext = queueNext;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public AddSongToPlaylistRequest build() {
            return new AddSongToPlaylistRequest(id, asin, trackNumber, queueNext, customerId);
        }
    }
}
