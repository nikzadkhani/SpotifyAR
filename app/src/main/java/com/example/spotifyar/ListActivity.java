/**
 * ListActivity.java - Provides a RecyclerView that display's the users Spotify library. This is
 * loaded in via com.spotify.ar.services.TrackService. The track the user clicks on in the
 * recycler view will be passed up through the adapter, then the fragment, to this class, then
 * it will be passed as an intent to the next activity, com.spotify.ar.ARActivity.
 * The bottom navbar lets you toggle between this activity and com.spotify.ar.SearchActivity. Both
 * will lead to the ARActivity.
 */

package com.example.spotifyar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifyar.fragments.LibraryRecyclerFragment;
import com.example.spotifyar.interfaces.TrackFragmentListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.protocol.types.Track;

public class ListActivity extends AppCompatActivity implements TrackFragmentListener {
    private Button danceBtn;
    private TextView trackConfirmView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        LibraryRecyclerFragment libraryRecyclerFragment = (LibraryRecyclerFragment) getSupportFragmentManager().findFragmentById(R.id.libraryRecyclerFragment);

        danceBtn = (Button) findViewById(R.id.startArBtn);
        trackConfirmView = (TextView) findViewById(R.id.confirmTrackView);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenuLibrary);

        trackConfirmView.setText("");


        danceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track currentTrack = libraryRecyclerFragment.getCurrentSelectedTrack();
                Intent intent = new Intent(ListActivity.this, ARActivity.class);
                intent.putExtra("selectedTrack", currentTrack.uri);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.search:
                                Intent intent = new Intent(ListActivity.this, SearchActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public void setTrackViewText(Track track) {
//        Toast.makeText(this, "Hi", Toast.LENGTH_LONG).show();
        trackConfirmView.setText(track.name + " by " + track.artists.get(0).name);
//        trackConfirmView.setText(track);
    }
}