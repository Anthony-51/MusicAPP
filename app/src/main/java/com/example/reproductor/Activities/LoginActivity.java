package com.example.reproductor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.reproductor.Activities.MainActivity;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityLoginActivtyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginActivtyBinding loginBinding;
    public static String TAG = "Main Activity";
    private ServicesFirebase service;
    private String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginActivtyBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        service = new ServicesFirebase();
        slider();
        loginBinding.btnRegister.setOnClickListener(this::register);
        loginBinding.btnLogin.setOnClickListener(this::login);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_bottom,R.anim.slide_to_bottom);
    }
    private void slider(){
        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .build();
        SlidrInterface slidr = Slidr.attach(this, config);
        Log.e(TAG,""+getResources().getConfiguration());

    }
    private void login(View view){
        email = loginBinding.emailEditText.getText().toString();
        password = loginBinding.passwordEditText.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            if(password.length() >= 6){
                loginFirebase();
            }
            else{
                Toast.makeText(this,"Contrasena debe tener 6 o mas caracteres",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Los campos no deben de estar vacios",Toast.LENGTH_SHORT).show();
        }
    }


    private void register(View view){
        email = loginBinding.emailEditText.getText().toString();
        password = loginBinding.passwordEditText.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            if(password.length() >= 6){
                registerFirebase();
            }
            else{
                Toast.makeText(this,"Contrasena debe tener 6 o mas caracteres",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Los campos no deben de estar vacios",Toast.LENGTH_SHORT).show();
        }
    }
    private void registerFirebase(){
        service.getFirebaseAuth().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("password",password);
                    String id = Objects.requireNonNull(service.getFirebaseAuth().getCurrentUser()).getUid();
//                    service.getDatabaseReference().child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> taskH) {
//                            if(taskH.isSuccessful()){
//                                Intent home = new Intent(getApplicationContext(), MainActivity.class);
//                                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                getApplicationContext().startActivity(home);
//                                finish();//evitar que usuario regrese si ya se registro
//                            }else{
//                                Toast.makeText(getApplicationContext(),"Fallo en registrar datos",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                    service.getFirebaseFirestore().collection("users").document(email).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> taskH) {
                            if(taskH.isSuccessful()){
                                Intent home = new Intent(getApplicationContext(),MainActivity.class);
                                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(home);
                                finish();//evitar que usuario regrese si ya se registro
                            }else{
                                Toast.makeText(getApplicationContext(),"Fallo en registrar datos",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registrar",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginFirebase() {
        service.getFirebaseAuth().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent home = new Intent(getApplicationContext(),MainActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(home);
                    finish();//evitar que usuario regrese si ya se registro
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo ingresar",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}