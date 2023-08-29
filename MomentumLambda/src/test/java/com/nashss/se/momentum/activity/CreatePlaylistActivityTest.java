package com.nashss.se.momentum.activity;

import com.nashss.se.momentum.activity.requests.CreatePlaylistRequest;
import com.nashss.se.momentum.activity.results.CreatePlaylistResult;
import com.nashss.se.momentum.dynamodb.PlaylistDao;
import com.nashss.se.momentum.dynamodb.models.Playlist;
import com.nashss.se.momentum.exceptions.InvalidAttributeValueException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreatePlaylistActivityTest {
    @Mock
    private PlaylistDao playlistDao;

    private CreatePlaylistActivity createPlaylistActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createPlaylistActivity = new CreatePlaylistActivity(playlistDao);
    }

    @Test
    public void handleRequest_withTags_createsAndSavesPlaylistWithTags() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedSongCount = 0;
        List<String> expectedTags = List.of("tag");

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                                            .withName(expectedName)
                                            .withCustomerId(expectedCustomerId)
                                            .withTags(expectedTags)
                                            .build();

        // WHEN
        CreatePlaylistResult result = createPlaylistActivity.handleRequest(request);

        // THEN
        verify(playlistDao).savePlaylist(any(Playlist.class));

        assertNotNull(result.getPlaylist().getId());
        assertEquals(expectedName, result.getPlaylist().getName());
        assertEquals(expectedCustomerId, result.getPlaylist().getCustomerId());
        assertEquals(expectedSongCount, result.getPlaylist().getSongCount());
        assertEquals(expectedTags, result.getPlaylist().getTags());
    }

    @Test
    public void handleRequest_noTags_createsAndSavesPlaylistWithoutTags() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedSongCount = 0;

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                                            .withName(expectedName)
                                            .withCustomerId(expectedCustomerId)
                                            .build();

        // WHEN
        CreatePlaylistResult result = createPlaylistActivity.handleRequest(request);

        // THEN
        verify(playlistDao).savePlaylist(any(Playlist.class));

        assertNotNull(result.getPlaylist().getId());
        assertEquals(expectedName, result.getPlaylist().getName());
        assertEquals(expectedCustomerId, result.getPlaylist().getCustomerId());
        assertEquals(expectedSongCount, result.getPlaylist().getSongCount());
        assertNull(result.getPlaylist().getTags());
    }

    @Test
    public void handleRequest_invalidName_throwsInvalidAttributeValueException() {
        // GIVEN
        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                                            .withName("I'm illegal")
                                            .withCustomerId("customerId")
                                            .build();

        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class, () -> createPlaylistActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_invalidCustomerId_throwsInvalidAttributeValueException() {
        // GIVEN
        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                                            .withName("AllOK")
                                            .withCustomerId("Jemma's \"illegal\" customer ID")
                                            .build();

        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class, () -> createPlaylistActivity.handleRequest(request));
    }
}