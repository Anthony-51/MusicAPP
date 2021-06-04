package com.example.reproductor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.R;
import com.example.reproductor.Adapters.firstRowAdapter;
import com.example.reproductor.databinding.FragmentGridRecyclerBinding;

public class gridFragmentRecycler extends Fragment {
    private FragmentGridRecyclerBinding binding;
    private RecyclerView.LayoutManager gridLayout;
    private firstRowAdapter rowAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGridRecyclerBinding.inflate(inflater, container, false);
        gridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        binding.firstRown.setLayoutManager(gridLayout);

        rowAdapter = new firstRowAdapter();
        binding.firstRown.setAdapter(rowAdapter);
        return binding.getRoot();
    }
}