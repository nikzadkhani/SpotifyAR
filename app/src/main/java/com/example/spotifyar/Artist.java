package com.example.spotifyar;

import java.util.Arrays;

public class Artist {
    private String name;
    private String id;
    private String[] genres;


    /**
     * Object to serialize Artists
     * @param name
     * @param id
     * @param genres
     */
    public Artist(String name, String id, String[] genres) {
        this.name = name;
        this.id = id;
        this.genres = genres;
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

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", genres=" + Arrays.toString(genres) +
                '}';
    }
}
