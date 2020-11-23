package com.example.spotifyar.models;

public class Audio {
    private float tempo;
    private int duration_ms;

    public Audio(float tempo, int duration_ms) {
        this.tempo = tempo;
        this.duration_ms = duration_ms;
    }

    public float getTempo() {
        return tempo;
    }

    public int getDuration() {
        return duration_ms;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "tempo=" + tempo +
                '}';
    }


}
