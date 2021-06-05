package com.example.reproductor.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.databinding.RowMainSongsBinding;

public class SongHomeAdapter extends RecyclerView.Adapter<SongHomeAdapter.ViewHolder> {
    @NonNull
    @Override
    public SongHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowMainSongsBinding songBinding = RowMainSongsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(songBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHomeAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RowMainSongsBinding binding;
        public ViewHolder(RowMainSongsBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
