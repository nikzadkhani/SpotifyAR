package com.example.spotifyar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TrackFragment extends Fragment implements OnItemClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private SongService songService;
    private TrackItem[] tracks;
    
    private RecyclerView recyclerView;
    
    private int currentSelectedTrackIndex;

    private TrackFragmentListener TFL;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrackFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TrackFragment newInstance(int columnCount) {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        songService = new SongService(context);
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
        TFL.setSongViewText(tracks[position]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_item_list, container, false);
//        TFL.setSongViewText("yellow");


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            songService.getLibraryTracks(() -> {
                Song[] songs = songService.getLibrarySongs();
                tracks = new TrackItem[songs.length];
                for (int i = 0; i < songs.length; i++) {
                    tracks[i] = new TrackItem(songs[i]);
                }
                recyclerView.setAdapter(new MyTrackRecyclerViewAdapter(context, Arrays.asList(tracks), this));
                recyclerView.setHasFixedSize(true);
            });
        }
        return view;
    }
    
    public TrackItem  getCurrentSelectedTrack() {
        return tracks[this.currentSelectedTrackIndex];
    }
}