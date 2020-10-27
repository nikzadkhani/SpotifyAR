package com.example.spotifyar;

import java.util.Arrays;

public class Song {
    private String id;
    private String name;
    private Artist[] artists;
    private String uri;

    public Song(String id, String name, Artist[] artists, String uri) {
        this.name = name;
        this.id = id;
        this.artists = artists;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstArtistName() {return artists[0].getName(); }

    public String getUri() { return uri; }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artists=" + Arrays.toString(artists) +
                ", uri='" + uri + '\'' +
                '}';
    }
}
