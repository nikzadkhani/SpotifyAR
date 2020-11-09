package com.example.spotifyar;

import java.util.Arrays;

public class Artist {
    private String name;
    private String id;
    private String[] genres;
    private String uri;


    /**
     * Object to serialize Artists
     * @param name
     * @param id
     * @param genres
     */
    public Artist(String name, String id, String[] genres, String uri) {
        this.name = name;
        this.id = id;
        this.genres = genres;
        this.uri = uri;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", genres=" + Arrays.toString(genres) +
                '}';
    }
}
