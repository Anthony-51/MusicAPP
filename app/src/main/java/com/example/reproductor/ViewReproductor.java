package com.example.reproductor;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityViewReproductorBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class ViewReproductor extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private ActivityViewReproductorBinding reproductorBinding;
    private ServicesFirebase services;
    private ArrayList<Canciones> canciones;
    private MediaPlayer mediaPlayer;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reproductorBinding = ActivityViewReproductorBinding.inflate(getLayoutInflater());
        setContentView(reproductorBinding.getRoot());
        services = new ServicesFirebase();
        getDatos();
        fetchMusicFromFirebase();
        Object img = services.getImage(canciones.get(index).getArtista(),canciones.get(index).getNombre());

        GlideApp.with(this).load(img).into(reproductorBinding.imgReproductor);
        reproductorBinding.nameReproductor.setText(canciones.get(index).getNombre());


        reproductorBinding.replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reproductorBinding.replay.isSelected()){
                    reproductorBinding.replay.setSelected(false);

                }else{
                    reproductorBinding.replay.setSelected(true);
                }
            }
        });
        reproductorBinding.shufle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reproductorBinding.shufle.isSelected()){
                    reproductorBinding.shufle.setSelected(false);

                }else{
                    reproductorBinding.shufle.setSelected(true);
                }
            }
        });
        }
    void getDatos(){
        canciones = (ArrayList<Canciones>)getIntent().getSerializableExtra("ArrayCanciones");
        index = getIntent().getIntExtra("Index",0);
    }
    public void nextSong(View view){
        index++;
        if(index > canciones.size()-1){
            Toast.makeText(this,"No hay mas canciones",Toast.LENGTH_SHORT).show();
            index--;
        }else{
            Object img = services.getImage(canciones.get(index).getArtista(),canciones.get(index).getNombre());

            GlideApp.with(this).load(img).into(reproductorBinding.imgReproductor);
            reproductorBinding.nameReproductor.setText(canciones.get(index).getNombre());
        }
    }
    public void previousSong(View view){
        index--;
        if(index < 0){
            Toast.makeText(this,"No hay mas canciones",Toast.LENGTH_SHORT).show();
            index++;
        }else{
            Object img = services.getImage(canciones.get(index).getArtista(),canciones.get(index).getNombre());

            GlideApp.with(this).load(img).into(reproductorBinding.imgReproductor);
            reproductorBinding.nameReproductor.setText(canciones.get(index).getNombre());
        }
    }
    void fetchMusicFromFirebase(){
        String cancion = canciones.get(index).getNombre();
        String artista = canciones.get(index).getArtista();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        StorageReference ref = services.getStorageReference().child(artista+" Music/"+cancion+".mp3");
        if(services.getStorageReference() == null){
            Toast.makeText(getApplicationContext(),"Error al encontrar databse",Toast.LENGTH_SHORT).show();
        }
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    //dowload url or file
                    final String url = uri.toString();
                    mediaPlayer.setDataSource(url);
                    // wait for mediaplayer to get prepare
                    mediaPlayer.setOnPreparedListener(ViewReproductor.this);
                    mediaPlayer.prepareAsync();
                    onPrepared(mediaPlayer);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}