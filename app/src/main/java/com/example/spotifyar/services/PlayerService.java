package com.example.spotifyar.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.spotify.protocol.types.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to play selected songs.
 */
public class PlayerService {
    private static final String ENDPOINT = "https://api.spotify.com/v1/me/player/play";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;


    private JSONObject songsToPlay = new JSONObject();

    public PlayerService(Context context){
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public void addSongToPlaybackQueue(Track track){
        JSONArray uriArray = new JSONArray();
        uriArray.put(track.uri);
        try {
            songsToPlay.put("uris", uriArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addSongToPlaybackQueue(String uri) {
        JSONArray uriArray = new JSONArray();
        uriArray.put(uri);
        try {
            songsToPlay.put("uris", uriArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void playQueuedSong() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ENDPOINT,
                songsToPlay, response -> {
                    Log.d("Player Service", response.toString());
                }, error -> {
                    Log.d("Player Service", error.toString());
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

        queue.add(jsonObjectRequest);
    }

}
