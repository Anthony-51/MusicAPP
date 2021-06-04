package com.example.reproductor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Adapters.RecentActivityAdapter;
import com.example.reproductor.Adapters.SongLikesAdapter;
import com.example.reproductor.R;
import com.example.reproductor.databinding.FragmentLibreriaBinding;

public class LibreriaFragment extends Fragment {

    private FragmentLibreriaBinding binding;
    private RecentActivityAdapter activityAdapter;
    private SongLikesAdapter likesAdapter;
    private RecyclerView.LayoutManager gridLayout, linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentLibreriaBinding.inflate(inflater, container, false);

        linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        gridLayout = new GridLayoutManager(getContext(),1, LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerLibrary.setLayoutManager(gridLayout);
        binding.recyclerLikes.setLayoutManager(linearLayout);

        activityAdapter = new RecentActivityAdapter();
        likesAdapter = new SongLikesAdapter();
        binding.recyclerLibrary.setAdapter(activityAdapter);
        binding.recyclerLikes.setAdapter(likesAdapter);

        return binding.getRoot();
    }
}