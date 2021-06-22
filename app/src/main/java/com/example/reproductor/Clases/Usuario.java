package com.example.reproductor.Clases;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String email, password, userName, imgPerfil, imgFondo;

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
}
