package com.nashss.se.musicplaylistservice.dynamodb.models;

import com.nashss.se.musicplaylistservice.converters.AlbumTrackLinkedListConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a record in the playlists table.
 */
@DynamoDBTable(tableName = "playlists")
public class Playlist {
    private String id;
    private String name;
    private String customerId;
    private String customerName;
    private Integer songCount;
    private Set<String> tags;
    private List<AlbumTrack> songList;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // "name" is a reserved word in DDB, so the attribute in the table is called "playlistName".
    @DynamoDBAttribute(attributeName = "playlistName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBAttribute(attributeName = "customerName")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @DynamoDBAttribute(attributeName = "songCount")
    public Integer getSongCount() {
        return songCount;
    }

    public void setSongCount(Integer songCount) {
        this.songCount = songCount;
    }

    /**
     * Returns the set of tags associated with this Playlist, null if there are none.
     *
     * @return Set of tags for this playlist
     */
    @DynamoDBAttribute(attributeName = "tags")
    public Set<String> getTags() {
        // normally, we would prefer to return an empty Set if there are no
        // tags, but DynamoDB doesn't represent empty Sets...needs to be null
        // instead
        if (null == tags) {
            return null;
        }

        return new HashSet<>(tags);
    }

    /**
     * Sets the tags for this Playlist as a copy of input, or null if input is null.
     *
     * @param tags Set of tags for this playlist
     */
    public void setTags(Set<String> tags) {
        // see comment in getTags()
        if (null == tags) {
            this.tags = null;
        } else {
            this.tags = new HashSet<>(tags);
        }

        this.tags = tags;
    }

    // PARTICIPANTS: You do not need to modify the songList getters/setters or annotations
    @DynamoDBTypeConverted(converter = AlbumTrackLinkedListConverter.class)
    @DynamoDBAttribute(attributeName = "songList")
    public List<AlbumTrack> getSongList() {
        return songList;
    }

    public void setSongList(List<AlbumTrack> songList) {
        this.songList = songList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Playlist playlist = (Playlist) o;
        return id.equals(playlist.id) &&
                name.equals(playlist.name) &&
                customerId.equals(playlist.customerId) &&
                customerName.equals(playlist.customerName) &&
                songCount.equals(playlist.songCount) &&
                Objects.equals(tags, playlist.tags) &&
                Objects.equals(songList, playlist.songList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customerId, customerName, songCount, tags, songList);
    }

}
