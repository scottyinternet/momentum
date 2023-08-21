package com.nashss.se.musicplaylistservice.activity.results;

import com.nashss.se.musicplaylistservice.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class AddSongToPlaylistResult {
    private final List<SongModel> songList;

    private AddSongToPlaylistResult(List<SongModel> songList) {
        this.songList = songList;
    }

    public List<SongModel> getSongList() {
        return new ArrayList<>(songList);
    }

    @Override
    public String toString() {
        return "AddSongToPlaylistResult{" +
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

        public AddSongToPlaylistResult build() {
            return new AddSongToPlaylistResult(songList);
        }
    }
}
