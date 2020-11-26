package com.example.spotifyar.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifyar.interfaces.VolleyCallBack;
import com.example.spotifyar.models.Audio;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AudioService {
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Audio audio;


    // Create JSonObjectRequest and add to volley request queue
    // Request Queue will execute the JSONObjectRequest for us
    public AudioService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }







    public Audio getCurrentAudio(){
        return audio;
    }


    public Audio getAudioFeatures(String uri, final VolleyCallBack callBack) {
        //Ensuring URI follows Spotify Code guidelines 
        if (uri.equals("") || !uri.contains(":"))
            throw new IllegalArgumentException("Invalid URI in AudioService");

        int idStartIndex = uri.lastIndexOf(":")+1;

        String id = uri.substring(idStartIndex);

        String endpoint = "https://api.spotify.com/v1/audio-features/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    audio = gson.fromJson(response.toString(), Audio.class); // Put our json info into an Audio class object
                    callBack.onSuccess(); // use our volley callback
                }, error -> {
                    // TODO: Handle error

                }) {

            // Add our authorization token to the jsonObjectRequst

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