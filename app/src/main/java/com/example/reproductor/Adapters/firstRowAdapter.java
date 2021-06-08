package com.example.reproductor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.GlideApp;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.ViewReproductor;
import com.example.reproductor.databinding.RowMainBinding;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Stack;

public class firstRowAdapter extends RecyclerView.Adapter<firstRowAdapter.ViewHolder> {
    private ArrayList<Canciones> canciones;
    private ServicesFirebase service;
    Context context;
    public firstRowAdapter(ArrayList<Canciones> canciones) {
        this.canciones = canciones;
        service = new ServicesFirebase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
       RowMainBinding rowBinding = RowMainBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String artista = canciones.get(position).getArtista();
        String cancion = canciones.get(position).getNombre();
        Object img = service.getImage(artista,cancion);
        Glide.with(context).load(img).into(holder.binding.imageRecycler);
        holder.binding.textRecycler.setText(canciones.get(position).getNombre());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return canciones.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowMainBinding binding;
        public ViewHolder(RowMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.textRecycler.setSelected(true);
        }
    }
    void startActivity(int position){
        Intent intent = new Intent(context, ViewReproductor.class);
        intent.putExtra("ArrayCanciones",canciones);
        intent.putExtra("Index",position);
        context.startActivity(intent);

    }

}
