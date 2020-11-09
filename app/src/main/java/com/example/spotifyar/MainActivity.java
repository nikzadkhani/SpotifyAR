package com.example.spotifyar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MaterialTextView userWelcome;
    private MaterialTextView songView;
    private MaterialTextView artistView;
    private MaterialButton selectSongBtn;

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
        userWelcome = (MaterialTextView) findViewById(R.id.userWelcome);
        songView = (MaterialTextView) findViewById(R.id.lastPlayedSong);
        artistView = (MaterialTextView) findViewById(R.id.artistView);
        selectSongBtn = (MaterialButton) findViewById(R.id.selectSongBtn);

        SharedPreferences sharedPreferences = getSharedPreferences("SPOTIFY", 0);
        String displayName = sharedPreferences.getString("display_name", "User");
        userWelcome.setText("Welcome " + displayName + "!");

        getTracks(); // Updates Song view

        selectSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        // Test PlayerService Below
        playerService = new PlayerService(getApplicationContext());


    }



    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getRecentlyPlayedSongs();
            updateSong();
        });

        songService.getLibraryTracks(() -> {
           librarySongs = songService.getLibrarySongs();

           Toast.makeText(getApplicationContext(), librarySongs[0].getName(), Toast.LENGTH_LONG).show();
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
