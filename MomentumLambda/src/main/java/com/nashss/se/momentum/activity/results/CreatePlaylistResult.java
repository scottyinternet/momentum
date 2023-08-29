package com.nashss.se.momentum.activity.results;

import com.nashss.se.momentum.models.PlaylistModel;

public class CreatePlaylistResult {
    private final PlaylistModel playlist;

    private CreatePlaylistResult(PlaylistModel playlist) {
        this.playlist = playlist;
    }

    public PlaylistModel getPlaylist() {
        return playlist;
    }

    @Override
    public String toString() {
        return "CreatePlaylistResult{" +
                "playlist=" + playlist +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PlaylistModel playlist;

        public Builder withPlaylist(PlaylistModel playlist) {
            this.playlist = playlist;
            return this;
        }

        public CreatePlaylistResult build() {
            return new CreatePlaylistResult(playlist);
        }
    }
}
