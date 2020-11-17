package com.example.spotifyar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.protocol.types.Album;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchBarFragment.ControlFragmentListener {
    private Button danceBtn;
    private TextView songConfirmView;
    SearchResultListFragment SearchResultFrag;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenuSearch);
        danceBtn = (Button) findViewById(R.id.startArBtn2);
        songConfirmView = (TextView) findViewById(R.id.confirmSongView2);

        SearchResultFrag = (SearchResultListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_result_list);

        songConfirmView.setText("");
        danceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case (R.id.library):
                                Intent intent = new Intent(SearchActivity.this, ListActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return false;
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