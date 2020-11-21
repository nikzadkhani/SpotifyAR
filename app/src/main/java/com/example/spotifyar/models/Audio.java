package com.example.spotifyar.models;

public class Audio {
    private int tempo;

    public Audio(int tempo) {
        this.tempo = tempo;
    }

    public int getTempo() {
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
