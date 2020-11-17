package com.example.spotifyar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.protocol.types.Album;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchBarFragment.ControlFragmentListener {
    private Button danceBtn;
    private TextView songConfirmView;
    SearchResultListFragment SearchResultFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);


        danceBtn = (Button) findViewById(R.id.startArBtn2);
        songConfirmView = (TextView) findViewById(R.id.confirmSongView2);

        SearchResultFrag = (SearchResultListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_result_list);

        danceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Really awful way of figuring out which song the user chose
                // Check adapter ViewHolder onclick for this works.
                String trackIndexString = songConfirmView.getText().toString().split("\\.")[0];
                int trackIndex = Integer.parseInt(trackIndexString);
                String chosenTrackUri = SearchResultFrag.getTrackAtIndex(trackIndex).uri;
            }
        });
    }


    @Override
    public void loadTrackData(ArrayList<Song> tracks) {
       //Send the data into the top fragment (Send the data into Search
        //Pass the list into the adapter
        SearchResultFrag.updateTrackAdapter(tracks);
    }

    @Override
    public void loadArtistData(ArrayList<Artist> artists) {
      //  SearchResultFrag.updateArtistAdapter(tracks);

    }

    @Override
    public void loadAlbumData(ArrayList<Album> albums) {

    }
}