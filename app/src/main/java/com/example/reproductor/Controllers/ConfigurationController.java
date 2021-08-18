package com.example.reproductor.Controllers;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.reproductor.Views.Activities.ConfigurationActivity;
import com.example.reproductor.Models.Clases.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ConfigurationController {
    private StorageReference storage;
    private FirebaseFirestore firestore;
    private int cont = 0;
    private Uri uri[] = new Uri[2];
    private User user;
    private ConfigurationActivity configuration;

    public ConfigurationController(ConfigurationActivity configuration){
        this.storage = FirebaseStorage.getInstance().getReference();
        this.firestore = FirebaseFirestore.getInstance();
        this.user = User.getInstance();
        this.configuration = configuration;
    }

    public void updateUserProfile(){

    }

    public void updateUserAccount(){}

    private void loadImage(ImageView image){
        String nameImage;
        if(cont < uri.length){
            int var = cont;
            if(uri[cont] != null){
                if(cont == 0){
                    nameImage = "imgProfile";
                }else{
                    nameImage = "imgCover";
                }
                StorageReference filePath = storage.child("users").child(user.getEmail()).child(nameImage);
                UploadTask uploadTask = filePath.putFile(uri[cont]);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        //Continue with the task to get the download url
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri dowloadUri = task.getResult();
                            if(nameImage.equalsIgnoreCase("imgProfile")){
                                Glide.with(configuration.getApplicationContext()).load(dowloadUri).into(image);
                            }else
                            {
                                Glide.with(configuration.getApplicationContext()).load(dowloadUri).into(image);
                            }
                            firestore.collection("users").document(user.getEmail()).update(nameImage, String.valueOf(dowloadUri));
                            cont++;
                            loadImage(image);
                        }
                    }
                });
            }
            if(uri[var] == null){
                cont++;
            }
        }else{
            cont = 0;
            uri[0] = null;
            uri[1] = null;
        }
    }
}
