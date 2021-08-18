package com.example.reproductor.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reproductor.Models.Clases.User;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityConfigurationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ConfigurationActivity extends AppCompatActivity {
    ActivityConfigurationBinding vbConfig;
    User user = User.getInstance();
    private Uri uri[];
    private String image;
    private ServicesFirebase service;
    private int cont = 0;
    private static final int GALLERY_INTENT = 1;//buscar significado, infiero que es el numero de archivos que se podra seleccioanr de la galeria
    private static final String TAG = "ConfigurationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vbConfig = ActivityConfigurationBinding.inflate(getLayoutInflater());
        setContentView(vbConfig.getRoot());

        service = new ServicesFirebase();
        uri = new Uri[2];
        setSupportActionBar(vbConfig.toolbar);
        getSupportActionBar().setTitle("Configuracion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadData();
        Buttons();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData(){
        vbConfig.emailEdit.setText(user.getEmail());
        if(user.getUserName() == null && user.getImgProfile() == null && user.getImgCover() == null){
            vbConfig.btnUpdatePrf.setText(R.string.save);
        }else{
            vbConfig.btnUpdatePrf.setText(R.string.update);
            if(user.getUserName() != null && !user.getUserName().isEmpty()){
                vbConfig.userEdit.setText(user.getUserName());
            }
            if(user.getImgProfile() != null && !user.getImgProfile().isEmpty()){
                Glide.with(this).load(user.getImgProfile()).into(vbConfig.imgPrf);
            }
            if(user.getImgCover() != null && !user.getImgCover().isEmpty()){
                Glide.with(this).load(user.getImgCover()).into(vbConfig.imgCover);
            }
        }
    }

    private void Buttons(){
        vbConfig.btnUpdatePrf.setOnClickListener(this::btnProfile);
        vbConfig.btnUpdateAcc.setOnClickListener(this::UpdateAccount);
        vbConfig.btnUploadPrf.setOnClickListener(this::UploadImgProfile);
        vbConfig.btnUploadCover.setOnClickListener(this::UploadImgCover);
    }

    private void btnProfile(View view){
        vbConfig.profile.setVisibility(View.INVISIBLE);
        vbConfig.progressPrf.setVisibility(View.VISIBLE);
        String userName = vbConfig.userEdit.getText().toString();
        if(!userName.isEmpty()){
            if(!userName.equals(user.getUserName())){
                service.getFirebaseFirestore().collection("users").document(user.getEmail()).update("userName",userName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            vbConfig.btnUpdatePrf.setText(R.string.save);
                        }else{
                            Toast.makeText(getApplicationContext(),"No se pudieron guardar los datos",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            loadImage();
        }else{
            Toast.makeText(this,"Nombre de usuario no debe estar vacio.",Toast.LENGTH_SHORT).show();
        }
    }
    private void UpdateAccount(View view){
        String emailEdit = vbConfig.emailEdit.getText().toString();
        String currentPassword = vbConfig.currentPassAcc.getText().toString();
        String newPassword = vbConfig.newPassAcc.getText().toString();

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
        if(vbConfig.btnUploadPrf.getText().equals("Subir Perfil")){
            image = "ImgProfile";
            openGallery();
        }else{
            uri[0] = null;
            cancelUploadImg(vbConfig.btnUploadPrf,vbConfig.imgPrf,user.getImgProfile(),"Subir Perfil");
        }
    }
    private void UploadImgCover(View view){
        if(vbConfig.btnUploadCover.getText().equals("Subir Portada")){
            image = "ImgCover";
            openGallery();
        }else{
            uri[1] = null;
            cancelUploadImg(vbConfig.btnUploadCover,vbConfig.imgCover,user.getImgCover(),"Subir Portada");
        }
    }

    private void cancelUploadImg(Button btnUpload,ImageView imgUpload, String url, String titleButton){
        btnUpload.setText(titleButton);
        if(url != null){
            Glide.with(this).load(url).into(imgUpload);
        }else{
            imgUpload.setImageResource(R.drawable.ainaaiba);
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT){
            if (resultCode == RESULT_OK){
                Uri uriData = data.getData();
                if(image.equalsIgnoreCase("ImgProfile")){
                    vbConfig.btnUploadPrf.setText(R.string.cancel);
                    uri[0] = uriData;
                    vbConfig.imgPrf.setImageURI(uriData);
                }else{
                    vbConfig.btnUploadCover.setText(R.string.cancel);
                    uri[1] = uriData;
                    vbConfig.imgCover.setImageURI(uriData);
                }
            }

        }
    }

    private void loadImage(){
        String nameImage;
        if(cont < uri.length){
            int var = cont;
            if(uri[cont] != null){
                if(cont == 0){
                    nameImage = "imgProfile";
                }else{
                    nameImage = "imgCover";
                }
                StorageReference filePath = service.getStorageReference().child("users").child(user.getEmail()).child(nameImage);
                UploadTask uploadTask = filePath.putFile(uri[cont]);

                Task<Uri> urlTask = uploadTask.continueWithTask(task -> filePath.getDownloadUrl()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Uri dowloadUri = task.getResult();
                            Log.e(TAG, "loadImage: " + String.valueOf(dowloadUri) );
                            if(nameImage.equalsIgnoreCase("imgProfile")){
                                Glide.with(getApplicationContext()).load(dowloadUri).into(vbConfig.imgPrf);
                            }else
                            {
                                Glide.with(getApplicationContext()).load(dowloadUri).into(vbConfig.imgCover);
                            }
                            service.getFirebaseFirestore().collection("users").document(user.getEmail()).update(nameImage, String.valueOf(dowloadUri));
                            cont++;
                            loadImage();
                        }
                    }
                );
            }
            if(uri[var] == null){
                cont++;
            }
        }else{
            cont = 0;
            uri[0] = null;
            uri[1] = null;
            vbConfig.profile.setVisibility(View.VISIBLE);
            vbConfig.progressPrf.setVisibility(View.INVISIBLE);
        }
    }
}