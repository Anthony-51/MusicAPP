package com.example.reproductor.Views.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Models.Adapters.RecentActivityAdapter;
import com.example.reproductor.Models.Adapters.SongLikesAdapter;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Models.Clases.User;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.databinding.FragmentLibreriaBinding;

import java.util.ArrayList;
import java.util.Collections;

public class LibreriaFragment extends Fragment implements OnNoteListener {

    private static final String TAG = "LibreriaFragment";
    private FragmentLibreriaBinding binding;
    private User user;
    private SwipeRefreshLayout refreshLayout;
    SendData sendData;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentLibreriaBinding.inflate(inflater, container, false);

        user = User.getInstance();
        ArrayList<SongModel> list = new ArrayList<>(user.getCancionesRecientes());
        Collections.reverse(list);

        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager gridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerLikes.setLayoutManager(linearLayout);
        binding.recyclerLibrary.setLayoutManager(gridLayout);

        RecentActivityAdapter activityAdapter = new RecentActivityAdapter(list, this);
        SongLikesAdapter likesAdapter = new SongLikesAdapter(user.getSongLikes(), this);
        binding.recyclerLibrary.setAdapter(activityAdapter);
        binding.recyclerLikes.setAdapter(likesAdapter);
        visibilityViews();

        refreshLayout = binding.libraryContent;

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateContent(list, activityAdapter, likesAdapter);
            }
        });

        return binding.getRoot();
    }

    private void visibilityViews(){
        if(user.getSongLikes() == null && user.getCancionesRecientes() == null){
            binding.layoutSongLikes.setVisibility(View.GONE);
            binding.layoutActRecent.setVisibility(View.GONE);
            binding.txtMessage.setVisibility(View.VISIBLE);
        }else{
            binding.txtMessage.setVisibility(View.GONE);
            if(user.getSongLikes() != null){
                binding.layoutSongLikes.setVisibility(View.VISIBLE);
            }else{
                binding.layoutSongLikes.setVisibility(View.GONE);
            }
            if(user.getCancionesRecientes() != null){
                binding.layoutActRecent.setVisibility(View.VISIBLE);
            }else {
                binding.layoutActRecent.setVisibility(View.GONE);
            }
        }
    }

    private void updateContent(ArrayList<SongModel> list, RecentActivityAdapter activityAdapter, SongLikesAdapter likesAdapter) {
        if(user.getSongLikes() != null){
            list.clear();
            list.addAll(user.getCancionesRecientes());
            Collections.reverse(list);
            activityAdapter.setCancionesRecientes(list);
        }
        if(user.getCancionesRecientes() != null){
            likesAdapter.setSongLikesArrayList(user.getSongLikes());
        }
        visibilityViews();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onNoteClick(int position, String recycler) {
        if(recycler.equals("RecentActivity")){
            sendData.getData(user.getCancionesRecientes().get(position),user.getCancionesRecientes());
        }else{
            sendData.getData(user.getSongLikes().get(position), user.getSongLikes());
        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            sendData = (SendData) activity;
        }catch(RuntimeException e){
            throw new RuntimeException(activity.toString()+"Must implement method");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}