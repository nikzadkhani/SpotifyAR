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


import java.util.ArrayList;

import com.example.spotifyar.R;
import com.spotify.protocol.types.Track;

/**
 * A fragment representing a list of Items.
 */
public class SearchRecyclerFragment extends Fragment implements OnItemClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private Track[] tracks;

    private RecyclerView recyclerView;
    private int currentSelectedTrackIndex;

    Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchRecyclerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchRecyclerFragment newInstance(int columnCount) {
        SearchRecyclerFragment fragment = new SearchRecyclerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_item_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

        public Track getTrackAtIndex (int index){
            return tracks[index];
        }

        //Make 3 different adapts
        public void updateTrackAdapter(ArrayList<Track> adapterContent) {
            //If both pages are changing or only one is changing, check this line
            Track[] tracks = new Track[adapterContent.size()];
            for (int i = 0; i < adapterContent.size(); i++) {
                tracks[i] = adapterContent.get(i);
            }
            recyclerView.setAdapter(new TrackRecyclerViewAdapter(context, tracks, this));;
        }


}