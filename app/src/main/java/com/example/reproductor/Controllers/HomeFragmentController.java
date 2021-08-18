package com.example.reproductor.Controllers;

import android.view.View;

import com.example.reproductor.Models.Adapters.HomeAdapter;
import com.example.reproductor.Models.Clases.ArtistModel;
import com.example.reproductor.Models.Clases.ArtistsList;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Models.Clases.SongsList;
import com.example.reproductor.Models.Clases.User;
import com.example.reproductor.databinding.HomeFragmentBinding;

import java.util.ArrayList;
import java.util.Random;

public class HomeFragmentController {
    private static HomeFragmentController instance;
    private final ArtistsList artistsList;
    private final User user;
    private final SongsList songsList;
    private ArrayList<SongModel> randomList, randomOtherSongList, songsRecommended;
    private HomeFragmentBinding vbHome;

    private HomeFragmentController(){
        this.artistsList = ArtistsList.getInstance();
        this.user = User.getInstance();
        this.songsList = SongsList.getInstance();
    }

    public void setBinding(HomeFragmentBinding vbHome){
        this.vbHome = vbHome;
    }

    public static HomeFragmentController getInstance(){
        if(instance == null){
            instance = new HomeFragmentController();
        }
        return instance;
    }

    public void loadAdapters(HomeAdapter rowAdapter, HomeAdapter recommendedAdapter,HomeAdapter adapter){
                    loadOtherSongByArtist(artistsList.getArtists(), rowAdapter);
                    loadRecommendedAdapter(songsList.getAllSongDB(),recommendedAdapter);
                    loadFavoriteAdapter(adapter);
    }

    public void loadRecommendedAdapter(ArrayList<SongModel> songModels, HomeAdapter adapter){
        if(songsRecommended == null){
            songsRecommended = new ArrayList<>();
        }else {
            songsRecommended.clear();
        }
        randomSong(songModels,songsRecommended,6);
        songsList.setRecommendedSongs(songsRecommended);
        adapter.setSongModels(songsRecommended);
    }

    public void loadFavoriteAdapter(HomeAdapter adapter){
        if(user.getSongLikes() != null){
            if(!user.getSongLikes().isEmpty()){
                vbHome.layoutFavorites.setVisibility(View.VISIBLE);
                if (randomList == null) {
                    randomList = new ArrayList<>();
                }else{
                    randomList.clear();
                }
                int size = user.getSongLikes().size();
                if(size > 6){
                    while (randomList.size() < 6) {
                        randomSong(user.getSongLikes(),randomList,6);
                    }
                }else{
                    randomList = new ArrayList<>(user.getSongLikes());
                }
                songsList.setFavoriteSongs(randomList);
                adapter.setSongModels(randomList);
            }else{
                vbHome.layoutFavorites.setVisibility(View.GONE);
            }
        }else{
            vbHome.layoutFavorites.setVisibility(View.GONE);
        }
    }


    public void loadOtherSongByArtist(ArrayList<ArtistModel> artistModels, HomeAdapter adapter){
        if(user.getSongLikes() != null){
            if(!user.getSongLikes().isEmpty()){
                vbHome.layoutOtherSongs.setVisibility(View.VISIBLE);
                if(randomOtherSongList == null){
                    randomOtherSongList = new ArrayList<>();
                }else{
                    randomOtherSongList.clear();
                }
                ArrayList<SongModel> songModels = new ArrayList<>();
                ArrayList<String> artist = new ArrayList<>();
                for (int i = 0; i < user.getSongLikes().size(); i++) {
                    String artista = user.getSongLikes().get(i).getArtista();
                    if(!artist.contains(artista)){
                        artist.add(artista);
                    }
                }
                for (int i = 0; i < artist.size(); i++) {
                    for (int j = 0; j < artistModels.size(); j++) {
                        if(artistModels.get(j).getNombre().equalsIgnoreCase(artist.get(i))){
                            songModels.addAll(artistModels.get(j).getCanciones());
                        }
                    }
                }
                randomSong(songModels,randomOtherSongList, 9);
                songsList.setRandomOtherSongs(randomOtherSongList);
                adapter.setSongModels(randomOtherSongList);
            }else{
                vbHome.layoutOtherSongs.setVisibility(View.GONE);
            }
        }else{
            vbHome.layoutOtherSongs.setVisibility(View.GONE);
        }
    }

    public void randomSong(ArrayList<SongModel> arrayList, ArrayList<SongModel> fillArray, int size){
        for (int i = 0; i < arrayList.size(); i++) {
            SongModel model = arrayList.get(new Random().nextInt(arrayList.size()));
            if(!fillArray.contains(model)){
                if(fillArray.size() == size){
                    return;
                }
                fillArray.add(model);
            }
        }
    }

}
