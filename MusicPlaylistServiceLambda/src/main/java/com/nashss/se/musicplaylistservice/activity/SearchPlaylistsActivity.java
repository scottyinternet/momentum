package com.nashss.se.musicplaylistservice.activity;

import com.nashss.se.musicplaylistservice.activity.requests.SearchPlaylistsRequest;
import com.nashss.se.musicplaylistservice.activity.results.SearchPlaylistsResult;
import com.nashss.se.musicplaylistservice.converters.ModelConverter;
import com.nashss.se.musicplaylistservice.dynamodb.PlaylistDao;
import com.nashss.se.musicplaylistservice.dynamodb.models.Playlist;
import com.nashss.se.musicplaylistservice.models.PlaylistModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import javax.inject.Inject;

import static com.nashss.se.musicplaylistservice.utils.NullUtils.ifNull;

/**
 * Implementation of the SearchPlaylistActivity for the MusicPlaylistService's SearchPlaylists API.
 * <p>
 * This API allows the customer to search for playlists by name or tag.
 */
public class SearchPlaylistsActivity {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

    /**
     * Instantiates a new SearchPlaylistsActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlist table.
     */
    @Inject
    public SearchPlaylistsActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    /**
     * This method handles the incoming request by searching for playlist from the database.
     * <p>
     * It then returns the matching playlists, or an empty result list if none are found.
     *
     * @param searchPlaylistsRequest request object containing the search criteria
     * @return searchPlaylistsResult result object containing the playlists that match the
     * search criteria.
     */
    public SearchPlaylistsResult handleRequest(final SearchPlaylistsRequest searchPlaylistsRequest) {
        log.info("Received SearchPlaylistsRequest {}", searchPlaylistsRequest);

        String criteria = ifNull(searchPlaylistsRequest.getCriteria(), "");
        String[] criteriaArray = criteria.isBlank() ? new String[0] : criteria.split("\\s");

        List<Playlist> results = playlistDao.searchPlaylists(criteriaArray);
        List<PlaylistModel> playlistModels = new ModelConverter().toPlaylistModelList(results);

        return SearchPlaylistsResult.builder()
                .withPlaylists(playlistModels)
                .build();
    }
}
