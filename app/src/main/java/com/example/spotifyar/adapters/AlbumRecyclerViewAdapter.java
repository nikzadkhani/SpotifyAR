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
import com.example.spotifyar.models.Album;

import java.util.ArrayList;

/**/
public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Album> albums;
    private final Context adapterContext;
    private final SearchFragmentListener SFL;

    private int currentSelection = -1;
    private int previousSelection = -1;

    private final int selectedColor;
    private final int normalColor;

    public AlbumRecyclerViewAdapter(Context context, ArrayList<Album> albums, SearchFragmentListener SFL) {
        this.albums = albums;
        this.adapterContext = context;

        this.SFL = SFL;

        this.selectedColor = ContextCompat.getColor(adapterContext, R.color.colorSecondary);
        this.normalColor = ContextCompat.getColor(adapterContext, R.color.colorBackground);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.album = this.albums.get(position);
        holder.albumTitleView.setText(holder.album.getArtists()[0].name); // first artist name
        holder.albumArtistView.setText(holder.album.getName()); // album name


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

                SFL.setAlbumViewText(albums.get(position));
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
        return this.albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView albumTitleView;
        public final TextView albumArtistView;
        public final ConstraintLayout constraintLayout;
        public Album album;

        public ViewHolder(View view) {
            super(view);
            albumTitleView = (TextView) view.findViewById(R.id.albumTitle);
            albumArtistView = (TextView) view.findViewById(R.id.albumArtist);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.album_item_layout);
        }
    }
}
