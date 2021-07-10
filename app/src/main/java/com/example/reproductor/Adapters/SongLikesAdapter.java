package com.example.reproductor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Artistas;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowSongLikesBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongLikesAdapter extends RecyclerView.Adapter<SongLikesAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> songLikesArrayList;
    private final ServicesFirebase services;
    private Context context;
    private final OnNoteListener onNoteListener;


    public SongLikesAdapter(ArrayList<HashMap<String, Object>> songLikesArrayList, OnNoteListener onNoteListener) {
        this.songLikesArrayList = songLikesArrayList;
        this.onNoteListener = onNoteListener;
        this.services = new ServicesFirebase();
    }

    @NonNull
    @Override
    public SongLikesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowSongLikesBinding binding = RowSongLikesBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongLikesAdapter.ViewHolder holder, int position) {
        if(songLikesArrayList != null){
            String carpeta = songLikesArrayList.get(position).get("Artista").toString();
            String cancion = songLikesArrayList.get(position).get("Cancion").toString();
            Object img = services.getImage(carpeta,cancion);
            Glide.with(context).load(img).into(holder.vBinding.imgSong);
            holder.vBinding.nameSong.setText(cancion);
        }
    }

    @Override
    public int getItemCount() {
        if(songLikesArrayList != null){
            return songLikesArrayList.size();
        }else {
            return 9;
        }
    }

    public ArrayList<HashMap<String, Object>> getSongLikesArrayList() {
        return songLikesArrayList;
    }

    public void setSongLikesArrayList(ArrayList<HashMap<String, Object>> songLikesArrayList) {
        this.songLikesArrayList = songLikesArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RowSongLikesBinding vBinding;
        private final OnNoteListener onNoteListener;
        public ViewHolder(RowSongLikesBinding binding, OnNoteListener onNoteListener) {
            super(binding.getRoot());
            this.vBinding = binding;
            this.onNoteListener = onNoteListener;
            this.vBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition(),"");
        }
    }
}
