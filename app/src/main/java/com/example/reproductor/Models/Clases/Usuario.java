package com.example.reproductor.Models.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Usuario implements Serializable, Comparable<Usuario> {
    private String email, password, userName, imgPerfil, imgFondo;
    private ArrayList<HashMap<String, Object>> cancionesRecientes;
    private ArrayList<HashMap<String, Object>> songLikes;

    public Usuario() {
    }

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Usuario(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    public Usuario(String email, String password, String userName, String imgPerfil, String imgFondo) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.imgPerfil = imgPerfil;
        this.imgFondo = imgFondo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public String getImgFondo() {
        return imgFondo;
    }

    public void setImgFondo(String imgFondo) {
        this.imgFondo = imgFondo;
    }

    public ArrayList<HashMap<String, Object>> getCancionesRecientes() {
        return cancionesRecientes;
    }

    public void setCancionesRecientes(ArrayList<HashMap<String, Object>> cancionesRecientes) {
        this.cancionesRecientes = cancionesRecientes;
    }

    public ArrayList<HashMap<String, Object>> getSongLikes() {
        return songLikes;
    }

    public void setSongLikes(ArrayList<HashMap<String, Object>> songLikes) {
        this.songLikes = songLikes;
    }

    @Override
    public int compareTo(Usuario o) {
        return 0;
    }
}
