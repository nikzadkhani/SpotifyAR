package com.example.spotifyar.interfaces;


import com.example.spotifyar.models.Album;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;

public interface SearchFragmentListener {
    void loadTrackData(ArrayList<Track> tracks);
    void loadArtistData(ArrayList<Artist> artists);
    void loadAlbumData(ArrayList<Album> albums);

    void setSearchTrackViewText(Track track);
    void setArtistViewText(Artist artist);
    void setAlbumViewText(Album album);
    
    void danceBtnOnClickSearch();
}