package com.example.reproductor.Clases;

import java.io.Serializable;

public class Canciones implements Serializable {
    private String Nombre;
    private String artista;
    public Canciones() {
    }
    public Canciones(String nombre) {
        this.Nombre = nombre;
    }
    public Canciones(String nombre, String artista) {
        Nombre = nombre;
        this.artista = artista;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }



    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }
}
