package com.example.reproductor.Fragments;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.reproductor.Activities.MainActivity;
import com.example.reproductor.Adapters.ArtistAdapter;
import com.example.reproductor.Adapters.ArtistHomeAdapter;
import com.example.reproductor.Adapters.firstRowAdapter;
import com.example.reproductor.Clases.Artistas;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.Clases.Usuario;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.example.reproductor.databinding.HomeFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment implements OnNoteListener {
    private ServicesFirebase services;
    public static HomeFragmentBinding binding;
    private RecyclerView.LayoutManager firstGridLayout,secondGridLayout, thirdGridLayout,fourthGridLayout;
    private firstRowAdapter rowAdapter,secondAdapter,fourthAdapter;
    private ArtistHomeAdapter artistHomeAdapter;
    public static ArrayList<Canciones> canciones, songsSameByArtist, randomSongs;
    private ArrayList<HashMap<String, Object>> songLikesArrayList;
    private ArrayList<String> nameArtist;
    SendData sendData;
    int index,height;
    private final String TAG = "Home Fragment";
    private SwipeRefreshLayout refresh;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        services = new ServicesFirebase();
        nameArtist = new ArrayList<>();
//
//        songLikesArrayList = new ArrayList<>();
        setLayouts();
        getDatos();

        rowAdapter = new firstRowAdapter(canciones,this, "Recommended");
        secondAdapter = new firstRowAdapter(this,songLikesArrayList,"Favorite");
        artistHomeAdapter = new ArtistHomeAdapter(nameArtist);
        fourthAdapter = new firstRowAdapter(songsSameByArtist,this,"OthersSongsArtist");


        binding.firstRown.setAdapter(rowAdapter);
        binding.secondRow.setAdapter(secondAdapter);
        binding.thirdRow.setAdapter(artistHomeAdapter);
        binding.fourthRow.setAdapter(fourthAdapter);

        getUser();
        refresh = binding.homeContent;
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDatos();
                refresh.setRefreshing(false);
            }
        });

        return binding.getRoot();
    }

    private void getUser(){
        if(services.getFirebaseAuth().getCurrentUser() != null){
            binding.layoutFavorites.setVisibility(View.VISIBLE);
            binding.layoutSongs.setVisibility(View.VISIBLE);
        }else{
            binding.layoutFavorites.setVisibility(View.GONE);
            binding.layoutSongs.setVisibility(View.GONE);
        }
    }

    void getDatos(){
        if(services.getFirebaseAuth().getCurrentUser() != null){
            String email = services.getFirebaseAuth().getCurrentUser().getEmail();
            services.getFirebaseFirestore().collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Usuario user = document.toObject(Usuario.class);
                            if(user.getSongLikes() != null){
                                songLikesArrayList = user.getSongLikes();
                            }
                             othersSongsBySameArtist(user.getSongLikes());
                             secondAdapter.setSongLikesArrayList(songLikesArrayList);
                             binding.secondRow.setAdapter(secondAdapter);
                        }
                    }
                }
            });
            services.getFirebaseFirestore().collection("artistas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if(canciones == null){
                            canciones = new ArrayList<>();
                            randomSongs = new ArrayList<>();
                        }else{
                            canciones.clear();
                            randomSongs.clear();
                            nameArtist.clear();
                        }
                        for (DocumentSnapshot document : task.getResult()){
                                nameArtist.add(document.getId());
                                final ArrayList<String> randomSong = (ArrayList<String>) document.get("Canciones");
                                if(randomSong != null){
                                    for(int i = 0; i < randomSong.size(); i++){
                                        Canciones cs = new Canciones(randomSong.get(i),document.getId());
                                        randomSongs.add(cs);
                                    }
                                }
                        }
                        final int size = randomSongs.size();
                        for(int  i = 0; i < size; i++){
                            final Canciones rSong = randomSongs.get(new Random().nextInt(size));
                            if(!canciones.contains(rSong)){
                                if(canciones.size() == 9){
                                    break;
                                }
                                canciones.add(rSong);
                            }
                        }
                        rowAdapter.setCanciones(canciones);
                        artistHomeAdapter.setArtistasArrayList(nameArtist);
                        binding.firstRown.setAdapter(rowAdapter);
                        binding.thirdRow.setAdapter(artistHomeAdapter);
                    }
                }
            });
        }

    }
    private void othersSongsBySameArtist(ArrayList<HashMap<String,Object>> songs){
        final int size = songs.size();
        ArrayList<Canciones> prbcanciones = new ArrayList<>();

        List<String> artistas = new ArrayList<>();
        for(int i = 0; i < size; i++){
            final String artist = songs.get(i).get("Artista").toString();
            if(!artistas.contains(artist)){
                artistas.add(artist);
            }
        }
        services.getFirebaseFirestore().collection("artistas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(songsSameByArtist == null){
                        songsSameByArtist = new ArrayList<>();
                    }else{
                        songsSameByArtist.clear();
                        prbcanciones.clear();
                    }
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        if(artistas.contains(documentSnapshot.getId())){
                            final ArrayList<String> randomSong = (ArrayList<String>) documentSnapshot.get("Canciones");
                            if(randomSong != null){
                                for(int i = 0; i < randomSong.size(); i++){
                                    Canciones cs  = new Canciones(randomSong.get(i),documentSnapshot.getId());
                                    prbcanciones.add(cs);
                                }
                            }
                        }
                    }
                    final int size = prbcanciones.size();
                    for (int i = 0; i < size; i++){
                        final Canciones can = prbcanciones.get(new Random().nextInt(size));
                        if(!songsSameByArtist.contains(can)){
                            if(songsSameByArtist.size() == 9){
                                break;
                            }
                            songsSameByArtist.add(can);
                        }
                    }
                    fourthAdapter.setCanciones(songsSameByArtist);
                    binding.fourthRow.setAdapter(fourthAdapter);
                }
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
    @Override
    public void onNoteClick(int position, String recycler) {
        String carpeta, cancion;
        switch (recycler) {
            case "Recommended":
                carpeta = canciones.get(position).getArtista();
                cancion = canciones.get(position).getNombre();
                sendData.getData(carpeta, cancion);
                break;
            case  "Favorite":
                carpeta = songLikesArrayList.get(position).get("Artista").toString();
                cancion = songLikesArrayList.get(position).get("Cancion").toString();
                index = position;
                sendData.getData(carpeta, cancion);
                break;
            case  "OthersSongsArtist":
                carpeta = songsSameByArtist.get(position).getArtista();
                cancion = songsSameByArtist.get(position).getNombre();
                sendData.getData(carpeta,cancion);
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