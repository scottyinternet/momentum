package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.GetPlaylistSongsRequest;
import com.nashss.se.musicplaylistservice.activity.results.GetPlaylistSongsResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverter;
import com.nashss.se.musicplaylistservice.dynamodb.PlaylistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Playlist;
import com.nashss.se.musicplaylistservice.exceptions.InvalidAttributeValueException;
import com.nashss.se.musicplaylistservice.models.SongModel;
import com.nashss.se.musicplaylistservice.models.SongOrder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

/**
 * Implementation of the GetPlaylistSongsActivity for the MusicPlaylistService's GetPlaylistSongs API.
 * <p>
 * This API allows the customer to get the list of songs of a saved playlist.
 */
public class GetPlaylistSongsActivity {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

    /**
     * Instantiates a new GetPlaylistSongsActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlist table.
     */
    @Inject
    public GetPlaylistSongsActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    /**
     * This method handles the incoming request by retrieving the playlist from the database.
     * <p>
     * It then returns the playlist's song list.
     * <p>
     * If the playlist does not exist, this should throw a PlaylistNotFoundException.
     *
     * @param getPlaylistSongsRequest request object containing the playlist ID
     * @return getPlaylistSongsResult result object containing the playlist's list of API defined {@link SongModel}s
     */
    public GetPlaylistSongsResult handleRequest(final GetPlaylistSongsRequest getPlaylistSongsRequest) {
        log.info("Received GetPlaylistSongsRequest {}", getPlaylistSongsRequest);

        String songOrder = computeSongOrder(getPlaylistSongsRequest.getOrder());

        Playlist playlist = playlistDao.getPlaylist(getPlaylistSongsRequest.getId());
        List<SongModel> songModels = new ModelConverter().toSongModelList(playlist.getSongList());

        if (songOrder.equals(SongOrder.REVERSED)) {
            Collections.reverse(songModels);
        } else if (songOrder.equals(SongOrder.SHUFFLED)) {
            Collections.shuffle(songModels);
        }

        return GetPlaylistSongsResult.builder()
                .withSongList(songModels)
                .build();
    }

    private String computeSongOrder(String songOrder) {
        String computedSongOrder = songOrder;

        if (null == songOrder) {
            computedSongOrder = SongOrder.DEFAULT;
        } else if (!Arrays.asList(SongOrder.values()).contains(songOrder)) {
            throw new InvalidAttributeValueException(String.format("Unrecognized sort order: '%s'", songOrder));
        }

        return computedSongOrder;
    }
}
