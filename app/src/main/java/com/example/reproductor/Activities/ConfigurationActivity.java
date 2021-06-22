package com.example.reproductor.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Usuario;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityConfigurationBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ConfigurationActivity extends AppCompatActivity {
    ActivityConfigurationBinding configurationBinding;
    private String userName, imgUser, bgImgUser, email, image;
    private ServicesFirebase service;
    private Usuario user;
    private static final int GALLERY_INTENT = 1;//buscar significado, infiero que es el numero de archivos que se podra seleccioanr de la galeria
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configurationBinding = ActivityConfigurationBinding.inflate(getLayoutInflater());
        setContentView(configurationBinding.getRoot());
        service = new ServicesFirebase();
        user = new Usuario();
        email = getIntent().getStringExtra("email");
        getUser();
        Buttons();
    }

    private void getUser(){
        service.getFirebaseFirestore().collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "Datos Firestore";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(Usuario.class);
                        configurationBinding.emailEdit.setText(user.getEmail());
                        UpdateAndSave();
                    } else {
                        Log.e(TAG, "No such document");
                    }
                } else {
                    Log.e(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    private void UpdateAndSave(){
        if(user.getUserName() == null){
            configurationBinding.btnProfile.setText(R.string.save);
        }else{
            configurationBinding.userNameEdit.setText(user.getUserName());
            configurationBinding.btnProfile.setText(R.string.update);
            if(user.getImgPerfil() != null){
                Glide.with(this).load(user.getImgPerfil()).into(configurationBinding.imgUserProfile);
            }
            if(user.getImgFondo() != null){
                Glide.with(this).load(user.getImgFondo()).into(configurationBinding.imgUserCover);
            }
        }
    }

    private void Buttons(){
        configurationBinding.btnProfile.setOnClickListener(this::btnProfile);
        configurationBinding.btnUpdateAccount.setOnClickListener(this::UpdateAccount);
        configurationBinding.btnUploadImgProfile.setOnClickListener(this::UploadImgProfile);
        configurationBinding.btnImgCover.setOnClickListener(this::UploadImgCover);
    }

    private void btnProfile(View view){
        userName = configurationBinding.userNameEdit.getText().toString();
        if(!userName.isEmpty()){
            user.setUserName(userName);

        service.getFirebaseFirestore().collection("users").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Se guardo los datos",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(),"No se pudieron guardar los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
        }else{
            Toast.makeText(this,"Nombre de usuario no debe estar vacio.",Toast.LENGTH_SHORT).show();
        }
    }
    private void UpdateAccount(View view){
        String emailEdit = configurationBinding.emailEdit.getText().toString();
        String currentPassword = configurationBinding.currentPasswordEdit.getText().toString();
        String newPassword = configurationBinding.newPasswordEdit.getText().toString();

        if (!emailEdit.isEmpty() && !currentPassword.isEmpty() && !newPassword.isEmpty() && currentPassword.length() >=6 && newPassword.length() >=6){
            if(currentPassword.equals(user.getPassword())){
                service.getFirebaseAuth().getCurrentUser().updateEmail(emailEdit).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            service.getFirebaseAuth().getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Cuenta actualizada",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Fallo al actualizar",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"Email no valido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else{
            if(emailEdit.isEmpty()){
                Toast.makeText(this,"Ingrese un email valido",Toast.LENGTH_SHORT).show();
            }
            if(currentPassword.isEmpty()){
                Toast.makeText(this,"Contrasena actual no debe estar vacia.",Toast.LENGTH_SHORT).show();
            }
            if(newPassword.isEmpty()){
                Toast.makeText(this,"La contrasena debe tener mas de 6 digitos y no estar vacia.",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void UploadImgProfile(View view){
        image = "profile";
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.putExtra("Imagen","profile");
        gallery.setType("*/*");
        startActivityForResult(gallery,GALLERY_INTENT);
        //intentar enviar un dato con putString
    }
    private void UploadImgCover(View view){
        image = "cover";
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.putExtra("Imagen","cover");
        gallery.setType("*/*");
        startActivityForResult(gallery, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT){
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();
                Log.e("TAG", "onActivityResult: "+image);
                if (image.equals("profile")){
                    StorageReference filePath = service.getStorageReference().child("users").child(email).child("ImgProfile");
                    UploadTask uploadTask = filePath.putFile(uri);

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
                                Glide.with(getApplicationContext()).load(dowloadUri).into(configurationBinding.imgUserProfile);
                                user.setImgPerfil(String.valueOf(dowloadUri));
                                service.getFirebaseFirestore().collection("users").document(email).set(user);
                            }
                        }
                    });
                }else {
                    StorageReference filePath = service.getStorageReference().child("users").child(email).child("ImgCover");
                    UploadTask uploadTask = filePath.putFile(uri);

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
                                Glide.with(getApplicationContext()).load(dowloadUri).into(configurationBinding.imgUserCover);
                                user.setImgFondo(String.valueOf(dowloadUri));
                                service.getFirebaseFirestore().collection("users").document(email).set(user);
                            }
                        }
                    });
                }

            }

        }
    }
}