package com.example.reproductor.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.Target;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.R;
import com.example.reproductor.databinding.RowSongsUploadBinding;

import java.util.ArrayList;

public class SongsUploadAdapter extends RecyclerView.Adapter<SongsUploadAdapter.ViewHolder> {
    private final ArrayList<SongModel> songModels;
    private Context context;

    public SongsUploadAdapter(ArrayList<SongModel> songModels) {
        this.songModels = songModels;
    }

    @NonNull
    @Override
    public SongsUploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowSongsUploadBinding vbSongUpl = RowSongsUploadBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(vbSongUpl);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsUploadAdapter.ViewHolder holder, int position) {
        if(songModels != null){
            if(!songModels.isEmpty()){
                Glide.with(context).load(songModels.get(position).getUrlImg())
                        .centerCrop()
                        .placeholder(R.drawable.ainaaiba)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)
                        .into(holder.vbSongUpl.imgSong);
                holder.vbSongUpl.songName.setText(songModels.get(position).getCancion());
            }
        }
    }

    @Override
    public int getItemCount() {
        return songModels != null ? songModels.size() : 6;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RowSongsUploadBinding vbSongUpl;
        public ViewHolder(RowSongsUploadBinding vbSonUpl) {
            super(vbSonUpl.getRoot());
            this.vbSongUpl = vbSonUpl;
        }
    }
}
