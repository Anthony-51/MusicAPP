package com.example.reproductor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowRecentActivityBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>{

    private ArrayList<HashMap<String, Object>> cancionesRecientes;
    private final ServicesFirebase servicesFirebase;
    private Context context;
    private final OnNoteListener onNoteListener;

    public RecentActivityAdapter(ArrayList<HashMap<String, Object>> cancionesRecientes, OnNoteListener onNoteListener) {
        this.cancionesRecientes = cancionesRecientes;
        this.servicesFirebase = new ServicesFirebase();
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public RecentActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowRecentActivityBinding binding = RowRecentActivityBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentActivityAdapter.ViewHolder holder, int position) {
        if(cancionesRecientes != null){
            String cancion = cancionesRecientes.get(position).get("Cancion").toString();
            String artista = cancionesRecientes.get(position).get("Artista").toString();
            Object img = servicesFirebase.getImage(artista, cancion);
            Glide.with(context).load(img)
                    .into(holder
                            .binding.imgArtist);
            holder.binding.nameArtist.setText(artista);
            holder.binding.nameSong.setText(cancion);
        }

    }

    @Override
    public int getItemCount() {
        if(cancionesRecientes != null){
            return cancionesRecientes.size();
        }else{
            return 9;
        }
    }

    public ArrayList<HashMap<String, Object>> getCancionesRecientes() {
        return cancionesRecientes;
    }
    public void setCancionesRecientes(ArrayList<HashMap<String, Object>> cancionesRecientes){
        this.cancionesRecientes = cancionesRecientes;
    }

    public void reverseArray(){
        if(cancionesRecientes != null){
            Collections.reverse(cancionesRecientes);
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RowRecentActivityBinding binding;
        private final OnNoteListener noteListener;
        public ViewHolder(RowRecentActivityBinding binding, OnNoteListener noteListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.noteListener = noteListener;
            binding.getRoot().setOnClickListener(this);
            binding.nameSong.setSelected(true);
            binding.nameArtist.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            this.noteListener.onNoteClick(getAdapterPosition(),"RecentActivity");
        }
    }
}
