package com.example.spotifyar.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifyar.interfaces.VolleyCallBack;
import com.example.spotifyar.models.Album;
import com.google.gson.Gson;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Track;

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


    private JSONObject tracksToPlay = new JSONObject();
    private ArrayList<Track> trackArray = new ArrayList<>();
    private ArrayList<Artist> artistArray = new ArrayList<>();
    private ArrayList<Album> albumArray = new ArrayList<>();

    // Creqte JSonObjectRequest and add to volley request queue
    // Request Queue will execute the JSonObjectRequest for us 


    public SearchService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Track> getTrackArray() {
        return trackArray;
    }


    public ArrayList<Artist> getArtistArray() {
        return artistArray;
    }


    public ArrayList<Album> getAlbumArray() {
        return albumArray;
    }


    public void getSearchResults(String query, String queryType, final VolleyCallBack callBack) {
        queryType = queryType.toLowerCase();
        String cleanQuery = query.toLowerCase().replace(" ", "&20");
        String queryEndpoint = ENDPOINT + "?q=" + cleanQuery + "&type=" + queryType;

        JsonObjectRequest jsonObjectRequest;
        if (queryType.equals("track")) {
            trackArray.clear();
            jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, queryEndpoint, null,
                    response -> {
                        Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONObject("tracks").optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                Track track = gson.fromJson(object.toString(), Track.class); // // Put our json info into a searchService class object
                                trackArray.add(track);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccess(); // use our volley callback 
                    }, error -> {
                // TODO: Handle Error
            }) {

                // Add our authorization token to the jsonObjectRequest
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String token = sharedPreferences.getString("token", "");
                    Log.v("SHARED PREF TOKEN", token);
                    String auth = "Bearer " + token;
                    headers.put("Authorization", auth);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
        } else if (queryType.equals("artist")) {
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String token = sharedPreferences.getString("token", "");
                    String auth = "Bearer " + token;
                    headers.put("Authorization", auth);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
        } else if (queryType.equals("album")) {
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String token = sharedPreferences.getString("token", "");
                    String auth = "Bearer " + token;
                    headers.put("Authorization", auth);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
        } else {
            Log.v("QUERYTYPE", queryType);
            jsonObjectRequest = null;
        }

        if (jsonObjectRequest == null)
            throw new IllegalArgumentException("Invalid queryType in getSearchResults");
        Log.v("QUERYTYPE", jsonObjectRequest.toString());
        queue.add(jsonObjectRequest);
    }
}