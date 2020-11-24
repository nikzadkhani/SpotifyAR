package com.example.spotifyar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyar.R;
import com.example.spotifyar.adapters.AlbumRecyclerViewAdapter;
import com.example.spotifyar.adapters.ArtistRecyclerViewAdapter;
import com.example.spotifyar.adapters.TrackRecyclerViewAdapter;
import com.example.spotifyar.interfaces.SearchFragmentListener;
import com.example.spotifyar.interfaces.TrackFragmentListener;
import com.example.spotifyar.models.Album;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class SearchRecyclerFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

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
    
    //Make 3 different adapts
    public void updateToTrackAdapter(ArrayList<Track> adapterContent) {
        Log.v("updateToTrackAdapter", adapterContent.toString());
        //If both pages are changing or only one is changing, check this line
        Track[] tracks = new Track[adapterContent.size()];
        for (int i = 0; i < adapterContent.size(); i++) {
            tracks[i] = adapterContent.get(i);
        }
        recyclerView.setAdapter(new TrackRecyclerViewAdapter(context, tracks, (SearchFragmentListener) getContext()));
    }

    public void updateToAlbumAdapter(ArrayList<Album> albums) {
        recyclerView.setAdapter(new AlbumRecyclerViewAdapter(context, albums, (SearchFragmentListener) getContext()));
    }

    public void updateToArtistAdapter(ArrayList<Artist> artists) {
        recyclerView.setAdapter(new ArtistRecyclerViewAdapter(context, artists, (SearchFragmentListener) getContext()));
    }
}