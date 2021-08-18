package com.example.reproductor.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.databinding.RowSongLikesBinding;

import java.util.ArrayList;

public class SongLikesAdapter extends RecyclerView.Adapter<SongLikesAdapter.ViewHolder> {
    private ArrayList<SongModel> songLikesArrayList;
    private Context context;
    private final OnNoteListener onNoteListener;


    public SongLikesAdapter(ArrayList<SongModel> songLikesArrayList, OnNoteListener onNoteListener) {
        this.songLikesArrayList = songLikesArrayList;
        this.onNoteListener = onNoteListener;
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
            String urlImg = songLikesArrayList.get(position).getUrlImg();
            String cancion = songLikesArrayList.get(position).getCancion();
            Glide.with(context).load(urlImg).into(holder.vBinding.imgSong);
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
    public void setSongLikesArrayList(ArrayList<SongModel> listModel){
        this.songLikesArrayList = listModel;
        this.notifyDataSetChanged();
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
