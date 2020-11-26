package com.example.spotifyar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.spotifyar.R;
import com.example.spotifyar.interfaces.SearchFragmentListener;
import com.example.spotifyar.models.Album;
import com.example.spotifyar.services.SearchService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;




/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchBarFragment} factory method to
 * create an instance of this fragment.
 */
public class SearchBarFragment extends Fragment {

    private Button searchButton;
    private TextInputLayout searchBar;
    private TextInputEditText searchBarText;
    private String[] QUERY_TYPES = new String[] {"Track", "Album", "Artist"};

    private SearchService searchService;
    


    public SearchBarFragment() {
        // Required empty public constructor
    }

    SearchFragmentListener SFL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {  
        super.onAttach(context);
        SFL = (SearchFragmentListener) getParentFragment().getContext();
        searchService = new SearchService(context);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_bar, container, false);


        searchBar = view.findViewById(R.id.searchBar);
        searchBarText = view.findViewById(R.id.searchBarText);
        AutoCompleteTextView queryTypeSelection =  view.findViewById(R.id.spinner);


        //create adapter for the selected spinner item
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.dropdown_menu_popup_item,
                QUERY_TYPES
        );
        //Set the adapter
        queryTypeSelection.setAdapter(adapter);
        queryTypeSelection.setText("Track", false);

        searchBarText.setOnKeyListener(new View.OnKeyListener() {

           //Run when search input is entered
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String input = searchBar.getEditText().getText().toString();
                    String queryType = queryTypeSelection.getText().toString();

                    //Assume we have the correct data, now we have to transfer it to the main activity
                    searchService.getSearchResults(input, queryType, () -> {
                        if(queryType.equals("Track")){
                            ArrayList<Track> tracks = searchService.getTrackArray();
                            SFL.loadTrackData(tracks);
                        }
                        else if(queryType.equals("Artist")){
                            ArrayList<Artist> artists = searchService.getArtistArray();
                            SFL.loadArtistData(artists);
                        }
                        else if(queryType.equals("Album")){
                            ArrayList<Album> albums = searchService.getAlbumArray();
                            SFL.loadAlbumData(albums);
                        }});

                    return true;
                }
                return false;
            }
        });


        searchBar.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    
                    String input = searchBar.getEditText().getText().toString();

                    String queryType = queryTypeSelection.getText().toString();

                    //Assume we have the correct data, now we have to transfer it to the main activity
                    searchService.getSearchResults(input, queryType, () -> {
                        if(queryType.equals("Track")){
                            ArrayList<Track> tracks = searchService.getTrackArray();
                            SFL.loadTrackData(tracks);
                        }
                        else if(queryType.equals("Artist")){
                            ArrayList<Artist> artists = searchService.getArtistArray();
                            SFL.loadArtistData(artists);
                        }
                        else if(queryType.equals("Album")){
                            ArrayList<Album> albums = searchService.getAlbumArray();
                            SFL.loadAlbumData(albums);
                        }


                    });
                    
            }
        });



        return view;
    }


}