package com.example.spotifyar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyar.R;
import com.example.spotifyar.adapters.TrackRecyclerViewAdapter;
import com.example.spotifyar.interfaces.TrackFragmentListener;
import com.example.spotifyar.services.TrackService;
import com.spotify.protocol.types.Track;

/**
 * A fragment representing a list of Items.
 */
public class LibraryRecyclerFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private TrackService trackService;
    private Track[] tracks;
    
    private RecyclerView recyclerView;
    
    private int currentSelectedTrackIndex;


    public LibraryRecyclerFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        trackService = new TrackService(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_item_list, container, false);
//        TFL.setTrackViewText("yellow");


        // Set the adapter so list of tracks is shown
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            trackService.getLibraryTracks(() -> {
                tracks = trackService.getLibraryTracks();
                recyclerView.setAdapter(new TrackRecyclerViewAdapter(context, tracks, (TrackFragmentListener) getContext()));
                recyclerView.setHasFixedSize(true);
            });
        }
        return view;
    }
    
    //send track information to the adapter to display
    public Track  getCurrentSelectedTrack() {
        return tracks[this.currentSelectedTrackIndex];
    }
}