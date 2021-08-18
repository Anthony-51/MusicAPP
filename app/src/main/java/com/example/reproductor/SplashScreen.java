package com.example.reproductor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.reproductor.Views.Activities.LoginActivity;
import com.example.reproductor.Views.Activities.MainActivity;
import com.example.reproductor.Services.ServicesFirebase;

public class SplashScreen extends AppCompatActivity {
    private ServicesFirebase service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesFirebase();
        Intent intent;
        if(service.getFirebaseAuth().getCurrentUser() != null){
            intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            finish();
        }else{
            intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        }
    }
}