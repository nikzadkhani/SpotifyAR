package com.example.spotifyar;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

public class TempoService {
    private RequestQueue queue;
    private SharedPreferences sharedPreferences;
    private float tempo;


    public float getTempo(String id, final VolleyCallBack callBack) { //id of the  song
        String endpoint = "https://api.spotify.com/v1/audio-features/" + id;   //"https://api.spotify.com/v1/audio-analysis/{id}";    //extra input of track id needed   //"https://api.spotify.com/v1/me/tracks";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    Audio audio = gson.fromJson(response.toString(), Audio.class); //at this point I should have the tempo of the specific song
                    tempo = audio.getTempo(); //do i need to store this information in a queue like the other class
                    //librarySongs[n] = song;

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
        queue.add(jsonObjectRequest); //is this needed?
        return tempo; //librarySongs;
    }


}
