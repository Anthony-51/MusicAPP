package com.example.reproductor.Models.Clases;


import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private static User instance;
    private String email, imgCover, imgProfile, password, userName;
    private ArrayList<SongModel> cancionesRecientes, songLikes, songsUploaded;
    private User(){
    }

    public static User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgCover() {
        return imgCover;
    }

    public void setImgCover(String imgCover) {
        this.imgCover = imgCover;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
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

    public ArrayList<SongModel> getCancionesRecientes() {
        return cancionesRecientes;
    }

    public void setCancionesRecientes(ArrayList<SongModel> cancionesRecientes) {
        this.cancionesRecientes = cancionesRecientes;
    }

    public ArrayList<SongModel> getSongLikes() {
        return songLikes;
    }

    public void setSongLikes(ArrayList<SongModel> songLikes) {
        this.songLikes = songLikes;
    }

    public ArrayList<SongModel> getSongsUploaded() {
        return songsUploaded;
    }

    public void setSongsUploaded(ArrayList<SongModel> songsUploaded) {
        this.songsUploaded = songsUploaded;
    }

    public void setUser(User user){
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.imgProfile = user.getImgProfile();
        this.imgCover = user.getImgCover();
        this.cancionesRecientes = user.getCancionesRecientes();
        this.songLikes = user.getSongLikes();
        this.songsUploaded = user.getSongsUploaded();
    }

}
