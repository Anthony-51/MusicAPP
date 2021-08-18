package com.example.reproductor.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.databinding.RowListSearchBinding;

import java.util.ArrayList;


public class ListSearchAdapter extends RecyclerView.Adapter<ListSearchAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<SongModel> songsAll;
    private final ArrayList<SongModel> songAllList;
    private final OnNoteListener mNoteListener;

    public ListSearchAdapter(ArrayList<SongModel> songsAll, OnNoteListener mNoteListener) {
        this.songsAll = songsAll;
        this.songAllList = new ArrayList<>(songsAll);
        this.mNoteListener = mNoteListener;
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
            String cancion = songsAll.get(position).getCancion();
            holder.binding.textRecycler.setText(cancion);
        }
    }

    @Override
    public int getItemCount() {

        return songsAll != null ? songsAll.size() : 6;

    }

    public ArrayList<SongModel> getSongsAll() {
        return songsAll;
    }

    public void setSongsAll(ArrayList<SongModel> songsAll) {
        this.songsAll = songsAll;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<SongModel> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(songAllList);

            }else{
                for(SongModel cs : songAllList){
                    if(cs.getCancion().toLowerCase().contains(constraint.toString().toLowerCase())){
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
            songsAll.addAll((ArrayList<SongModel>) results.values);
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
