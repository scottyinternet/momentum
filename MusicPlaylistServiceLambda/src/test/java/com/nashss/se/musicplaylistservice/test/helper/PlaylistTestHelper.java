package com.nashss.se.musicplaylistservice.test.helper;

import com.nashss.se.musicplaylistservice.dynamodb.models.AlbumTrack;
import com.nashss.se.musicplaylistservice.dynamodb.models.Playlist;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class PlaylistTestHelper {
    private PlaylistTestHelper() {
    }

    public static Playlist generatePlaylist() {
        return generatePlaylistWithNAlbumTracks(1);
    }

    public static Playlist generatePlaylistWithNAlbumTracks(int numTracks) {
        Playlist playlist = new Playlist();
        playlist.setId("id");
        playlist.setName("a playlist");
        playlist.setCustomerId("CustomerABC");
        playlist.setTags(Collections.singleton("tag"));

        List<AlbumTrack> albumTracks = new LinkedList<>();
        for (int i = 0; i < numTracks; i++) {
            albumTracks.add(AlbumTrackTestHelper.generateAlbumTrack(i));
        }
        playlist.setSongList(albumTracks);
        playlist.setSongCount(albumTracks.size());

        return playlist;
    }
}
