package com.example.spotifyar.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spotifyar.fragments.LibraryFragment;
import com.example.spotifyar.fragments.SearchFragment;

public class ListActivityViewAdapter extends FragmentStateAdapter {


    public ListActivityViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LibraryFragment();
        } else {
            return new SearchFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
