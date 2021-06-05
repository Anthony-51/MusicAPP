package com.example.reproductor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.reproductor.databinding.ActivityViewReproductorBinding;

public class ViewReproductor extends AppCompatActivity {
    private ActivityViewReproductorBinding reproductorBinding;
    private String nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reproductorBinding = ActivityViewReproductorBinding.inflate(getLayoutInflater());
        setContentView(reproductorBinding.getRoot());
        getDatos();


    }

    void getDatos(){
        nom = getIntent().getStringExtra("name");
    }
}