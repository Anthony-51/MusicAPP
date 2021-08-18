package com.example.reproductor.Views.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reproductor.Models.Adapters.ArtistHomeAdapter;
import com.example.reproductor.Models.Adapters.HomeAdapter;
import com.example.reproductor.Models.Clases.ArtistsList;
import com.example.reproductor.Models.Clases.SongsList;
import com.example.reproductor.Controllers.HomeFragmentController;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment implements OnNoteListener {
    private HomeFragmentBinding binding;
    SendData sendData;
    private final String TAG = "Home Fragment";
    private SongsList songsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);

        setLayouts();

        HomeFragmentController controller = HomeFragmentController.getInstance();
        controller.setBinding(binding);
        ArtistsList artist = ArtistsList.getInstance();
        songsList = SongsList.getInstance();

        HomeAdapter rowAdapter = new HomeAdapter(this, null, "Recommended");
        HomeAdapter secondAdapter = new HomeAdapter(this,null,"Favorite");
        ArtistHomeAdapter artistHomeAdapter = new ArtistHomeAdapter(artist.getArtists());
        HomeAdapter fourthAdapter = new HomeAdapter(this, null,"OthersSongsArtist");


        binding.firstRown.setAdapter(rowAdapter);
        binding.secondRow.setAdapter(secondAdapter);
        binding.thirdRow.setAdapter(artistHomeAdapter);
        binding.fourthRow.setAdapter(fourthAdapter);

        controller.loadAdapters(fourthAdapter, rowAdapter, secondAdapter);
        binding.homeContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(artist.getArtists() != null && songsList != null){
                    controller.loadAdapters(fourthAdapter,rowAdapter,secondAdapter);
                }
                binding.homeContent.setRefreshing(false);
            }
        });

        return binding.getRoot();
    }


    void setLayouts(){
        RecyclerView.LayoutManager firstGridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager secondGridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager thirdGridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager fourthGridLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        binding.firstRown.setLayoutManager(firstGridLayout);
        binding.secondRow.setLayoutManager(secondGridLayout);
        binding.thirdRow.setLayoutManager(thirdGridLayout);
        binding.fourthRow.setLayoutManager(fourthGridLayout);
    }
    @Override
    public void onNoteClick(int position, String recycler) {
        switch (recycler) {
            case "Recommended":
                sendData.getData(songsList.getRecommendedSongs().get(position), songsList.getRecommendedSongs());
                break;
            case  "Favorite":
                sendData.getData(songsList.getFavoriteSongs().get(position),songsList.getFavoriteSongs());
                break;
            case  "OthersSongsArtist":
                sendData.getData(songsList.getRandomOtherSongs().get(position), songsList.getRandomOtherSongs());
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