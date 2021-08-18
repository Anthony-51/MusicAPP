package com.example.reproductor.Models.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Models.Clases.SongsList;
import com.example.reproductor.Controllers.MusicController;
import com.example.reproductor.Interfaces.ItemTouchHelperListener;
import com.example.reproductor.R;
import com.example.reproductor.databinding.RowMainArtistBinding;

import java.util.ArrayList;

public class ArtistRecommendedAdapter extends RecyclerView.Adapter<ArtistRecommendedAdapter.ViewHolder> implements ItemTouchHelperListener {
    private ArrayList<SongModel> songModels;
    private Context context;
    private ItemTouchHelper mListener;
    private final SongsList songsList;
    private final MusicController music;
    public ArtistRecommendedAdapter(ArrayList<SongModel> songModels) {
        this.songModels = songModels;
        songsList = SongsList.getInstance();
        music =MusicController.getInstance();
    }

    @NonNull
    @Override
    public ArtistRecommendedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RowMainArtistBinding binding = RowMainArtistBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistRecommendedAdapter.ViewHolder holder, int position) {
        if(songModels != null){
            if(music.getIndex() == position){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.light_gray));
            }else{
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.black));
            }
            String urlImg = songModels.get(position).getUrlImg();
            String cancion = songModels.get(position).getCancion();

            Glide.with(context).load(urlImg).into(holder.binding.imageRecycler);
            holder.binding.textRecycler.setText(cancion);
        }


    }

    @Override
    public int getItemCount() {
        return songModels != null ? songModels.size() : 5;
    }

    public void setSongModels(ArrayList<SongModel> songModels){
        this.songModels = songModels;
        this.notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        SongModel model = songModels.get(fromPosition);
        songModels.remove(model);
        songModels.add(toPosition,model);
        songsList.setSongsPlaying(songModels);
        notifyItemMoved(fromPosition,toPosition);
        music.setIndex(songsList.getSongsPlaying());
    }

    @Override
    public void onItemSwiped(int position){
        ArrayList<SongModel> tempModels = new ArrayList<>(songModels);
        tempModels.remove(position);
        songsList.setSongsPlaying(tempModels);
        setSongModels(tempModels);
    }

    public void setItemTouchHelper(ItemTouchHelper mListener){
        this.mListener = mListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private final RowMainArtistBinding binding;
        private final GestureDetector mGestureListener;
        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(RowMainArtistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.mGestureListener = new GestureDetector(binding.getRoot().getContext(),this);
            binding.getRoot().setOnTouchListener(this);
        }


        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //same clicklistener
            Log.e("TAG", "onSingleTapUp: " );
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mListener.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureListener.onTouchEvent(event);
            return true;
        }
    }
}
