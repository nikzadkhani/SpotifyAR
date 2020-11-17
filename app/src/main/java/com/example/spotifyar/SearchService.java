package com.example.spotifyar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.spotify.protocol.types.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchService {
    private static final String ENDPOINT = "https://api.spotify.com/v1/search";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;


    private JSONObject songsToPlay = new JSONObject();
    private ArrayList<Song> songArray = new ArrayList<>();
    private ArrayList<Artist> artistArray = new ArrayList<>();
    private ArrayList<Album> albumArray = new ArrayList<>();



    public SearchService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }





    public ArrayList<Song> getSongArray() {
        return songArray;
    }


    public ArrayList<Artist> getArtistArray() {
        return artistArray;
    }


    public ArrayList<Album> getAlbumArray() {
        return albumArray;
    }


    public void getSearchResults(String query, String queryType, final VolleyCallBack callBack) {
        String cleanQuery = query.toLowerCase().replace(" ", "&20");
        String queryEndpoint = ENDPOINT + "?q=" + cleanQuery + "&type=" + queryType;

        JsonObjectRequest jsonObjectRequest;
        switch (queryType) {
            case "track":
                songArray.clear();
                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET, queryEndpoint, null,
                        response -> {
                            Gson gson = new Gson();
                            JSONArray jsonArray = response.optJSONObject("tracks").optJSONArray("items");
                            for (int n = 0; n < jsonArray.length(); n++) {
                                try {
                                    JSONObject object = jsonArray.getJSONObject(n);
                                    Song song = gson.fromJson(object.toString(), Song.class);
                                    songArray.add(song);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            callBack.onSuccess();
                        }, error -> {
                    // TODO: Handle Error
                });
            case "artist":
                artistArray.clear();
                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET, queryEndpoint, null,
                        response -> {
                            Gson gson = new Gson();
                            JSONArray jsonArray = response.optJSONObject("artists").optJSONArray("items");
                            for (int n = 0; n < jsonArray.length(); n++) {
                                try {
                                    JSONObject object = jsonArray.getJSONObject(n);
                                    Artist artist = gson.fromJson(object.toString(), Artist.class);
                                    artistArray.add(artist);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            callBack.onSuccess();
                        }, error -> {
                    // TODO: Handle Error
                });
            case "album":
                albumArray.clear();
                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET, queryEndpoint, null,
                        response -> {
                            Gson gson = new Gson();
                            JSONArray jsonArray = response.optJSONObject("albums").optJSONArray("items");
                            for (int n = 0; n < jsonArray.length(); n++) {
                                try {
                                    JSONObject object = jsonArray.getJSONObject(n);
                                    Album album = gson.fromJson(object.toString(), Album.class);
                                    albumArray.add(album);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            callBack.onSuccess();
                        }, error -> {
                    // TODO: Handle Error
                });
            default:
                jsonObjectRequest = null;
        }

        if (jsonObjectRequest == null)
            throw new IllegalArgumentException("Invalid queryType in getSearchResults");

        queue.add(jsonObjectRequest);
    }
}