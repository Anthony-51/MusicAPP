package com.example.reproductor.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowMainBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class firstRowAdapter extends RecyclerView.Adapter<firstRowAdapter.ViewHolder>{
    private ArrayList<Canciones> canciones;
    private ServicesFirebase service;
    public String recycler;
    OnNoteListener mNoteListener;
    Context context;
    private ArrayList<HashMap<String, Object>> songLikesArrayList;

    public firstRowAdapter() {
    }

    public firstRowAdapter(OnNoteListener mNoteListener, ArrayList<HashMap<String, Object>> songLikesArrayList, String recycler) {
        this.mNoteListener = mNoteListener;
        this.songLikesArrayList = songLikesArrayList;
        this.recycler = recycler;
        service = new ServicesFirebase();
    }

    public firstRowAdapter(ArrayList<Canciones> canciones, OnNoteListener mNoteListener, String recycler) {
        this.canciones = canciones;
        this.mNoteListener = mNoteListener;
        this.recycler = recycler;
        service = new ServicesFirebase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
       RowMainBinding rowBinding = RowMainBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(rowBinding, mNoteListener,recycler);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(canciones != null){
            String artista = canciones.get(position).getArtista();
            String cancion = canciones.get(position).getNombre();
            Object img = service.getImage(artista,cancion);
            Glide.with(context)
                    .load(img)
                    .centerCrop()
                    .placeholder(R.drawable.aiba)
                    .into(holder.binding.imageRecycler);
            holder.binding.textRecycler.setText(cancion);
        }
        if(songLikesArrayList != null){
            String artista = songLikesArrayList.get(position).get("Artista").toString();
            String cancion = songLikesArrayList.get(position).get("Cancion").toString();
            Object img = service.getImage(artista,cancion);
            Glide.with(context)
                    .load(img)
                    .centerCrop()
                    .placeholder(R.drawable.aiba)
                    .into(holder.binding.imageRecycler);
            holder.binding.textRecycler.setText(cancion);
        }
    }

    @Override
    public int getItemCount() {
        if (canciones == null) {
            if(songLikesArrayList != null){
                return songLikesArrayList.size();
            }
            else{
                return 9;
            }
        } else {
            return canciones.size();
        }
    }

    public ArrayList<Canciones> getCanciones() {
        return canciones;
    }

    public void setCanciones(ArrayList<Canciones> canciones) {
        this.canciones = canciones;
    }

    public ArrayList<HashMap<String, Object>> getSongLikesArrayList() {
        return songLikesArrayList;
    }

    public void setSongLikesArrayList(ArrayList<HashMap<String, Object>> songLikesArrayList) {
        this.songLikesArrayList = songLikesArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final RowMainBinding binding;
        OnNoteListener onNoteListener;
        private final String recycler;
        public ViewHolder(RowMainBinding binding,OnNoteListener onNoteListener, String recycler) {
            super(binding.getRoot());
            this.binding = binding;
            this.onNoteListener = onNoteListener;
            this.recycler = recycler;
            binding.getRoot().setOnClickListener(this);
            binding.textRecycler.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            this.onNoteListener.onNoteClick(getAdapterPosition(),recycler);
        }

    }

}
