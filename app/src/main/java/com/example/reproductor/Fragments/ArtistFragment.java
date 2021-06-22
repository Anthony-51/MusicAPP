package com.example.reproductor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Adapters.ArtistAdapter;
import com.example.reproductor.Clases.Artistas;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.FragmentArtistBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArtistFragment extends Fragment {
    private FragmentArtistBinding binding;
    private RecyclerView.LayoutManager linearLayout;
    private ArtistAdapter artistAdapter;
    private ServicesFirebase service;
    private ArrayList<Artistas> artistas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentArtistBinding.inflate(inflater, container, false);

        service = new ServicesFirebase();
        artistas = new ArrayList<>();

        loadData();
        linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        binding.reciclerArtist.setLayoutManager(linearLayout);

        artistAdapter = new ArtistAdapter(artistas);
        binding.reciclerArtist.setAdapter(artistAdapter);

        return binding.getRoot();
    }

    void loadData(){
        service.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(final DataSnapshot data : snapshot.getChildren()){
                    if(!data.getKey().equals("users")){
                        Artistas at = data.getValue(Artistas.class);
                        artistas.add(at);
                        artistAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}