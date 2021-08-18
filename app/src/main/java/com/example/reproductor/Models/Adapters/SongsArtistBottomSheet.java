package com.example.reproductor.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Models.Clases.ArtistModel;
import com.example.reproductor.Interfaces.OpenBottomSheetArtist;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowArtistBottomSheetBinding;

public class SongsArtistBottomSheet extends RecyclerView.Adapter<SongsArtistBottomSheet.ViewHolder> {
//    private ArrayList<String> songs;
    private final ArtistModel artistas;
    private ServicesFirebase services;
    private Context context;
    private OpenBottomSheetArtist openBottomSheetArtist;

    public SongsArtistBottomSheet(ArtistModel artistas, OpenBottomSheetArtist openBottomSheetArtist) {
        this.artistas = artistas;
        this.openBottomSheetArtist = openBottomSheetArtist;
        this.services = new ServicesFirebase();
    }

    @NonNull
    @Override
    public SongsArtistBottomSheet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowArtistBottomSheetBinding songBinding = RowArtistBottomSheetBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(songBinding,openBottomSheetArtist);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsArtistBottomSheet.ViewHolder holder, int position) {

        if(artistas != null){
            String urlImg = artistas.getCanciones().get(position).getUrlImg();
            String cancion = artistas.getCanciones().get(position).getCancion();
            holder.binding.nameSongArtist.setText(cancion);
            Glide.with(context).load(urlImg).into(holder.binding.imgSongArtist);
        }

    }

    @Override
    public int getItemCount() {
        return artistas.getCanciones().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final OpenBottomSheetArtist openBottomSheetArtist;
        private final RowArtistBottomSheetBinding binding;
        public ViewHolder(RowArtistBottomSheetBinding binding,OpenBottomSheetArtist openBottomSheetArtist){
            super(binding.getRoot());
            this.binding = binding;
            this.openBottomSheetArtist = openBottomSheetArtist;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openBottomSheetArtist.sendPositionArtist(getAdapterPosition());
        }
    }
}
