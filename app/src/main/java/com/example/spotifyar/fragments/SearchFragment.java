package com.example.spotifyar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotifyar.R;
import com.example.spotifyar.interfaces.SearchFragmentListener;
import com.example.spotifyar.models.Album;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Track;

public class SearchFragment extends Fragment {
    private Button danceBtn;
    private TextView searchedTrackView;


    private SearchRecyclerFragment SearchResultFrag;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        danceBtn = (Button) view.findViewById(R.id.searchStartArBtn);
        searchedTrackView = (TextView) view.findViewById(R.id.searchedTrackView);

        searchedTrackView.setText("");

        danceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchFragmentListener) getContext()).danceBtnOnClickSearch();
            }
        });

        return view;
    }

    public void setConfirmTrackViewText(Track track) {
        String newConfirmText = track.name + " by " + track.artists.get(0).name;
        searchedTrackView.setText(newConfirmText);
    }

    public void setConfirmArtistViewText(Artist artist) {
        String newConfirmText = artist.name;
        searchedTrackView.setText(newConfirmText);
    }

    public void setConfirmAlbumViewText(Album album) {
        String newConfirmText = album.getName() + " by " + album.getFirstArtistName();
        searchedTrackView.setText(newConfirmText);
    }
}