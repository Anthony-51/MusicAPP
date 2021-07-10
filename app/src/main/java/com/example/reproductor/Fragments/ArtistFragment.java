package com.example.reproductor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.reproductor.Adapters.ArtistAdapter;
import com.example.reproductor.Clases.Artistas;
import com.example.reproductor.Interfaces.OpenBottomSheetArtist;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.FragmentArtistBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ArtistFragment extends Fragment implements OpenBottomSheetArtist {
    private FragmentArtistBinding binding;
    private ArtistAdapter artistAdapter;
    private ServicesFirebase service;
    private ArrayList<Artistas> artistas;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentArtistBinding.inflate(inflater, container, false);

        service = new ServicesFirebase();
        artistas = new ArrayList<>();




        loadData();
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.reciclerArtist.setLayoutManager(linearLayout);

        artistAdapter = new ArtistAdapter(artistas,this);
        binding.reciclerArtist.setAdapter(artistAdapter);

        return binding.getRoot();
    }

    void loadData(){
        service.getFirebaseFirestore().collection("artistas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        ArrayList<String> song = (ArrayList<String>) document.get("Canciones");
                        Artistas at =  new Artistas(document.getId(),document.get("Descripcion").toString(),song);
                        artistas.add(at);
                    }
                    artistAdapter.setArtist(artistas);
                    binding.reciclerArtist.setAdapter(artistAdapter);
                }
            }
        });
    }

    @Override
    public void sendPositionArtist(int position) {
        BottomSheetDialogArtist prb = new BottomSheetDialogArtist(artistas.get(position));
        prb.show(getParentFragmentManager(),"probando");

//        Log.e("TAG", "sendPositionArtist: " + position );
//        if(artistas != null){
//            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), com.example.reproductor.R.style.Base_Theme_MyApp);
//            View bottomSheetView = LayoutInflater.from(getContext()).inflate(com.example.reproductor.R.layout.prueba_bottom_sheet_dialog,
//                    (RelativeLayout)getView().findViewById(com.example.reproductor.R.id.button_sheet_container));
//            bottomSheetView.findViewById(com.example.reproductor.R.id.close_btn_artist).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    bottomSheetDialog.dismiss();
//                }
//            });
//            bottomSheetDialog.setContentView(bottomSheetView);
//            bottomSheetDialog.show();
//        }

    }
}