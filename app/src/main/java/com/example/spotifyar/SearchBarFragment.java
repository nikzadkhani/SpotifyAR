package com.example.spotifyar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.spotify.protocol.types.Album;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBarFragment extends Fragment {

 private static final String ARG_PARAM1 = "input";
private Spinner spinner;
private String searchInput;
private Button searchButton;
private EditText searchBar;
private String[] dropDown = {"Album", "Artist", "Track"};


    public SearchBarFragment() {
        // Required empty public constructor
    }


    //Needs 3 different methods for 3 different data type TRYING TO OVERLOAD BUT IT WONT LET ME!!
    public interface ControlFragmentListener {            //this is just an interface definition.
        public void loadTrackData(ArrayList<Song> tracks); //it could live in its own file.  placed here for convenience.
        public void loadArtistData(ArrayList<Artist> artists);
        public void loadAlbumData(ArrayList<Album> albums);
    }

    ControlFragmentListener CFL;

    public static SearchBarFragment newInstance(String input) {
        SearchBarFragment fragment = new SearchBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, input);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            searchInput = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onAttach(Context context) {   //The onAttach method, binds the fragment to the owner.  Fragments are hosted by Activities, therefore, context refers to: ____________?
        super.onAttach(context);
        CFL = (ControlFragmentListener) context;  //context is a handle to the main activity, let's bind it to our interface.
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


/*        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getRecentlyPlayedSongs();
            Log.v("TrackISHERE", recentlyPlayedTracks.get(0).toString());
            updateSong();
        });

*/
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SearchService searchService = new SearchService(getContext());
                    String input = searchBar.getText().toString();
                    String queryType = spinner.getSelectedItem().toString();

                    //Passes in data into Search Activity
                    searchService.getSearchResults(input, queryType, () -> {
                        if(queryType.equals("Track")){
                            ArrayList<Song> tracks = searchService.getSongArray();
                            CFL.loadTrackData(tracks);
                        }
                        else if(queryType.equals("Artist")){
                            ArrayList<Artist> artists = searchService.getArtistArray();
                            CFL.loadArtistData(artists);
                        }
                        else if(queryType.equals("Album")){
                            ArrayList<Album> albums = searchService.getAlbumArray();
                            CFL.loadAlbumData(albums);
                        }


                    });
                    
            }
        });

        return view;
    }


}