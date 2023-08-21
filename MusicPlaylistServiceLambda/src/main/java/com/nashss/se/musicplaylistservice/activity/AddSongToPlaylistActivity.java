package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.AddSongToPlaylistRequest;
import com.nashss.se.musicplaylistservice.activity.results.AddSongToPlaylistResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverter;
import com.nashss.se.musicplaylistservice.dynamodb.AlbumTrackDao;
import com.nashss.se.musicplaylistservice.dynamodb.PlaylistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.AlbumTrack;
import com.nashss.se.musicplaylistservice.dynamodb.models.Playlist;
import com.nashss.se.musicplaylistservice.models.SongModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

/**
 * Implementation of the AddSongToPlaylistActivity for the MusicPlaylistService's AddSongToPlaylist API.
 *
 * This API allows the customer to add a song to their existing playlist.
 */
public class AddSongToPlaylistActivity {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;
    private final AlbumTrackDao albumTrackDao;

    /**
     * Instantiates a new AddSongToPlaylistActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlist table.
     * @param albumTrackDao AlbumTrackDao to access the album_track table.
     */
    @Inject
    public AddSongToPlaylistActivity(PlaylistDao playlistDao, AlbumTrackDao albumTrackDao) {
        this.playlistDao = playlistDao;
        this.albumTrackDao = albumTrackDao;
    }

    /**
     * This method handles the incoming request by adding an additional song
     * to a playlist and persisting the updated playlist.
     * <p>
     * It then returns the updated song list of the playlist.
     * <p>
     * If the playlist does not exist, this should throw a PlaylistNotFoundException.
     * <p>
     * If the album track does not exist, this should throw an AlbumTrackNotFoundException.
     *
     * @param addSongToPlaylistRequest request object containing the playlist ID and an asin and track number
     *                                 to retrieve the song data
     * @return addSongToPlaylistResult result object containing the playlist's updated list of
     *                                 API defined {@link SongModel}s
     */
    public AddSongToPlaylistResult handleRequest(final AddSongToPlaylistRequest addSongToPlaylistRequest) {
        log.info("Received AddSongToPlaylistRequest {} ", addSongToPlaylistRequest);

        String asin = addSongToPlaylistRequest.getAsin();
        // Allow NPE when unboxing Integer if track number is null (getTrackNumber returns Integer)
        int trackNumber = addSongToPlaylistRequest.getTrackNumber();

        Playlist playlist = playlistDao.getPlaylist(addSongToPlaylistRequest.getId());

        if (!playlist.getCustomerId().equals(addSongToPlaylistRequest.getCustomerId())) {
            throw new SecurityException("You must own a playlist to add songs to it.");
        }

        AlbumTrack albumTrackToAdd = albumTrackDao.getAlbumTrack(asin, trackNumber);

        LinkedList<AlbumTrack> albumTracks = (LinkedList<AlbumTrack>) (playlist.getSongList());
        if (addSongToPlaylistRequest.isQueueNext()) {
            albumTracks.addFirst(albumTrackToAdd);
        } else {
            albumTracks.addLast(albumTrackToAdd);
        }

        playlist.setSongList(albumTracks);
        playlist.setSongCount(playlist.getSongList().size());
        playlist = playlistDao.savePlaylist(playlist);

        List<SongModel> songModels = new ModelConverter().toSongModelList(playlist.getSongList());
        return AddSongToPlaylistResult.builder()
                .withSongList(songModels)
                .build();
    }
}
