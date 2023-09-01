package com.nashss.se.momentum.converters;

import com.nashss.se.momentum.dynamodb.models.Event;
import com.nashss.se.momentum.models.EventModel;
import com.nashss.se.momentum.models.PlaylistModel;
import com.nashss.se.momentum.models.SongModel;
import com.nashss.se.momentum.dynamodb.models.AlbumTrack;
import com.nashss.se.momentum.dynamodb.models.Playlist;
import com.nashss.se.momentum.test.helper.AlbumTrackTestHelper;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static com.nashss.se.momentum.utils.CollectionUtils.copyToSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelConverterTest {
    private ModelConverter modelConverter = new ModelConverter();

    @Test
    void toPlaylistModel_withTags_convertsPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setId("id");
        playlist.setName("name");
        playlist.setCustomerId("customerId");
        playlist.setSongCount(0);
        playlist.setTags(Sets.newHashSet("tag"));

        PlaylistModel playlistModel = modelConverter.toPlaylistModel(playlist);
        assertEquals(playlist.getId(), playlistModel.getId());
        assertEquals(playlist.getName(), playlistModel.getName());
        assertEquals(playlist.getCustomerId(), playlistModel.getCustomerId());
        assertEquals(playlist.getSongCount(), playlistModel.getSongCount());
        assertEquals(playlist.getTags(), copyToSet(playlistModel.getTags()));
    }

    @Test
    void toPlaylistModel_nullTags_convertsPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setId("id");
        playlist.setName("name");
        playlist.setCustomerId("customerId");
        playlist.setSongCount(0);
        playlist.setTags(null);

        PlaylistModel playlistModel = modelConverter.toPlaylistModel(playlist);
        assertEquals(playlist.getId(), playlistModel.getId());
        assertEquals(playlist.getName(), playlistModel.getName());
        assertEquals(playlist.getCustomerId(), playlistModel.getCustomerId());
        assertEquals(playlist.getSongCount(), playlistModel.getSongCount());
        assertNull(playlistModel.getTags());
    }

    @Test
    void toSongModel_withAlbumTrack_convertsToSongModel() {
        // GIVEN
        AlbumTrack albumTrack = AlbumTrackTestHelper.generateAlbumTrack(2);

        // WHEN
        SongModel result = modelConverter.toSongModel(albumTrack);

        // THEN
        AlbumTrackTestHelper.assertAlbumTrackEqualsSongModel(
            albumTrack,
            result,
            String.format("Expected album track %s to match song model %s",
                          albumTrack,
                          result)
        );
    }

    @Test
    void toSongModelList_withAlbumTracks_convertsToSongModelList() {
        // GIVEN
        // list of AlbumTracks
        int numTracks = 4;
        List<AlbumTrack> albumTracks = new LinkedList<>();
        for (int i = 0; i < numTracks; i++) {
            albumTracks.add(AlbumTrackTestHelper.generateAlbumTrack(i));
        }

        // WHEN
        List<SongModel> result = modelConverter.toSongModelList(albumTracks);

        // THEN
        AlbumTrackTestHelper.assertAlbumTracksEqualSongModels(albumTracks, result);
    }

    @Test
    void toEventModel_withEvent_convertsToEventModel() {
        Event event = new Event();
        event.setEventId("1234");
        event.setGoalId("G1234");
        event.setDate(LocalDate.parse("2023-01-01"));
        event.setMeasurement(12.0);
        EventModel eventModel = modelConverter.toEventModel(event);

        assertEquals(event.getEventId(), eventModel.getEventId());
        assertEquals(event.getGoalId(), eventModel.getGoalId());
        assertEquals(event.getDate(), eventModel.getDateOfEvent());
        assertEquals(event.getMeasurement(), eventModel.getMeasurement());
    }
}
