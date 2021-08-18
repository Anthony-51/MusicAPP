package com.example.reproductor.Models.Clases;

import java.io.Serializable;

public class SongModel implements Serializable {
    private String cancion;
    private String artista;
    private String search;
    private String urlSong;
    private String urlImg;

    public SongModel() {
    }
    public SongModel(String artista, String cancion){
        this.artista = artista;
        this.cancion = cancion;
    }

    public SongModel(String cancion, String artista, String search) {
        this.cancion = cancion;
        this.artista = artista;
        this.search = search;
    }

    public SongModel(String cancion, String artista, String search, String urlSong, String urlImg) {
        this.cancion = cancion;
        this.artista = artista;
        this.search = search;
        this.urlSong = urlSong;
        this.urlImg = urlImg;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUrlSong() {
        return urlSong;
    }

    public void setUrlSong(String urlSong) {
        this.urlSong = urlSong;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
