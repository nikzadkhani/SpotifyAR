package com.example.spotifyar.models;

import com.spotify.protocol.types.Artist;

public class Album {
    private Artist[] artists;
    private String id;
    private String uri;
    private String name;

    public Album(Artist[] artists, String id, String uri, String name) {
        this.artists = artists;
        this.id = id;
        this.uri = uri;
        this.name = name;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public String getFirstArtistName() {
        return artists[0].name;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }
}
