package com.example.spotifyar;

public class Audio {
    @Override
    public String toString() {
        return "Audio{" +
                "tempo=" + tempo +
                '}';
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getTempo() {
        return tempo;
    }

    public Audio(int tempo) {
        this.tempo = tempo;
    }

    private int tempo;
    
}
