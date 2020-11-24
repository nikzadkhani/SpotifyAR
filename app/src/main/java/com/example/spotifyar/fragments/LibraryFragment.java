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
import com.example.spotifyar.interfaces.TrackFragmentListener;
import com.spotify.protocol.types.Track;

public class LibraryFragment extends Fragment {
    private TextView trackConfirmView;
    private Button danceBtn;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        trackConfirmView = (TextView) view.findViewById(R.id.confirmTrackView);
        danceBtn = (Button) view.findViewById(R.id.startArBtn);

        trackConfirmView.setText("");

        danceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrackFragmentListener) getContext()).danceBtnOnClickLibrary();
            }
        });

        return view;
    }

    public void setConfirmText(Track track) {
        trackConfirmView.setText(track.name + " by " + track.artists.get(0).name);
    }
}