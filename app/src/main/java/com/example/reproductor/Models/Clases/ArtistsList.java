package com.example.reproductor.Models.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtistsList implements Serializable {
    private static ArtistsList instance;
    private ArrayList<ArtistModel> artists;
    private ArtistsList(){
    }

    public static ArtistsList getInstance(){
        if(instance == null){
            instance = new ArtistsList();
        }
        return instance;
    }

    public ArrayList<ArtistModel> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<ArtistModel> artists) {
        this.artists = artists;
    }
}
