package com.example.reproductor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Artistas;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowArtistHomeAdapterBinding;

import java.util.ArrayList;

public class ArtistHomeAdapter extends RecyclerView.Adapter<ArtistHomeAdapter.ViewHolder> {

    private ArrayList<String> artistasArrayList;
    private final ServicesFirebase services;
    private Context context;

    public ArtistHomeAdapter(ArrayList<String> artistasArrayList) {
        this.artistasArrayList = artistasArrayList;
        this.services = new ServicesFirebase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowArtistHomeAdapterBinding binding = RowArtistHomeAdapterBinding.inflate(LayoutInflater.from(context),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHomeAdapter.ViewHolder holder, int position) {
        String nombre = artistasArrayList.get(position);
        Object img = services.getImage(nombre);
        Glide.with(context).load(img).into(holder.homeArtistBinding.imgArtistHome);
        holder.homeArtistBinding.textArtistHome.setText(nombre);
    }

    public ArrayList<String> getArtistasArrayList() {
        return artistasArrayList;
    }
    public void setArtistasArrayList(ArrayList<String> artistasArrayList) {
        this.artistasArrayList = artistasArrayList;
    }
    @Override
    public int getItemCount() {
        return artistasArrayList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RowArtistHomeAdapterBinding homeArtistBinding;
        public ViewHolder(RowArtistHomeAdapterBinding homeArtistBinding){
            super(homeArtistBinding.getRoot());
            this.homeArtistBinding = homeArtistBinding;
            homeArtistBinding.textArtistHome.setSelected(true);
        }
    }
}
