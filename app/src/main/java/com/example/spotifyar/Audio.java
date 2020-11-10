package com.example.spotifyar;

public class Audio {
    private float tempo;

    public Audio(int tempo) {
        this.tempo = tempo;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "tempo=" + tempo +
                '}';
    }
}
