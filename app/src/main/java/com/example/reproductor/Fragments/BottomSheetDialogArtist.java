package com.example.reproductor.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.reproductor.Activities.MainActivity;
import com.example.reproductor.Adapters.SongsArtistBottomSheet;
import com.example.reproductor.Clases.Artistas;
import com.example.reproductor.Interfaces.OpenBottomSheetArtist;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.Metrics;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.PruebaBottomSheetDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class BottomSheetDialogArtist extends BottomSheetDialogFragment implements OpenBottomSheetArtist {
    private ServicesFirebase services;
    private Artistas artist;
    private SendData sendData;
    private PruebaBottomSheetDialogBinding layoutBinding;
    private BottomSheetBehavior bottomSheetBehavior;

    public BottomSheetDialogArtist(Artistas artist) {
        this.artist = artist;
        this.services = new ServicesFirebase();
    }

    ImageButton closeButton;
    TextView nameArtist, descriptioArtist;
    ImageView imageArtist;
    RecyclerView recyclerArtist;

    public BottomSheetDialogArtist(Context context, int base_theme_myApp) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutBinding = PruebaBottomSheetDialogBinding.inflate(inflater, container, false);
        closeButton = layoutBinding.closeBtnArtist;
        nameArtist = layoutBinding.nameArtist;
        descriptioArtist = layoutBinding.descriptionArtist;
        imageArtist = layoutBinding.imgArtist;
        recyclerArtist = layoutBinding.recyclerSongsArtist;


        nameArtist.setText(artist.getNombre());
        descriptioArtist.setText(artist.getDescripcion());
        Object img = services.getImage(artist.getNombre());
        Glide.with(this).load(img).into(imageArtist);

        RecyclerView.LayoutManager linear = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerArtist.setLayoutManager(linear);

        SongsArtistBottomSheet songArtist = new SongsArtistBottomSheet(artist, this);
        recyclerArtist.setAdapter(songArtist);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return layoutBinding.getRoot();
    }


    @Override
    public void sendPositionArtist(int position) {
        sendData.getData(artist.getNombre(),artist.getCanciones().get(position));
        dismiss();
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


    public int getWindowsHeight(){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


}
