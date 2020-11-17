package com.example.spotifyar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TrackItem}.
 */
public class MyTrackRecyclerViewAdapter extends RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder> {

    private final List<TrackItem> tracks;
    private final Context adapterContext;
    private final OnItemClickListener listener;
    
    private int currentSelection = -1;
    private int previousSelection = -1;
    
    private final int selectedColor;
    private final int normalColor;

    public MyTrackRecyclerViewAdapter(Context context, List<TrackItem> tracks, OnItemClickListener listener) {
        this.tracks = tracks;
        this.adapterContext = context;
        this.listener = listener;

        this.selectedColor = ContextCompat.getColor(adapterContext, R.color.colorSecondary);
        this.normalColor = ContextCompat.getColor(adapterContext, R.color.colorBackground);
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
        holder.trackTitleView.setText(this.tracks.get(position).name);
        holder.trackArtistView.setText(this.tracks.get(position).artist);
        
        if (previousSelection != -1 && previousSelection == position) 
            holder.constraintLayout.setBackgroundColor(normalColor);
        
        if (currentSelection != -1 && currentSelection == position)
            holder.constraintLayout.setBackgroundColor(selectedColor);
        
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSelection = currentSelection;
                currentSelection = position;
                
                notifyItemChanged(currentSelection);
                notifyItemChanged(previousSelection);

                listener.onTrackItemClicked(currentSelection);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tracks.size();
    }
    
    public int getCurrentSelection() {
        return currentSelection;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView trackTitleView;
        public final TextView trackArtistView;
        public final ConstraintLayout constraintLayout;
        public TrackItem track;

        public ViewHolder(View view) {
            super(view);
            trackTitleView = (TextView) view.findViewById(R.id.trackTitle);
            trackArtistView = (TextView) view.findViewById(R.id.trackArtist);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.track_item_layout);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    ", trackTitleView=" + trackTitleView.getText() +
                    ", trackArtistView=" + trackArtistView.getText() +
                    ", track=" + track.toString() +
                    '}';
        }
    }
}