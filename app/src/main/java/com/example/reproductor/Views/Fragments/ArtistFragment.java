package com.example.reproductor.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Models.Adapters.ArtistAdapter;
import com.example.reproductor.Models.Clases.ArtistsList;
import com.example.reproductor.Interfaces.OpenBottomSheetArtist;
import com.example.reproductor.databinding.FragmentArtistBinding;

public class ArtistFragment extends Fragment implements OpenBottomSheetArtist {
    private FragmentArtistBinding binding;
    private ArtistsList artistas;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentArtistBinding.inflate(inflater, container, false);

        artistas = ArtistsList.getInstance();

        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.reciclerArtist.setLayoutManager(linearLayout);

        ArtistAdapter artistAdapter = new ArtistAdapter(artistas.getArtists(), this);
        binding.reciclerArtist.setAdapter(artistAdapter);

        return binding.getRoot();
    }


    @Override
    public void sendPositionArtist(int position) {
        BottomSheetDialogArtist prb = new BottomSheetDialogArtist(artistas.getArtists().get(position));
        prb.show(getParentFragmentManager(),"probando");
//        Log.e("TAG", "sendPositionArtist: " + singleton.getCanciones().getClass().getName());

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}