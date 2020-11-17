package com.example.spotifyar;

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
