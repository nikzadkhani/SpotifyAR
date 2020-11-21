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
import com.example.spotifyar.models.Audio;
import com.example.spotifyar.models.User;
import com.google.gson.Gson;
import com.spotify.protocol.types.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AudioService {
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Audio audio;

    public AudioService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public Audio getAudioFeatures(String uri, final VolleyCallBack callBack) {
        if (uri.equals("") || !uri.contains(":"))
            throw new IllegalArgumentException("Invalid URI in AudioService");

        int idStartIndex = uri.lastIndexOf(":");
        String id = uri.substring(idStartIndex);

        String endpoint = "https://api.spotify.com/v1/audio-features/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    audio = gson.fromJson(response.toString(), Audio.class);
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return audio;
    }
}