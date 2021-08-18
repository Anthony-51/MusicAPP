package com.example.reproductor.Interfaces;

import com.example.reproductor.Models.Clases.SongModel;

import java.util.ArrayList;

public interface SendData {
   void getData(SongModel song, ArrayList<SongModel> modelArrayList);
}
