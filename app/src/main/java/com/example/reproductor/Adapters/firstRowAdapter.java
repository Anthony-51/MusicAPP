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
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.RowMainBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class firstRowAdapter extends RecyclerView.Adapter<firstRowAdapter.ViewHolder>{
    private ArrayList<Canciones> canciones;
    private ServicesFirebase service;
    OnNoteListener mNoteListener;
    Context context;
    public firstRowAdapter(){

    }
    public firstRowAdapter(ArrayList<Canciones> canciones,OnNoteListener mNoteListener) {
        this.canciones = canciones;
        this.mNoteListener = mNoteListener;
        service = new ServicesFirebase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
       RowMainBinding rowBinding = RowMainBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(rowBinding,mNoteListener);
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
            holder.binding.textRecycler.setText(canciones.get(position).getNombre());
        }

    }

    @Override
    public int getItemCount() {
        if(canciones != null){
            return canciones.size();
        }else{
            return 9;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RowMainBinding binding;
        OnNoteListener onNoteListener;
        public ViewHolder(RowMainBinding binding,OnNoteListener onNoteListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onNoteListener = onNoteListener;
            binding.getRoot().setOnClickListener(this);
            binding.textRecycler.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            this.onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

}
