package com.example.spotifyar;

/**
 * Track Item for List View
 */
public class TrackItem {
    public final String artist;
    public final String name;
    public final String id;

    public TrackItem(Song song) {
        this.artist = song.getFirstArtistName();
        this.name = song.getName();
        this.id = song.getId();
    }

    @Override
    public String toString() {
        return "TrackItem{" +
                ", artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
