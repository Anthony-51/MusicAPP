package com.example.reproductor.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Models.Clases.ArtistModel;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowArtistHomeAdapterBinding;

import java.util.ArrayList;

public class ArtistHomeAdapter extends RecyclerView.Adapter<ArtistHomeAdapter.ViewHolder> {

    private ArrayList<ArtistModel> artistasArrayList;
    private final ServicesFirebase services;
    private Context context;

    public ArtistHomeAdapter(ArrayList<ArtistModel> artistasArrayList) {
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
        if(artistasArrayList != null){
            String nombre = artistasArrayList.get(position).getNombre();
            Object img = services.getImage(nombre);
            Glide.with(context).load(img).into(holder.homeArtistBinding.imgArtistHome);
            holder.homeArtistBinding.textArtistHome.setText(nombre);
        }
    }

    public ArrayList<ArtistModel> getArtistasArrayList() {
        return artistasArrayList;
    }
    public void setArtistasArrayList(ArrayList<ArtistModel> artistasArrayList) {
        this.artistasArrayList = artistasArrayList;
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return artistasArrayList == null || artistasArrayList.size() == 0 ? 6 : artistasArrayList.size();
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
