package com.example.spotifyar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListActivity extends AppCompatActivity implements TrackFragmentListener {
    private Button danceBtn;
    private TextView songConfirmView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TrackFragment trackFragment = (TrackFragment) getSupportFragmentManager().findFragmentById(R.id.trackFragment);

        danceBtn = (Button) findViewById(R.id.startArBtn);
        songConfirmView = (TextView) findViewById(R.id.confirmSongView);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenuLibrary);

        songConfirmView.setText("");


        danceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackItem currentTrack = trackFragment.getCurrentSelectedTrack();
                Intent intent = new Intent(ListActivity.this, ARActivity.class);
                intent.putExtra("selectedTrack", currentTrack);
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
    public void setSongViewText(TrackItem track) {
//        Toast.makeText(this, "Hi", Toast.LENGTH_LONG).show();
        songConfirmView.setText(track.name + " by " + track.artist);
//        songConfirmView.setText(track);
    }
}