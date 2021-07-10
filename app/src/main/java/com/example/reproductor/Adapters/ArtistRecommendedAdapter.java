package com.example.reproductor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.databinding.RowMainArtistBinding;

public class ArtistRecommendedAdapter extends RecyclerView.Adapter<ArtistRecommendedAdapter.ViewHolder> {

    public ArtistRecommendedAdapter() {
    }

    @NonNull
    @Override
    public ArtistRecommendedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowMainArtistBinding binding = RowMainArtistBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistRecommendedAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowMainArtistBinding binding;
        public ViewHolder(RowMainArtistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
