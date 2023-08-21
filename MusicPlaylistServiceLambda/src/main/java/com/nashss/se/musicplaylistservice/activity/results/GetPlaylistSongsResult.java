package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class GetPlaylistSongsResult {
    private final List<SongModel> songList;

    private GetPlaylistSongsResult(List<SongModel> songList) {
        this.songList = songList;
    }

    public List<SongModel> getSongList() {
        return new ArrayList<>(songList);
    }

    @Override
    public String toString() {
        return "GetPlaylistSongsResult{" +
                "songList=" + songList +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<SongModel> songList;

        public Builder withSongList(List<SongModel> songList) {
            this.songList = new ArrayList<>(songList);
            return this;
        }

        public GetPlaylistSongsResult build() {
            return new GetPlaylistSongsResult(songList);
        }
    }
}
