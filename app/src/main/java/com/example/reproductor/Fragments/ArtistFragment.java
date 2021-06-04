package com.example.reproductor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Adapters.ArtistAdapter;
import com.example.reproductor.R;
import com.example.reproductor.databinding.FragmentArtistBinding;

public class ArtistFragment extends Fragment {
    private FragmentArtistBinding binding;
    private RecyclerView.LayoutManager linearLayout;
    private ArtistAdapter artistAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentArtistBinding.inflate(inflater, container, false);

        linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        binding.reciclerArtist.setLayoutManager(linearLayout);

        artistAdapter = new ArtistAdapter();
        binding.reciclerArtist.setAdapter(artistAdapter);

        return binding.getRoot();
    }
}