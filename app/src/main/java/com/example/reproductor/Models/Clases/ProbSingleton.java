package com.example.reproductor.Models.Clases;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reproductor.Views.Activities.MainActivity;
import com.example.reproductor.Models.Adapters.ArtistAdapter;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class ProbSingleton {
    private static ProbSingleton instance;
    private String text;
    private ArrayList<Canciones> canciones;
    private ArrayList<Canciones> randomSong;
    private ServicesFirebase service;
    private ArrayList<String> artistas;
    private ProbSingleton(){
        service = new ServicesFirebase();
        canciones = new ArrayList<>();
        randomSong = new ArrayList<>();
        artistas = new ArrayList<>();
    }
    public static ProbSingleton getInstance(){
        if(instance == null){
            instance = new ProbSingleton();
        }
        return instance;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }

    public synchronized void loadCanciones(Fragment fragment, MainActivity activity, ProgressBar progressBar){
        service.getFirebaseFirestore().collection("artistas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    canciones.clear();
                    for (DocumentSnapshot document : task.getResult()){
                        artistas.add(document.getId());
                    }
                    Log.e("TAG", "onComplete: datos cargados" );
                    progressBar.setVisibility(View.GONE);
                    changeFragments(fragment,activity);
                }
            }
        });
    }
    public synchronized void prbUser(User user2){

        service.getFirebaseFirestore().collection("users").document(service.getFirebaseAuth().getCurrentUser()
                .getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        User user = document.toObject(User.class);
                        if(user != null){
                            user2.setUser(user);
                        }
//                        user.setEmail(user1.getEmail());
                        Log.e("TAG", "onComplete: " + user.getUserName());
                    }
                }
            }
        });
    }
    public void tiempoReal(){
        service.getFirebaseFirestore().collection("artistas").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "Listen failed.", error);
                    return;
                }
                if(value != null){
                    for (DocumentSnapshot document : value.getDocuments()){
                        Log.e("TAG", "onEvent: " + document.getId());
                    }
                    notify();
                }
            }
        });
    }
    public void prb(ArtistAdapter artistAdapter){
        artistAdapter.notifyDataSetChanged();
    }

    public ArrayList<Canciones> getCanciones() {
        return canciones;
    }

    public void randomSongs(){
        if(!canciones.isEmpty()){
            randomSong.clear();
            for(int i = 0; i < canciones.size(); i++){
                Canciones cs = canciones.get(new Random().nextInt(canciones.size()));
                if(!randomSong.contains(cs)){
                    randomSong.add(cs);
                }
            }
        }
    }

    public void setCanciones(ArrayList<Canciones> canciones) {
        this.canciones = canciones;
    }

    public ArrayList<Canciones> getRandomSong() {
        return randomSong;
    }

    public void setRandomSong(ArrayList<Canciones> randomSong) {
        this.randomSong = randomSong;
    }

    public ArrayList<String> getArtistas() {
        return artistas;
    }

    public void setArtistas(ArrayList<String> artistas) {
        this.artistas = artistas;
    }

    public void changeFragments(Fragment fragment, MainActivity activity){
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }
}
