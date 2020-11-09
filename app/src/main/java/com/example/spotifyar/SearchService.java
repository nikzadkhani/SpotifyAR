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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchService {
    private static final String ENDPOINT = "https://api.spotify.com/v1/search";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;


    private JSONObject songsToPlay = new JSONObject();

    public SearchService(Context context){
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

//    public getSearchResults(String query, String queryType) {
//        String cleanQuery = query.toLowerCase().replace(" ", "&20");
//        String queryEndpoint = ENDPOINT + "?q=" + cleanQuery + "&type=" + queryType;
//
//        JsonObjectRequest jsonObjectRequest;
//        switch (queryType) {
//            case "track":
//                jsonObjectRequest = new JsonObjectRequest(
//                        Request.Method.GET, queryEndpoint, null,
//                        response -> {
//                            Gson gson = new Gson();
//
//
//                        }, error -> {
//                    // TODO: Handle Error
//                });
//            case "artist":
//                jsonObjectRequest = new JsonObjectRequest(
//                        Request.Method.GET, queryEndpoint, null,
//                        response -> {
//                            Gson gson = new Gson();
//                            JSONArray jsonArray = response.optJSONObject("artists").optJSONArray("items");
//                            for (int n = 0; n < jsonArray.length(); n++) {
//                                try {
//                                    JSONObject object = jsonArray.getJSONObject(n);
//                                    object = object.optJSONObject("track");
//
//                                    Song song = gson.fromJson(object.toString(), Song.class);
////                            Log.d("Inside Song Service", song.getUri());
//                                    recentlyPlayedSongs.add(song);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, error -> {
//                    // TODO: Handle Error
//                });
//            case "album":
//                jsonObjectRequest = new JsonObjectRequest(
//                        Request.Method.GET, queryEndpoint, null,
//                        response -> {
//                            Gson gson = new Gson();
//
//
//                        }, error -> {
//                    // TODO: Handle Error
//                });
//            default:
//                throw new IllegalArgumentException("Invalid queryType in getSearchResults");
//        }
//    }
//
//    public void playQueuedSong() {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ENDPOINT,
//                songsToPlay, response -> {
//            Log.d("Player Service", response.toString());
//        }, error -> {
//            Log.d("Player Service", error.toString());
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String token = sharedPreferences.getString("token", "");
//                String auth = "Bearer " + token;
//                headers.put("Authorization", auth);
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//
//        queue.add(jsonObjectRequest);
//    }

}
