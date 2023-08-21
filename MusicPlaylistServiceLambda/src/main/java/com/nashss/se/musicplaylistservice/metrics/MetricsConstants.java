package com.nashss.se.musicplaylistservice.metrics;

/**
 * Constant values for use with metrics.
 */
public class MetricsConstants {
    public static final String GETPLAYLIST_PLAYLISTNOTFOUND_COUNT = "GetPlaylist.PlaylistNotFoundException.Count";
    public static final String UPDATEPLAYLIST_INVALIDATTRIBUTEVALUE_COUNT =
        "UpdatePlaylist.InvalidAttributeValueException.Count";
    public static final String UPDATEPLAYLIST_INVALIDATTRIBUTECHANGE_COUNT =
        "UpdatePlaylist.InvalidAttributeChangeException.Count";
    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "MusicPlaylistService";
    public static final String NAMESPACE_NAME = "U3/MusicPlaylistService";
}
