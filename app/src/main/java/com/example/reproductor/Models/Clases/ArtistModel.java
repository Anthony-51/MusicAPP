package com.example.reproductor.Models.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtistModel implements Serializable {
    private String nombre, descripcion;
    private ArrayList<SongModel> canciones;
    public ArtistModel(){}

    public ArtistModel(String nombre, String descripcion, ArrayList<SongModel> canciones) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.canciones = canciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<SongModel> getCanciones() {
        return canciones;
    }

    public void setCanciones(ArrayList<SongModel> canciones) {
        this.canciones = canciones;
    }
}
