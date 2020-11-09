package com.example.spotifyar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private Button danceBtn;
    private TextView songConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TrackFragment trackFragment = (TrackFragment) getSupportFragmentManager().findFragmentById(R.id.trackFragment);

        danceBtn = (Button) findViewById(R.id.startArBtn);
        songConfirmView = (TextView) findViewById(R.id.confirmSongView);


        danceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Really awful way of figuring out which song the user chose
                // Check adapter ViewHolder onclick for this works.
                String trackIndexString = songConfirmView.getText().toString().split("\\.")[0];
                int trackIndex = Integer.parseInt(trackIndexString);
                String chosenTrackUri = trackFragment.getTrackAtIndex(trackIndex).uri;

            }
        });
    }

}