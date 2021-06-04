package com.example.reproductor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.databinding.RowRecentActivityBinding;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ViewHolder> {

    public RecentActivityAdapter() {
    }

    @NonNull
    @Override
    public RecentActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRecentActivityBinding binding = RowRecentActivityBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentActivityAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowRecentActivityBinding binding;
        public ViewHolder(RowRecentActivityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
