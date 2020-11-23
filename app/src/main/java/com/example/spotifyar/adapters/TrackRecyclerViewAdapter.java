/**
 * TrackRecyclerViewAdapter.java - Adapter for recycler view in LibraryRecyclerFragment
 * and also in SearchRecyclerFragment when the user searches for a track. Utilizes OnItemClickLIsten
 */

package com.example.spotifyar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyar.R;
import com.example.spotifyar.interfaces.OnItemClickListener;
import com.spotify.protocol.types.Track;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder> {

    private final Track[] tracks;
    private final Context adapterContext;
    private final OnItemClickListener listener;
    
    private int currentSelection = -1;
    private int previousSelection = -1;
    
    private final int selectedColor;
    private final int normalColor;

    public TrackRecyclerViewAdapter(Context context, Track[] tracks, OnItemClickListener listener) {
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.track = this.tracks[position];
        holder.trackTitleView.setText(holder.track.name);
        holder.trackArtistView.setText(holder.track.artists.get(0).name);
        
        if (previousSelection != -1 && previousSelection == position) 
            holder.constraintLayout.setBackgroundColor(normalColor);
        
        if (currentSelection != -1 && currentSelection == position)
            holder.constraintLayout.setBackgroundColor(selectedColor);


        /* The commented code below has a bug where if the keyboard is brought up it gets funky.*/
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

        holder.constraintLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    holder.constraintLayout.setClickable(false);
                } else {
                    holder.constraintLayout.setClickable(true);
                }
            }
        });

        holder.constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (holder.constraintLayout.isClickable()) {
                    holder.constraintLayout.setClickable(false);
                    return false;
                } else {
                    holder.constraintLayout.setClickable(true);
                    return true;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.tracks.length;
    }
    
    public int getCurrentSelection() {
        return currentSelection;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView trackTitleView;
        public final TextView trackArtistView;
        public final ConstraintLayout constraintLayout;
        public Track track;

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