package com.example.spotifyar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    List<TrackItem> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        Bundle bundle = getIntent().getExtras();
//        tracks = (ArrayList<TrackItem>) bundle.get("libraryTracks");
    }

}