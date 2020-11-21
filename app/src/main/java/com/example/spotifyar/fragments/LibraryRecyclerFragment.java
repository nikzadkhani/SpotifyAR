package com.example.spotifyar.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifyar.adapters.TrackRecyclerViewAdapter;
import com.example.spotifyar.interfaces.OnItemClickListener;
import com.example.spotifyar.interfaces.TrackFragmentListener;

import com.example.spotifyar.services.TrackService;
import com.example.spotifyar.R;


import com.spotify.protocol.types.Track;

/**
 * A fragment representing a list of Items.
 */
public class LibraryRecyclerFragment extends Fragment implements OnItemClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private TrackService trackService;
    private Track[] tracks;
    
    private RecyclerView recyclerView;
    
    private int currentSelectedTrackIndex;

    private TrackFragmentListener TFL;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LibraryRecyclerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LibraryRecyclerFragment newInstance(int columnCount) {
        LibraryRecyclerFragment fragment = new LibraryRecyclerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        trackService = new TrackService(context);
        TFL = (TrackFragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onTrackItemClicked(int position) {
        this.currentSelectedTrackIndex = position;
        TFL.setTrackViewText(tracks[position]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_item_list, container, false);
//        TFL.setTrackViewText("yellow");


        // Set the adapter
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
                recyclerView.setAdapter(new TrackRecyclerViewAdapter(context, tracks, this));
                recyclerView.setHasFixedSize(true);
            });
        }
        return view;
    }
    
    public Track  getCurrentSelectedTrack() {
        return tracks[this.currentSelectedTrackIndex];
    }
}