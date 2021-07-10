package com.example.reproductor.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Adapters.RecentActivityAdapter;
import com.example.reproductor.Adapters.SongLikesAdapter;
import com.example.reproductor.Clases.Usuario;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.FragmentLibreriaBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LibreriaFragment extends Fragment implements OnNoteListener {

    private static final String TAG = "LibreriaFragment";
    private FragmentLibreriaBinding binding;
    private RecentActivityAdapter activityAdapter;
    private SongLikesAdapter likesAdapter;
    private RecyclerView.LayoutManager gridLayout, linearLayout;
    private ServicesFirebase services;
    private Usuario user;
    private String email;
    private SwipeRefreshLayout refreshLayout;
    SendData sendData;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentLibreriaBinding.inflate(inflater, container, false);
        services = new ServicesFirebase();
        user = new Usuario();
        getData();

        linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        gridLayout = new GridLayoutManager(getContext(),1, LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerLikes.setLayoutManager(linearLayout);
        binding.recyclerLibrary.setLayoutManager(gridLayout);

        activityAdapter = new RecentActivityAdapter(user.getCancionesRecientes(),this);
        likesAdapter = new SongLikesAdapter(user.getSongLikes(),this);
        binding.recyclerLibrary.setAdapter(activityAdapter);
        binding.recyclerLikes.setAdapter(likesAdapter);

        refreshLayout = binding.libraryContent;

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                refreshLayout.setRefreshing(false);
            }
        });

        return binding.getRoot();
    }

    private void getData(){
        if(services.getFirebaseAuth().getCurrentUser() != null){
            email = services.getFirebaseAuth().getCurrentUser().getEmail();
            services.getFirebaseFirestore().collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            user = documentSnapshot.toObject(Usuario.class);
                            activityAdapter.setCancionesRecientes(user.getCancionesRecientes());
                            activityAdapter.reverseArray();
                            likesAdapter.setSongLikesArrayList(user.getSongLikes());
                            binding.recyclerLibrary.setAdapter(activityAdapter);
                            binding.recyclerLikes.setAdapter(likesAdapter);
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onNoteClick(int position, String recycler) {
        String carpeta, cancion;
        if(recycler.equals("RecentActivity")){
            Log.e(TAG, "onNoteClick: " + user.getCancionesRecientes().get(position));
            carpeta = user.getCancionesRecientes().get(position).get("Artista").toString();
            cancion = user.getCancionesRecientes().get(position).get("Cancion").toString();
            sendData.getData(carpeta, cancion);
        }else{
            carpeta = user.getSongLikes().get(position).get("Artista").toString();
            cancion = user.getSongLikes().get(position).get("Cancion").toString();
            sendData.getData(carpeta, cancion);
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
}