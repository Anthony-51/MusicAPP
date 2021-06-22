package com.example.reproductor.Fragments;

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

import com.bumptech.glide.Glide;
import com.example.reproductor.Activities.MainActivity;
import com.example.reproductor.Adapters.firstRowAdapter;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.example.reproductor.databinding.HomeFragmentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MediaPlayer.OnPreparedListener, firstRowAdapter.OnNoteListener {
    private ServicesFirebase services;
    private ActivityMainBinding activityMainBinding;
    private BottomSheetBehavior behavior;
    public View bottom;
    public static HomeFragmentBinding binding;
    private RecyclerView.LayoutManager firstGridLayout,secondGridLayout, thirdGridLayout,fourthGridLayout;
    private firstRowAdapter rowAdapter,secondAdapter,thirdAdapter,fourthAdapter;
    private MediaPlayer mediaPlayer;
    public static ArrayList<Canciones> canciones;
    private Animation animFadeIn, animFadeOut;
    ViewGroup.LayoutParams params;
    int index,height;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        services = new ServicesFirebase();
        canciones = new ArrayList<>();
        activityMainBinding = MainActivity.binding;
        setLayouts();
        getDatos();
        animFadeIn = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
        //BOTTOM SHEET
        CoordinatorLayout layout = (CoordinatorLayout) binding.homeContent;
        bottom = layout.findViewById(R.id.bottomsheet);
        behavior = BottomSheetBehavior.from(bottom);
        params = (ViewGroup.LayoutParams) binding.bottomsheet.imgReproductor.getLayoutParams();
        height = binding.bottomsheet.imgReproductor.getLayoutParams().height;

        rowAdapter = new firstRowAdapter(canciones,this);
        rowAdapter.notifyDataSetChanged();
        secondAdapter = new firstRowAdapter();
        thirdAdapter = new firstRowAdapter();
        fourthAdapter = new firstRowAdapter();

        binding.firstRown.setAdapter(rowAdapter);
        binding.secondRow.setAdapter(secondAdapter);
        binding.thirdRow.setAdapter(thirdAdapter);
        binding.fourthRow.setAdapter(fourthAdapter);
        stateBottomSheet();

        getUser();
        binding.bottomsheet.play.setOnClickListener(this::musicControl);
        binding.bottomsheet.next.setOnClickListener(this::nextSong);
        binding.bottomsheet.previous.setOnClickListener(this::previousSong);
        binding.bottomsheet.replay.setOnClickListener(this::replaySong);
        binding.bottomsheet.playCollapse.setOnClickListener(this::musicControl);
        binding.bottomsheet.nextCollapse.setOnClickListener(this::nextSong);
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
        services.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(final DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String nombre = dataSnapshot.getKey();
                    if(!nombre.equals("users")){
                        services.getDatabaseReference().child(nombre).child("Canciones").limitToFirst(3).addValueEventListener(new ValueEventListener() {
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

    public void stateBottomSheet(){
        binding.bottomsheet.layoutCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        behavior.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        activityMainBinding.buttonNav.setVisibility(View.VISIBLE);
                        activityMainBinding.toolbar.setVisibility(View.VISIBLE);
                        binding.bottomsheet.layoutCollapse.setVisibility(View.VISIBLE);
                        activityMainBinding.container.setPadding(0,0,0,100);
                        params.height =  binding.bottomsheet.imgReproductor.getMinimumHeight();
                        params.width = binding.bottomsheet.imgReproductor.getMinimumHeight();
                        binding.bottomsheet.imgReproductor.setLayoutParams(params);
                        binding.bottomsheet.imgReproductor.setTranslationY(-(float) (binding.bottomsheet.getRoot().getHeight() *0.12));
                        binding.bottomsheet.imgReproductor.setTranslationX(-(float) (binding.bottomsheet.getRoot().getRight() * 0.4));
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        binding.bottomsheet.layoutCollapse.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        activityMainBinding.buttonNav.setVisibility(View.INVISIBLE);
                        activityMainBinding.toolbar.setVisibility(View.GONE);
                        binding.bottomsheet.layoutCollapse.setVisibility(View.INVISIBLE);
                        activityMainBinding.container.setPadding(0,0,0,0);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        activityMainBinding.buttonNav.setVisibility(View.INVISIBLE);
                        activityMainBinding.toolbar.setVisibility(View.GONE);
                        activityMainBinding.container.setPadding(0,0,0,0);
                        mediaPlayer.reset();
                        Log.e("TAG","HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float var = (float) (450 - bottomSheet.getTop()) / 450;
                float var2 = (float) (bottomSheet.getTop() - (bottomSheet.getHeight() * 0.8)) / 100;
                binding.bottomsheet.layoutExpand.setAlpha(var);
                binding.bottomsheet.layoutCollapse.setAlpha(var2);
                int right = binding.bottomsheet.getRoot().getRight();
                int bottom = binding.bottomsheet.getRoot().getHeight();
                if(bottomSheet.getTop() > 400){

                    int med = height - ((binding.bottomsheet.getRoot().getTop() - 400) / 2);
                    if( med > binding.bottomsheet.imgReproductor.getMinimumHeight()){
                        params.height =  med;
                        params.width = med;
                        int hInner = binding.bottomsheet.getRoot().getTop() - 400; //height vuelve a comenzar de 0
                        if(hInner < (right * 0.4)){
                            binding.bottomsheet.imgReproductor.setTranslationX(-(float) (hInner));
                            if(hInner < (bottom * 0.12)){
                                binding.bottomsheet.imgReproductor.setTranslationY(-(float) (hInner));
                            }else{
                                binding.bottomsheet.imgReproductor.setTranslationY(-(float) (bottom * 0.12));
                            }
                        }else{
                            binding.bottomsheet.imgReproductor.setTranslationX(-(float) (right * 0.4));
                        }
                    }
                }else {
                    params.height =  height;
                    params.width = height;
                    binding.bottomsheet.imgReproductor.setTranslationY(0);
                    binding.bottomsheet.imgReproductor.setTranslationX(0);
                }
                binding.bottomsheet.imgReproductor.setLayoutParams(params);
            }
        });
    }
    void fetchMusicFromFirebase(String artista, String cancion){
        if(mediaPlayer != null){
            mediaPlayer.reset();
            StorageReference ref = services.getStorageReference().child(artista+" Music/"+cancion+".mp3");
            if(services.getStorageReference() == null){
                Toast.makeText(getContext(),"Error al encontrar databse",Toast.LENGTH_SHORT).show();
            }
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try{
                        final String url = uri.toString();
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.setOnPreparedListener(HomeFragment.this);
                        mediaPlayer.prepareAsync();
                        onPrepared(mediaPlayer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("TAG", e.getMessage());
                }
            });
        }
        else{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            StorageReference ref = services.getStorageReference().child(artista+" Music/"+cancion+".mp3");
            if(services.getStorageReference() == null){
                Toast.makeText(getContext(),"Error al encontrar databse",Toast.LENGTH_SHORT).show();
            }
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        //dowload url or file
                        final String url = uri.toString();
                        mediaPlayer.setDataSource(url);
                        // wait for mediaplayer to get prepare
                        mediaPlayer.setOnPreparedListener(HomeFragment.this);
                        mediaPlayer.prepareAsync();
                        onPrepared(mediaPlayer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("TAG", e.getMessage());
                }
            });
        }

    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onNoteClick(int position) {
        String carpeta = canciones.get(position).getArtista();
        String cancion = canciones.get(position).getNombre();
        Object img = services.getImage(carpeta,cancion);
        Glide.with(getContext()).load(img).into(binding.bottomsheet.imgReproductor);
        binding.bottomsheet.nameReproductor.setText(cancion);
        binding.bottomsheet.nameReproductorCollapse.setText(cancion);
        bottom.setVisibility(View.VISIBLE);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        fetchMusicFromFirebase(carpeta,cancion);
        index = position;
    }

    public void musicControl(View view){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            binding.bottomsheet.play.startAnimation(animFadeOut);
            binding.bottomsheet.play.setImageResource(R.drawable.ic_play);
            binding.bottomsheet.play.startAnimation(animFadeIn);
        }else{
            mediaPlayer.start();
            binding.bottomsheet.play.startAnimation(animFadeOut);
            binding.bottomsheet.play.setImageResource(R.drawable.ic_pause);
            binding.bottomsheet.play.startAnimation(animFadeIn);
        }
    }


    public void nextSong(View view){
        index++;
        if(index > canciones.size()-1){
            Toast.makeText(getContext(),"No hay mas canciones",Toast.LENGTH_SHORT).show();
            index--;
        }else{
            String carpeta = canciones.get(index).getArtista();
            String cancion = canciones.get(index).getNombre();
            Object img = services.getImage(canciones.get(index).getArtista(),canciones.get(index).getNombre());

            Glide.with(this).load(img).into(binding.bottomsheet.imgReproductor);
            binding.bottomsheet.nameReproductor.setText(cancion);
            binding.bottomsheet.nameReproductorCollapse.setText(cancion);
            fetchMusicFromFirebase(carpeta,cancion);
        }
    }
    public void previousSong(View view){
        index--;
        if(index < 0){
            Toast.makeText(getContext(),"No hay mas canciones",Toast.LENGTH_SHORT).show();
            index++;
        }else{
            String carpeta = canciones.get(index).getArtista();
            String cancion = canciones.get(index).getNombre();
            Object img = services.getImage(canciones.get(index).getArtista(),canciones.get(index).getNombre());

            Glide.with(this).load(img).into(binding.bottomsheet.imgReproductor);
            binding.bottomsheet.nameReproductor.setText(cancion);
            binding.bottomsheet.nameReproductorCollapse.setText(cancion);
            fetchMusicFromFirebase(carpeta,cancion);
        }
    }

    public void replaySong(View view){
        if(binding.bottomsheet.replay.isSelected()){
            binding.bottomsheet.replay.setSelected(false);
            mediaPlayer.setLooping(false);
        }else{
            binding.bottomsheet.replay.setSelected(true);
            mediaPlayer.setLooping(true);
        }
    }
}