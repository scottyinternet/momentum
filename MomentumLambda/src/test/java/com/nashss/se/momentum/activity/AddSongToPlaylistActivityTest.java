package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.AddSongToPlaylistRequest;
import com.nashss.se.momentum.activity.results.AddSongToPlaylistResult;
import com.nashss.se.momentum.models.SongModel;
import com.nashss.se.momentum.dynamodb.AlbumTrackDao;
import com.nashss.se.momentum.dynamodb.PlaylistDao;
import com.nashss.se.momentum.dynamodb.models.AlbumTrack;
import com.nashss.se.momentum.dynamodb.models.Playlist;
import com.nashss.se.momentum.exceptions.AlbumTrackNotFoundException;
import com.nashss.se.momentum.exceptions.PlaylistNotFoundException;
import com.nashss.se.momentum.test.helper.AlbumTrackTestHelper;
import com.nashss.se.momentum.test.helper.PlaylistTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AddSongToPlaylistActivityTest {
    @Mock
    private PlaylistDao playlistDao;

    @Mock
    private AlbumTrackDao albumTrackDao;

    private AddSongToPlaylistActivity addSongToPlaylistActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        addSongToPlaylistActivity = new AddSongToPlaylistActivity(playlistDao, albumTrackDao);
    }

    @Test
    void handleRequest_validRequest_addsSongToEndOfPlaylist() {
        // GIVEN
        // a non-empty playlist
        Playlist originalPlaylist = PlaylistTestHelper.generatePlaylist();
        String playlistId = originalPlaylist.getId();
        String customerId = originalPlaylist.getCustomerId();

        // the new song to add to the playlist
        AlbumTrack albumTrackToAdd = AlbumTrackTestHelper.generateAlbumTrack(2);
        String addedAsin = albumTrackToAdd.getAsin();
        int addedTracknumber = albumTrackToAdd.getTrackNumber();

        when(playlistDao.getPlaylist(playlistId)).thenReturn(originalPlaylist);
        when(playlistDao.savePlaylist(originalPlaylist)).thenReturn(originalPlaylist);
        when(albumTrackDao.getAlbumTrack(addedAsin, addedTracknumber)).thenReturn(albumTrackToAdd);

        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder()
            .withId(playlistId)
            .withAsin(addedAsin)
            .withTrackNumber(addedTracknumber)
            .withCustomerId(customerId)
            .build();

        // WHEN
        AddSongToPlaylistResult result = addSongToPlaylistActivity.handleRequest(request);

        // THEN
        verify(playlistDao).savePlaylist(originalPlaylist);

        assertEquals(2, result.getSongList().size());
        SongModel secondSong = result.getSongList().get(1);
        AlbumTrackTestHelper.assertAlbumTrackEqualsSongModel(albumTrackToAdd, secondSong);
    }

    @Test
    public void handleRequest_noMatchingPlaylistId_throwsPlaylistNotFoundException() {
        // GIVEN
        String playlistId = "missing id";
        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder()
                                               .withId(playlistId)
                                               .withAsin("asin")
                                               .withTrackNumber(1)
                                               .withCustomerId("doesn't matter")
                                               .build();
        when(playlistDao.getPlaylist(playlistId)).thenThrow(new PlaylistNotFoundException());

        // WHEN + THEN
        assertThrows(PlaylistNotFoundException.class, () -> addSongToPlaylistActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_noMatchingAlbumTrack_throwsAlbumTrackNotFoundException() {
        // GIVEN
        Playlist playlist = PlaylistTestHelper.generatePlaylist();

        String playlistId = playlist.getId();
        String cusomerId = playlist.getCustomerId();
        String asin = "nonexistent asin";
        int trackNumber = -1;
        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder()
                                               .withId(playlistId)
                                               .withAsin(asin)
                                               .withTrackNumber(trackNumber)
                                               .withCustomerId(cusomerId)
                                               .build();

        // WHEN
        when(playlistDao.getPlaylist(playlistId)).thenReturn(playlist);
        when(albumTrackDao.getAlbumTrack(asin, trackNumber)).thenThrow(new AlbumTrackNotFoundException());

        // THEN
        assertThrows(AlbumTrackNotFoundException.class, () -> addSongToPlaylistActivity.handleRequest(request));
    }

    @Test
    void handleRequest_validRequestWithQueueNextFalse_addsSongToEndOfPlaylist() {
        // GIVEN
        int startingTrackCount = 3;
        Playlist originalPlaylist = PlaylistTestHelper.generatePlaylistWithNAlbumTracks(startingTrackCount);
        String playlistId = originalPlaylist.getId();
        String customerId = originalPlaylist.getCustomerId();

        // the new song to add to the playlist
        AlbumTrack albumTrackToAdd = AlbumTrackTestHelper.generateAlbumTrack(8);
        String addedAsin = albumTrackToAdd.getAsin();
        int addedTracknumber = albumTrackToAdd.getTrackNumber();

        when(playlistDao.getPlaylist(playlistId)).thenReturn(originalPlaylist);
        when(playlistDao.savePlaylist(originalPlaylist)).thenReturn(originalPlaylist);
        when(albumTrackDao.getAlbumTrack(addedAsin, addedTracknumber)).thenReturn(albumTrackToAdd);

        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder()
                                               .withId(playlistId)
                                               .withAsin(addedAsin)
                                               .withTrackNumber(addedTracknumber)
                                               .withQueueNext(false)
                                               .withCustomerId(customerId)
                                               .build();

        // WHEN
        AddSongToPlaylistResult result = addSongToPlaylistActivity.handleRequest(request);

        // THEN
        verify(playlistDao).savePlaylist(originalPlaylist);

        assertEquals(startingTrackCount + 1, result.getSongList().size());
        SongModel lastSong = result.getSongList().get(result.getSongList().size() - 1);
        AlbumTrackTestHelper.assertAlbumTrackEqualsSongModel(albumTrackToAdd, lastSong);
    }

    @Test
    void handleRequest_validRequestWithQueueNextTrue_addsSongToBeginningOfPlaylist() {
        // GIVEN
        int startingPlaylistSize = 2;
        Playlist originalPlaylist = PlaylistTestHelper.generatePlaylistWithNAlbumTracks(startingPlaylistSize);
        String playlistId = originalPlaylist.getId();
        String customerId = originalPlaylist.getCustomerId();

        // the new song to add to the playlist
        AlbumTrack albumTrackToAdd = AlbumTrackTestHelper.generateAlbumTrack(6);
        String addedAsin = albumTrackToAdd.getAsin();
        int addedTracknumber = albumTrackToAdd.getTrackNumber();

        when(playlistDao.getPlaylist(playlistId)).thenReturn(originalPlaylist);
        when(playlistDao.savePlaylist(originalPlaylist)).thenReturn(originalPlaylist);
        when(albumTrackDao.getAlbumTrack(addedAsin, addedTracknumber)).thenReturn(albumTrackToAdd);

        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder()
                                               .withId(playlistId)
                                               .withAsin(addedAsin)
                                               .withTrackNumber(addedTracknumber)
                                               .withQueueNext(true)
                                               .withCustomerId(customerId)
                                               .build();

        // WHEN
        AddSongToPlaylistResult result = addSongToPlaylistActivity.handleRequest(request);

        // THEN
        verify(playlistDao).savePlaylist(originalPlaylist);

        assertEquals(startingPlaylistSize + 1, result.getSongList().size());
        SongModel firstSong = result.getSongList().get(0);
        AlbumTrackTestHelper.assertAlbumTrackEqualsSongModel(albumTrackToAdd, firstSong);
    }
}
