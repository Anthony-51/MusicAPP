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
import com.example.reproductor.R;
import com.example.reproductor.databinding.RowMainBinding;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    public String recycler;
    OnNoteListener mNoteListener;
    Context context;
    private ArrayList<SongModel> songModels;

    public HomeAdapter() {
    }

    public HomeAdapter(OnNoteListener mNoteListener, ArrayList<SongModel> songModels, String recycler) {
        this.mNoteListener = mNoteListener;
        this.songModels = songModels;
        this.recycler = recycler;
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
        if(songModels != null){
            String cancion = songModels.get(position).getCancion();
            String urlImg = songModels.get(position).getUrlImg();
            Glide.with(context)
                    .load(urlImg)
                    .centerCrop()
                    .placeholder(R.drawable.aiba)
                    .into(holder.binding.imageRecycler);
            holder.binding.textRecycler.setText(cancion);
        }
    }

    @Override
    public int getItemCount() {
        if(songModels != null){
            return songModels.size();
        }
        else{
            return 9;
        }
    }

    public ArrayList<SongModel> getSongModels() {
        return songModels;
    }

    public void setSongModels(ArrayList<SongModel> songModels) {
        this.songModels = songModels;
        this.notifyDataSetChanged();
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
