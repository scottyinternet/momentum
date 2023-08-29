package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.SearchPlaylistsRequest;
import com.nashss.se.momentum.activity.results.SearchPlaylistsResult;
import com.nashss.se.momentum.dynamodb.PlaylistDao;
import com.nashss.se.momentum.dynamodb.models.Playlist;
import com.nashss.se.momentum.models.PlaylistModel;

import com.google.common.collect.Sets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchPlyatlistsTests {
    @Mock
    private PlaylistDao playlistDao;

    private SearchPlaylistsActivity searchPlaylistsActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        searchPlaylistsActivity = new SearchPlaylistsActivity(playlistDao);
    }

    @Test
    public void handleRequest_whenPlaylistsMatchSearch_returnsPlaylistModelListInResult() {
        // GIVEN
        String criteria = "good";
        String[] criteriaArray = {criteria};

        List<Playlist> expected = List.of(
                newPlaylist("id1", "a good playlist", List.of("tag1", "tag2")),
                newPlaylist("id2", "another good playlist", List.of("tag1", "tag2")));

        when(playlistDao.searchPlaylists(criteriaArray)).thenReturn(expected);

        SearchPlaylistsRequest request = SearchPlaylistsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchPlaylistsResult result = searchPlaylistsActivity.handleRequest(request);

        // THEN
        List<PlaylistModel> resultPlaylists = result.getPlaylists();
        assertEquals(expected.size(), resultPlaylists.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).getId(), resultPlaylists.get(i).getId());
            assertEquals(expected.get(i).getName(), resultPlaylists.get(i).getName());
        }
    }

    @Test
    public void handleRequest_withNullCriteria_isIdenticalToEmptyCriteria() {
        // GIVEN
        String criteria = null;
        ArgumentCaptor<String[]> criteriaArray = ArgumentCaptor.forClass(String[].class);

        when(playlistDao.searchPlaylists(criteriaArray.capture())).thenReturn(List.of());

        SearchPlaylistsRequest request = SearchPlaylistsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchPlaylistsResult result = searchPlaylistsActivity.handleRequest(request);

        // THEN
        assertEquals(0, criteriaArray.getValue().length, "Criteria Array should be empty");
    }

    private static Playlist newPlaylist(String id, String name, List<String> tags) {
        Playlist playlist = new Playlist();

        playlist.setId(id);
        playlist.setName(name);
        playlist.setTags(Sets.newHashSet(tags));

        // the test code doesn't need these properties to be distinct.
        playlist.setCustomerId("a customer id");
        playlist.setSongCount(0);

        return playlist;
    }

}
