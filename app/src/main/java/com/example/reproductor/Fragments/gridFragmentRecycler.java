package com.example.reproductor.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.Adapters.ArtistRecommendedAdapter;
import com.example.reproductor.Adapters.FavoriteAdapter;
import com.example.reproductor.Adapters.SongHomeAdapter;
import com.example.reproductor.Adapters.firstRowAdapter;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.FragmentGridRecyclerBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Stack;

public class gridFragmentRecycler extends Fragment {
    private FragmentGridRecyclerBinding binding;
    private RecyclerView.LayoutManager firstGridLayout,secondGridLayout, thirdGridLayout,fourthGridLayout;
    private firstRowAdapter rowAdapter;
    private FavoriteAdapter favoriteAdapter;
    private ArtistRecommendedAdapter artistRecommended;
    private SongHomeAdapter songAdapter;
    private ServicesFirebase ser;
    ArrayList<Canciones> canciones;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGridRecyclerBinding.inflate(inflater, container, false);

        ser = new ServicesFirebase();
        canciones = new ArrayList<>();
        setLayouts();
        getDatos();

        rowAdapter = new firstRowAdapter(canciones);
        rowAdapter.notifyDataSetChanged();
        favoriteAdapter = new FavoriteAdapter();
        artistRecommended = new ArtistRecommendedAdapter();
        songAdapter = new SongHomeAdapter();



        binding.firstRown.setAdapter(rowAdapter);
        binding.secondRow.setAdapter(favoriteAdapter);
        binding.thirdRow.setAdapter(artistRecommended);
        binding.fourthRow.setAdapter(songAdapter);
        return binding.getRoot();
    }

    void getDatos(){
        ser.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(final DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String nombre = dataSnapshot.getKey();
                        ser.getDatabaseReference().child(nombre).child("Canciones").limitToFirst(3).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(final DataSnapshot ds : snapshot.getChildren()){
                                    Canciones cs = ds.getValue(Canciones.class);
                                    cs.setArtista(nombre);
                                    canciones.add(cs);
                                    rowAdapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    Log.e("prueba",""+nombre);
                }
            }

            @Override
            public void onCancelled (DatabaseError error){
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    void setLayouts(){
        firstGridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        secondGridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        thirdGridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        fourthGridLayout = new GridLayoutManager(getContext(),1,LinearLayoutManager.HORIZONTAL,false);
        binding.firstRown.setLayoutManager(firstGridLayout);
        binding.secondRow.setLayoutManager(secondGridLayout);
        binding.thirdRow.setLayoutManager(thirdGridLayout);
        binding.fourthRow.setLayoutManager(fourthGridLayout);
    }
}