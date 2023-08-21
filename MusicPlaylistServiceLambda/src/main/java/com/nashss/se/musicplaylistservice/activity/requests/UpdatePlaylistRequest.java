package com.nashss.se.musicplaylistservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UpdatePlaylistRequest.Builder.class)
public class UpdatePlaylistRequest {
    private final String id;
    private final String name;
    private final String customerId;

    private UpdatePlaylistRequest(String id, String name, String customerId) {
        this.id = id;
        this.name = name;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "UpdatePlaylistRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
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
        private String name;
        private String customerId;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public UpdatePlaylistRequest build() {
            return new UpdatePlaylistRequest(id, name, customerId);
        }
    }
}
