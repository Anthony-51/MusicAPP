package com.example.reproductor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.databinding.RowArtistsBinding;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    @NonNull
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowArtistsBinding binding = RowArtistsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ViewHolder holder, int position) {
        if (position % 2 != 0){
            holder.binding.firstImage.setVisibility(View.GONE);
            holder.binding.secondImage.setVisibility(View.VISIBLE);
        }else{
            holder.binding.firstImage.setVisibility(View.VISIBLE);
            holder.binding.secondImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowArtistsBinding binding;
        public ViewHolder(RowArtistsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
