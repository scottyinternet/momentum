package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.UpdatePlaylistRequest;
import com.nashss.se.momentum.activity.results.UpdatePlaylistResult;
import com.nashss.se.momentum.dynamodb.PlaylistDao;
import com.nashss.se.momentum.dynamodb.models.Playlist;
import com.nashss.se.momentum.exceptions.InvalidAttributeValueException;
import com.nashss.se.momentum.exceptions.PlaylistNotFoundException;
import com.nashss.se.momentum.metrics.MetricsConstants;
import com.nashss.se.momentum.metrics.MetricsPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdatePlaylistActivityTest {
    @Mock
    private PlaylistDao playlistDao;

    @Mock
    private MetricsPublisher metricsPublisher;

    private UpdatePlaylistActivity updatePlaylistActivity;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        updatePlaylistActivity = new UpdatePlaylistActivity(playlistDao, metricsPublisher);
    }

    @Test
    public void handleRequest_goodRequest_updatesPlaylistName() {
        // GIVEN
        String id = "id";
        String expectedCustomerId = "expectedCustomerId";
        String expectedName = "new name";
        int expectedSongCount = 10;

        UpdatePlaylistRequest request = UpdatePlaylistRequest.builder()
                                            .withId(id)
                                            .withCustomerId(expectedCustomerId)
                                            .withName(expectedName)
                                            .build();

        Playlist startingPlaylist = new Playlist();
        startingPlaylist.setCustomerId(expectedCustomerId);
        startingPlaylist.setName("old name");
        startingPlaylist.setSongCount(expectedSongCount);

        when(playlistDao.getPlaylist(id)).thenReturn(startingPlaylist);
        when(playlistDao.savePlaylist(startingPlaylist)).thenReturn(startingPlaylist);

        // WHEN
        UpdatePlaylistResult result = updatePlaylistActivity.handleRequest(request);

        // THEN
        assertEquals(expectedName, result.getPlaylist().getName());
        assertEquals(expectedCustomerId, result.getPlaylist().getCustomerId());
        assertEquals(expectedSongCount, result.getPlaylist().getSongCount());
    }

    @Test
    public void handleRequest_invalidName_throwsInvalidAttributeValueException() {
        // GIVEN
        UpdatePlaylistRequest request = UpdatePlaylistRequest.builder()
                                            .withId("id")
                                            .withName("I'm illegal")
                                            .withCustomerId("customerId")
                                            .build();

        // WHEN + THEN
        try {
            updatePlaylistActivity.handleRequest(request);
            fail("Expected InvalidAttributeValueException to be thrown");
        } catch (InvalidAttributeValueException e) {
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPLAYLIST_INVALIDATTRIBUTEVALUE_COUNT, 1);
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPLAYLIST_INVALIDATTRIBUTECHANGE_COUNT, 0);
        }
    }

    @Test
    public void handleRequest_playlistDoesNotExist_throwsPlaylistNotFoundException() {
        // GIVEN
        String id = "id";
        UpdatePlaylistRequest request = UpdatePlaylistRequest.builder()
                                            .withId(id)
                                            .withName("name")
                                            .withCustomerId("customerId")
                                            .build();

        when(playlistDao.getPlaylist(id)).thenThrow(new PlaylistNotFoundException());

        // THEN
        assertThrows(PlaylistNotFoundException.class, () -> updatePlaylistActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_customerIdNotMatch_throwsSecurityException() {
        // GIVEN
        String id = "id";
        UpdatePlaylistRequest request = UpdatePlaylistRequest.builder()
                                            .withId(id)
                                            .withName("name")
                                            .withCustomerId("customerId")
                                            .build();

        Playlist differentCustomerIdPlaylist = new Playlist();
        differentCustomerIdPlaylist.setCustomerId("different");

        when(playlistDao.getPlaylist(id)).thenReturn(differentCustomerIdPlaylist);

        // WHEN + THEN
        try {
            updatePlaylistActivity.handleRequest(request);
            fail("Expected InvalidAttributeChangeException to be thrown");
        } catch (SecurityException e) {
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPLAYLIST_INVALIDATTRIBUTEVALUE_COUNT, 0);
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPLAYLIST_INVALIDATTRIBUTECHANGE_COUNT, 1);
        }
    }
}
