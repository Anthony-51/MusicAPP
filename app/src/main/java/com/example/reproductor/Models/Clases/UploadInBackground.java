package com.example.reproductor.Models.Clases;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.reproductor.Views.Activities.MainActivity;
import com.example.reproductor.Services.ServicesFirebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadInBackground extends AsyncTask<String, Integer, String> {
    private final Uri[] uri;
    private int cont = 0;
    private final ArtistsList artistsList = ArtistsList.getInstance();
    private final User user = User.getInstance();
    private ArtistModel aModel;
    private final SongModel songUplBackground;
    private final ServicesFirebase service = new ServicesFirebase();
    public UploadInBackground(Uri[] uri, SongModel model) {
        this.uri = new Uri[2];
        this.uri[0] = uri[0];
        this.uri[1] = uri[1];
        this.songUplBackground = model;
    }

    @Override
    protected String doInBackground(String... strings) {
        loadMusic();
        return songUplBackground.getCancion();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(MainActivity.context,"Subiendo " + s,Toast.LENGTH_SHORT).show();
    }

    private void loadMusic(){
        String carpeta = null;
        if(cont < uri.length){
            int var = cont;
            if(uri[cont] != null){
                if(cont == 0){
                    carpeta = "img_song";
                }else{
                    carpeta = "music";
                }
                StorageReference filePath = service.getStorageReference().child(carpeta).child(System.currentTimeMillis() + getFileExtension(uri[cont]));
                UploadTask uploadTask = filePath.putFile(uri[cont]);
                Task<Uri> urlTask = uploadTask.continueWithTask(task -> filePath.getDownloadUrl()).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Uri dowloadUri = task.getResult();
                                if(dowloadUri != null){
                                    if(cont == 0){
                                        songUplBackground.setUrlImg(dowloadUri.toString());
                                    }else{
                                        songUplBackground.setUrlSong(dowloadUri.toString());
                                    }
                                }
                                cont++;
                                loadMusic();
                            }
                        }
                );
            }
            if(uri[var] == null){
                cont++;
            }
        }else{
            saveFirebase();
        }
    }
    private void saveFirebase(){
        service.getFirebaseFirestore().collection("canciones").document(songUplBackground.getCancion()).set(songUplBackground);
        if(findArtist(songUplBackground.getArtista())){
            aModel.getCanciones().add(songUplBackground);
            service.getFirebaseFirestore().collection("artistas").document(aModel.getNombre()).update("canciones", aModel.getCanciones());
        }else{
            ArrayList<SongModel> songs = new ArrayList<>();
            songs.add(songUplBackground);
            aModel = new ArtistModel(songUplBackground.getArtista(),"Sin descripcion.",songs);
            service.getFirebaseFirestore().collection("artistas").document(aModel.getNombre()).set(aModel);
        }
        if(user.getSongsUploaded()!=null){
            user.getSongsUploaded().add(songUplBackground);
            service.getFirebaseFirestore().collection("users").document(user.getEmail()).update("songsUploaded",user.getSongsUploaded());
        }else{
            ArrayList<SongModel> songUpl = new ArrayList<>();
            songUpl.add(songUplBackground);
            service.getFirebaseFirestore().collection("users").document(user.getEmail()).update("songsUploaded",songUpl);
        }
        Toast.makeText(MainActivity.context,"Se Termino de subir " + songUplBackground.getCancion(), Toast.LENGTH_SHORT).show();
    }

    private boolean findArtist(String s){
        for(int i = 0; i < artistsList.getArtists().size(); i++){
            String cad = artistsList.getArtists().get(i).getNombre();
            if(s.equalsIgnoreCase(cad)){
                aModel = artistsList.getArtists().get(i);
                return true;
            }
        }
        aModel = null;
        return false;
    }
    private String getFileExtension(Uri uri){
        ContentResolver resolver = MainActivity.context.getContentResolver();
        MimeTypeMap mime  = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(resolver.getType(uri));
    }
}
