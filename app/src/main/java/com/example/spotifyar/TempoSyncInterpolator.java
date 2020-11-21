package com.example.spotifyar;

import android.animation.TimeInterpolator;


public class TempoSyncInterpolator implements TimeInterpolator {
    int tempo = 90;

//    public TempoSyncInterpolator(int tempo){
//         return tempo;
//    }

   @Override
   public float getInterpolation(float input) {
       return (float) (input * 0.2);
   }
}
