package com.example.spotifyar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.spotifyar.R;
import com.example.spotifyar.interfaces.SearchFragmentListener;
import com.example.spotifyar.models.Album;
import com.example.spotifyar.services.SearchService;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;




/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchBarFragment} factory method to
 * create an instance of this fragment.
 */
public class SearchBarFragment extends Fragment {
    
    private Spinner spinner;
    private String searchInput;
    private Button searchButton;
    private EditText searchBar;
    private String[] dropDown = {"Album", "Artist", "Track"};

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

        searchButton = view.findViewById(R.id.searchButton);
        searchBar = view.findViewById(R.id.searchBar);
        spinner =  view.findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.dropDown, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    
                    String input = searchBar.getText().toString();
                    String queryType = spinner.getSelectedItem().toString();

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