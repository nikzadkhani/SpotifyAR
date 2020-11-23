/**
 * ListActivity.java - Provides a RecyclerView that display's the users Spotify library. This is
 * loaded in via com.spotify.ar.services.TrackService. The track the user clicks on in the
 * recycler view will be passed up through the adapter, then the fragment, to this class, then
 * it will be passed as an intent to the next activity, com.spotify.ar.ARActivity.
 * The bottom navbar lets you toggle between this activity and com.spotify.ar.SearchFragment. Both
 * will lead to the ARActivity.
 */

package com.example.spotifyar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.spotifyar.adapters.ListActivityViewAdapter;
import com.example.spotifyar.fragments.LibraryFragment;
import com.example.spotifyar.fragments.LibraryRecyclerFragment;
import com.example.spotifyar.fragments.SearchFragment;
import com.example.spotifyar.fragments.SearchRecyclerFragment;
import com.example.spotifyar.interfaces.SearchFragmentListener;
import com.example.spotifyar.interfaces.TrackFragmentListener;
import com.example.spotifyar.models.Album;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements SearchFragmentListener, TrackFragmentListener {
    private static final String LIBRARY_TAG = "f0";
    private static final String SEARCH_TAG = "f1";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ListActivityViewAdapter listActivityViewAdapter;

    private SearchRecyclerFragment searchRecyclerFragment;
    private LibraryRecyclerFragment libraryRecyclerFragment;

    private SearchFragment searchFragment;
    private LibraryFragment libraryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nav);
        
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        
        listActivityViewAdapter = new ListActivityViewAdapter(this);
        viewPager.setAdapter(listActivityViewAdapter);

        
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.library);
            } else {
                tab.setText(R.string.search);
            }
        }).attach();
    }

    @Override
    public void setLibraryTrackViewText(Track track) {
        getLibraryFragment().setConfirmText(track);
    }

    @Override
    public void loadTrackData(ArrayList<Track> tracks) {
        searchFragment = getSearchFragment();

        FragmentManager fcm = searchFragment.getChildFragmentManager();
        Toast.makeText(getApplicationContext(), fcm.getFragments().toString(), Toast.LENGTH_LONG);
        searchRecyclerFragment = (SearchRecyclerFragment) fcm.findFragmentById(R.id.searchRecyclerFrag);
        searchRecyclerFragment.updateToTrackAdapter(tracks);
    }

    @Override
    public void loadArtistData(ArrayList<Artist> artists) {
        searchFragment = getSearchFragment();

        FragmentManager fcm = searchFragment.getChildFragmentManager();
        Toast.makeText(getApplicationContext(), fcm.getFragments().toString(), Toast.LENGTH_LONG);
        searchRecyclerFragment = (SearchRecyclerFragment) fcm.findFragmentById(R.id.searchRecyclerFrag);
        searchRecyclerFragment.updateToArtistAdapter(artists);
    }

    @Override
    public void loadAlbumData(ArrayList<Album> albums) {
        searchRecyclerFragment.updateToAlbumAdapter(albums);
    }

    @Override
    public void setSearchTrackViewText(Track track) {
        getSearchFragment().setConfirmTrackViewText(track);
    }

    @Override
    public void setArtistViewText(Artist artist) {
        getSearchFragment().setConfirmArtistViewText(artist);
    }

    @Override
    public void setAlbumViewText(Album album) {
        getSearchFragment().setConfirmAlbumViewText(album);
    }

    public LibraryFragment getLibraryFragment() {
        return (LibraryFragment )getSupportFragmentManager().findFragmentByTag(LIBRARY_TAG);
    }

    public SearchFragment getSearchFragment() {
        return (SearchFragment )getSupportFragmentManager().findFragmentByTag(SEARCH_TAG);
    }


}