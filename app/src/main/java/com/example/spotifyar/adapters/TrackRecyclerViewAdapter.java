/**
 * TrackRecyclerViewAdapter.java - Adapter for recycler view in LibraryRecyclerFragment
 * and also in SearchRecyclerFragment when the user searches for a track. Utilizes OnItemClickLIsten
 */

package com.example.spotifyar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyar.R;
import com.example.spotifyar.interfaces.SearchFragmentListener;
import com.example.spotifyar.interfaces.TrackFragmentListener;
import com.spotify.protocol.types.Track;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder> {

    private final Track[] tracks;
    private final Context adapterContext;
    private TrackFragmentListener TFL = null;
    private SearchFragmentListener SFL = null;
    
    private int currentSelection = -1;
    private int previousSelection = -1;
    
    private final int selectedColor;
    private final int normalColor;

    private boolean isLibrary;

    public TrackRecyclerViewAdapter(Context context, Track[] tracks, TrackFragmentListener TFL) {
        this.tracks = tracks;
        this.adapterContext = context;
        this.TFL = TFL;

        this.selectedColor = ContextCompat.getColor(adapterContext, R.color.colorSecondary);
        this.normalColor = ContextCompat.getColor(adapterContext, R.color.colorBackground);

        this.isLibrary = true; // means we are being called from LibraryFragment
    }

    public TrackRecyclerViewAdapter(Context context, Track[] tracks, SearchFragmentListener SFL) {
        this.tracks = tracks;
        this.adapterContext = context;
        this.SFL = SFL;

        this.selectedColor = ContextCompat.getColor(adapterContext, R.color.colorSecondary);
        this.normalColor = ContextCompat.getColor(adapterContext, R.color.colorBackground);

        this.isLibrary = false;
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

        if (currentSelection != -1 && currentSelection == position)
            holder.constraintLayout.setBackgroundColor(selectedColor);
        else
            holder.constraintLayout.setBackgroundColor(normalColor);
        
        /* The commented code below has a bug where if the keyboard is brought up it gets funky.*/
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSelection = currentSelection;
                currentSelection = position;

                notifyItemChanged(previousSelection);
                notifyItemChanged(currentSelection);

                if (isLibrary) {
                    TFL.setLibraryTrackViewText(tracks[currentSelection]);
                } else {
                    SFL.setSearchTrackViewText(tracks[currentSelection]);
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