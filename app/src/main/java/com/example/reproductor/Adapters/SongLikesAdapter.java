package com.example.reproductor.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.databinding.RowSongLikesBinding;

public class SongLikesAdapter extends RecyclerView.Adapter<SongLikesAdapter.ViewHolder> {

    public SongLikesAdapter() {
    }

    @NonNull
    @Override
    public SongLikesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowSongLikesBinding binding = RowSongLikesBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongLikesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowSongLikesBinding vBinding;
        public ViewHolder(RowSongLikesBinding binding) {
            super(binding.getRoot());
            this.vBinding = binding;
        }
    }
}
