package com.example.spotifyar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.ar.core.ArCoreApk;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String AR_AVAILABLE = "AR is available on this device.";
    private final String AR_UNAVAILABLE = "AR is unavailable on this device";

    private TextView userWelcome;
    private TextView songView;
    private TextView artistView;
    private TextView arAvailabilityView;
    private Button selectSongBtn;

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;
    private Song[] librarySongs;
    private ArrayList<TrackItem> libraryTracks;
    private PlayerService playerService;

    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songService = new SongService(getApplicationContext());
        userWelcome = (TextView) findViewById(R.id.userWelcome);
        songView = (TextView) findViewById(R.id.lastPlayedSong);
        artistView = (TextView) findViewById(R.id.artistView);
        arAvailabilityView = (TextView) findViewById(R.id.arAvailabilityView);
        selectSongBtn = (Button) findViewById(R.id.selectSongBtn);

        SharedPreferences sharedPreferences = getSharedPreferences("SPOTIFY", 0);
        String displayName = sharedPreferences.getString("display_name", "User");
        displayName="tung.truong";
        userWelcome.setText("Welcome " + displayName + "!");

        getTracks(); // Updates Song view

        selectSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        // Test to see if AR is supported on device
        setArAvailabilityView();
    }

    /*
     * This is from https://developers.google.com/ar/develop/java/enable-arcore
     * I think what it is doing is making a network request and then waiting for the
     * request return before calling the function again? I changed it to change the
     * Text for a textview instead.
     */
    private void setArAvailabilityView() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);

        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setArAvailabilityView();
                }
            }, 200);
        }
        if (availability.isSupported()) {
            arAvailabilityView.setText(AR_AVAILABLE);
        } else { // Unsupported or unknown.
            arAvailabilityView.setText(AR_UNAVAILABLE);
        }


    }

    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getRecentlyPlayedSongs();
            updateSong();
        });

        songService.getLibraryTracks(() -> {
           librarySongs = songService.getLibrarySongs();

//           Toast.makeText(getApplicationContext(), librarySongs[0].getName(), Toast.LENGTH_LONG).show();
//           playerService.addSongToPlaybackQueue(librarySongs.get(0));
//           playerService.playQueuedSong();
//            Log.d("ListActivity", libraryTracks.get(0).toString());

        });

    }

    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            Song mostRecentTrack = recentlyPlayedTracks.get(0);
            String songName = mostRecentTrack.getName();
            String artistName = mostRecentTrack.getFirstArtistName();
            songView.setText(songName);
            artistView.setText(artistName);
            song = recentlyPlayedTracks.get(0);
        }
    }
}