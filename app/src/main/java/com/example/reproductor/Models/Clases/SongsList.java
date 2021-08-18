package com.example.reproductor.Models.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class SongsList implements Serializable {
    private static SongsList instance;

    private ArrayList<SongModel> allSongDB;
    private ArrayList<SongModel> allSongsUser;
    private ArrayList<SongModel> recommendedSongs;
    private ArrayList<SongModel> randomOtherSongs;
    private ArrayList<SongModel> favoriteSongs;
    private ArrayList<SongModel> songsPlaying;
    private SongsList(){}

    public static SongsList getInstance(){
        if(instance == null){
            instance = new SongsList();
        }
        return instance;
    }

    public ArrayList<SongModel> getAllSongsUser() {
        return allSongsUser;
    }

    public void setAllSongsUser(ArrayList<SongModel> allSongsUser) {
        this.allSongsUser = allSongsUser;
    }

    public ArrayList<SongModel> getRandomOtherSongs() {
        return randomOtherSongs;
    }

    public void setRandomOtherSongs(ArrayList<SongModel> randomOtherSongs) {
        this.randomOtherSongs = randomOtherSongs;
    }

    public ArrayList<SongModel> getRecommendedSongs() {
        return recommendedSongs;
    }

    public void setRecommendedSongs(ArrayList<SongModel> recommendedSongs) {
        this.recommendedSongs = recommendedSongs;
    }

    public ArrayList<SongModel> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void setFavoriteSongs(ArrayList<SongModel> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }

    public ArrayList<SongModel> getSongsPlaying() {
        return songsPlaying;
    }

    public void setSongsPlaying(ArrayList<SongModel> songsPlaying) {
        this.songsPlaying = songsPlaying;
    }

    public ArrayList<SongModel> getAllSongDB() {
        return allSongDB;
    }

    public void setAllSongDB(ArrayList<SongModel> allSongDB) {
        this.allSongDB = allSongDB;
    }
}
