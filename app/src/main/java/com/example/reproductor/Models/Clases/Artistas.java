package com.example.reproductor.Models.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Artistas implements Serializable {
    private String Nombre;
    private String Descripcion;
    private ArrayList<String> Canciones;

    public Artistas() {
//        try{
//        json.put("Nombre",nombre);
//    }catch(JSONException e){
//        e.printStackTrace();
//    }
    }

    public Artistas(String nombre, String descripcion) {
        this.Nombre = nombre;
        this.Descripcion = descripcion;
    }

    public Artistas(String nombre, String descripcion, ArrayList<String> canciones) {
        Nombre = nombre;
        Descripcion = descripcion;
        Canciones = canciones;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public ArrayList<String> getCanciones() {
        return Canciones;
    }

    public void setCanciones(ArrayList<String> canciones) {
        Canciones = canciones;
    }
}
