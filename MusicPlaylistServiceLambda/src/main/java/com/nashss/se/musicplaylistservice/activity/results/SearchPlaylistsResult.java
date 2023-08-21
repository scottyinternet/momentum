package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.PlaylistModel;

import java.util.ArrayList;
import java.util.List;

public class SearchPlaylistsResult {
    private final List<PlaylistModel> playlists;

    private SearchPlaylistsResult(List<PlaylistModel> playlists) {
        this.playlists = playlists;
    }

    public List<PlaylistModel> getPlaylists() {
        return playlists;
    }

    @Override
    public String toString() {
        return "SearchPlaylistsResult{" +
                "playlists=" + playlists +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<PlaylistModel> playlists ;

        public Builder withPlaylists(List<PlaylistModel> playlists) {
            this.playlists = new ArrayList<>(playlists);
            return this;
        }

        public SearchPlaylistsResult build() {
            return new SearchPlaylistsResult(playlists);
        }
    }
}
