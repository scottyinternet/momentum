package com.nashss.se.musicplaylistservice.models;

import java.util.Objects;

public class SongModel {
    private final String asin;
    private final String title;
    private final String album;
    private final int trackNumber;

    private SongModel(String asin, String title, String album, int trackNumber) {
        this.asin = asin;
        this.title = title;
        this.album = album;
        this.trackNumber = trackNumber;
    }

    public String getAsin() {
        return asin;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SongModel songModel = (SongModel) o;
        return trackNumber == songModel.trackNumber &&
                asin.equals(songModel.asin) &&
                title.equals(songModel.title) &&
                album.equals(songModel.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asin, title, album, trackNumber);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String asin;
        private String title;
        private String album;
        private int trackNumber;

        public Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAlbum(String album) {
            this.album = album;
            return this;
        }

        public Builder withTrackNumber(int trackNumber) {
            this.trackNumber = trackNumber;
            return this;
        }

        public SongModel build() {
            return new SongModel(asin, title, album, trackNumber);
        }
    }
}
