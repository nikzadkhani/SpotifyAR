/**
 * MainActivity.java - Opened after SplashActivity Auth. Serves as a homepage to ensure
 * Spotify API is working. Adds a TextView displaying if AR is available for the user's device.
 * Greets user then provides a button called select a track to the next activity
 * com.example.spotify.ar.ListActivity
 */

package com.example.spotifyar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifyar.services.PlayerService;
import com.example.spotifyar.services.TrackService;
import com.google.ar.core.ArCoreApk;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String AR_AVAILABLE = "AR is available on this device.";
    private final String AR_UNAVAILABLE = "AR is unavailable on this device";

    private TextView userWelcome;
    private TextView trackView;
    private TextView artistView;
    private TextView arAvailabilityView;
    private Button selectTrackBtn;

    private TrackService trackService;
    private ArrayList<Track> recentlyPlayedTracks;
    private SharedPreferences sharedPreferences;

    private Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackService = new TrackService(getApplicationContext());
        userWelcome = (TextView) findViewById(R.id.userWelcome);
        trackView = (TextView) findViewById(R.id.lastPlayedTrack);
        artistView = (TextView) findViewById(R.id.artistView);
        arAvailabilityView = (TextView) findViewById(R.id.arAvailabilityView);
        selectTrackBtn = (Button) findViewById(R.id.selectTrackBtn);

        sharedPreferences = getSharedPreferences("SPOTIFY", 0);
        String displayName = sharedPreferences.getString("display_name", "User");
        userWelcome.setText("Welcome " + displayName + "!");

        getTracks(); // Updates Track view

        selectTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        // Test to see if AR is supported on device
        setArAvailabilityView();
    }

    @Override
    public void onBackPressed() {
        if (sharedPreferences.getString("token", "epic").equals("epic")) {
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
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
        trackService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = trackService.getRecentlyPlayedTracks();
            updateTrack();
        });
    }

    private void updateTrack() {
        if (recentlyPlayedTracks.size() > 0) {
            Track mostRecentTrack = recentlyPlayedTracks.get(0);
            String trackName = mostRecentTrack.name;
            String artistName = mostRecentTrack.artists.get(0).name;
            trackView.setText(trackName);
            artistView.setText(artistName);
            track = recentlyPlayedTracks.get(0);
        }
    }
}