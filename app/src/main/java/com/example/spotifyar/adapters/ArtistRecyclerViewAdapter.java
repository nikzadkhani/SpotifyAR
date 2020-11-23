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
import com.spotify.protocol.types.Artist;

import java.util.ArrayList;


public class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Artist> artists;
    private final Context adapterContext;
    private final SearchFragmentListener SFL;

    private int currentSelection = -1;

    private final int selectedColor;
    private final int normalColor;

    public ArtistRecyclerViewAdapter(Context context, ArrayList<Artist> artists, SearchFragmentListener SFL) {
        this.artists = artists;
        this.adapterContext = context;

        this.SFL = SFL;

        this.selectedColor = ContextCompat.getColor(adapterContext, R.color.colorSecondary);
        this.normalColor = ContextCompat.getColor(adapterContext, R.color.colorBackground);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.artist = this.artists.get(position);
        holder.artistNameView.setText(holder.artist.name);

        if (currentSelection != -1 && currentSelection == position)
            holder.constraintLayout.setBackgroundColor(selectedColor);
        else
            holder.constraintLayout.setBackgroundColor(normalColor);


        /* The commented code below has a bug where if the keyboard is brought up it gets funky.*/
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = position;

                notifyItemChanged(currentSelection);

                SFL.setArtistViewText(artists.get(position));
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
    }

    @Override
    public int getItemCount() {
        return this.artists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView artistNameView;
        public final ConstraintLayout constraintLayout;
        public Artist artist;

        public ViewHolder(View view) {
            super(view);
            artistNameView = (TextView) view.findViewById(R.id.artistName);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.artist_item_layout);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    ", artistNameView=" + artistNameView.getText() +
                    ", artist=" + artist.toString() +
                    '}';
        }
    }
}