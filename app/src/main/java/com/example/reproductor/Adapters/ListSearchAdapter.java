package com.example.reproductor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowListSearchBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class ListSearchAdapter extends RecyclerView.Adapter<ListSearchAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Canciones> songsAll;
    private final ArrayList<Canciones> songAllList;
    private final OnNoteListener mNoteListener;
//    private final ServicesFirebase service;

    public ListSearchAdapter(ArrayList<Canciones> songsAll, OnNoteListener mNoteListener) {
        this.songsAll = songsAll;
        this.songAllList = new ArrayList<>(songsAll);
        this.mNoteListener = mNoteListener;
//        this.service = new ServicesFirebase();
    }

    @NonNull
    @Override
    public ListSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowListSearchBinding binding = RowListSearchBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding, mNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSearchAdapter.ViewHolder holder, int position) {
        if(songsAll != null){
            String artista = songsAll.get(position).getArtista();
            String cancion = songsAll.get(position).getNombre();
//            Object img = service.getImage(artista,cancion);
//            Glide.with(context)
//                    .load(img)
//                    .centerCrop()
//                    .placeholder(R.drawable.aiba)
//                    .into(holder.binding.imageRecycler);
            holder.binding.textRecycler.setText(cancion);
        }
    }

    @Override
    public int getItemCount() {

        return songsAll != null ? songsAll.size() : 6;

    }

    public ArrayList<Canciones> getSongsAll() {
        return songsAll;
    }

    public void setSongsAll(ArrayList<Canciones> songsAll) {
        this.songsAll = songsAll;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Canciones> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(songAllList);

            }else{
                for(Canciones cs : songAllList){
                    if(cs.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(cs);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            songsAll.clear();
            songsAll.addAll((ArrayList<Canciones>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final OnNoteListener onNoteListener;
        private final RowListSearchBinding binding;
        public ViewHolder(RowListSearchBinding binding, OnNoteListener onNoteListener) {
            super(binding.getRoot());
            this.onNoteListener = onNoteListener;
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition(),"");
        }
    }
}
