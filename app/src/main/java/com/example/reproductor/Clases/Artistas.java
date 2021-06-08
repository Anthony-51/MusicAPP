package com.example.reproductor.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Artistas implements Serializable {
    private String Nombre;
    private String Descripcion;



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

}
