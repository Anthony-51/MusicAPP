package com.example.reproductor.Adapters;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Artistas;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.OpenBottomSheetArtist;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowArtistsBinding;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private  ArrayList<Artistas> artist;
    private Context context;
    private final ServicesFirebase servicesFirebase;
    private final OpenBottomSheetArtist openBottomSheetArtist;
    public ArtistAdapter(ArrayList<Artistas> artist, OpenBottomSheetArtist openBottomSheetArtist) {
        this.servicesFirebase = new ServicesFirebase();
        this.artist = artist;
        this.openBottomSheetArtist = openBottomSheetArtist;
    }

    @NonNull
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowArtistsBinding binding = RowArtistsBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding, openBottomSheetArtist);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ViewHolder holder, int position) {
        String nombre =  artist.get(position).getNombre();
        String descripcion = artist.get(position).getDescripcion();
        Object img = servicesFirebase.getImage(nombre);
        holder.binding.titleArtist.setText(nombre);
        holder.binding.description.setText(descripcion);
        if (position % 2 != 0){
            Glide.with(context).load(img).into(holder.binding.secondImage);
            holder.binding.firstImage.setVisibility(View.GONE);
            holder.binding.secondImage.setVisibility(View.VISIBLE);
        }else{
            Glide.with(context).load(img).into(holder.binding.firstImage);
            holder.binding.firstImage.setVisibility(View.VISIBLE);
            holder.binding.secondImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return artist != null ? artist.size() : 5;
    }

    public ArrayList<Artistas> getArtist() {
        return artist;
    }

    public void setArtist(ArrayList<Artistas> artist) {
        this.artist = artist;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RowArtistsBinding binding;
        private final OpenBottomSheetArtist openBottomSheetArtist;
        public ViewHolder(RowArtistsBinding binding, OpenBottomSheetArtist openBottomSheetArtist) {
            super(binding.getRoot());
            this.binding = binding;
            this.openBottomSheetArtist = openBottomSheetArtist;
            binding.description.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            binding.description.setMovementMethod(new ScrollingMovementMethod());
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openBottomSheetArtist.sendPositionArtist(getAdapterPosition());
        }
    }
}
