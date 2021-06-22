package com.example.reproductor.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.Adapters.ArtistRecommendedAdapter;
import com.example.reproductor.Adapters.FavoriteAdapter;
import com.example.reproductor.Adapters.SongHomeAdapter;
import com.example.reproductor.Adapters.firstRowAdapter;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.example.reproductor.databinding.HomeFragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gridFragmentRecycler extends Fragment {
    private BottomSheetBehavior behavior;
    private View bottom;
    private HomeFragmentBinding binding;
    private RecyclerView.LayoutManager firstGridLayout,secondGridLayout, thirdGridLayout,fourthGridLayout;
    private firstRowAdapter rowAdapter;
    private FavoriteAdapter favoriteAdapter;
    private ArtistRecommendedAdapter artistRecommended;
    private SongHomeAdapter songAdapter;
    private ServicesFirebase ser;
    public static ArrayList<Canciones> canciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        ser = new ServicesFirebase();
        canciones = new ArrayList<>();
        setLayouts();
        getDatos();
        //BOTTOM SHEET
        CoordinatorLayout layout = (CoordinatorLayout) binding.homeContent;
        bottom = layout.findViewById(R.id.bottomsheet);
        behavior = BottomSheetBehavior.from(bottom);

        rowAdapter = new firstRowAdapter(canciones,getActivity());
        rowAdapter.notifyDataSetChanged();
        favoriteAdapter = new FavoriteAdapter();
        artistRecommended = new ArtistRecommendedAdapter();
        songAdapter = new SongHomeAdapter();
        binding.firstRown.setAdapter(rowAdapter);
        binding.secondRow.setAdapter(favoriteAdapter);
        binding.thirdRow.setAdapter(artistRecommended);
        binding.fourthRow.setAdapter(songAdapter);
        stateBottomSheet();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG","destruido");
    }

    public void stateBottomSheet(){
        behavior.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("TAG","collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("TAG","dragging");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("TAG","EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("TAG","HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

}