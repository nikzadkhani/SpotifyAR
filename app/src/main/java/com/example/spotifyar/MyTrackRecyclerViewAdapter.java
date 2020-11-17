package com.example.spotifyar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TrackItem}.
 */
public class MyTrackRecyclerViewAdapter extends RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder> {

    private final List<TrackItem> tracks;

    public MyTrackRecyclerViewAdapter(List<TrackItem> tracks) {
        this.tracks = tracks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_item, parent, false);
        Log.v("Recycler Adapter", String.valueOf(viewType));
        return new ViewHolder(view);
    }
        
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.track = this.tracks.get(position);
        holder.idView.setText(String.valueOf(position));
        holder.trackTitleView.setText(this.tracks.get(position).name);
        holder.trackArtistView.setText(this.tracks.get(position).artist);

    }

    @Override
    public int getItemCount() {
        return this.tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView idView;
        public final TextView trackTitleView;
        public final TextView trackArtistView;
        public final TextView confirmSongView;
        public TrackItem track;

        public ViewHolder(View view) {
            super(view);
            idView = (TextView) view.findViewById(R.id.itemNumber);
            trackTitleView = (TextView) view.findViewById(R.id.trackTitle);
            trackArtistView = (TextView) view.findViewById(R.id.trackArtist);
            confirmSongView = (TextView) view.findViewById(R.id.confirmSongView);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int trackIndex = Integer.parseInt(idView.getText().toString());
                    String trackName = trackTitleView.getText().toString();
                    String trackArtist = trackArtistView.getText().toString();
                    String s = trackIndex+".\t"+trackName+" by "+trackArtist;
                    confirmSongView.setText(s);
                }
            });
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "idView=" + idView.getText() +
                    ", trackTitleView=" + trackTitleView.getText() +
                    ", trackArtistView=" + trackArtistView.getText() +
                    ", track=" + track.toString() +
                    '}';
        }
    }
}