package com.example.reproductor.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.reproductor.Clases.Usuario;
import com.example.reproductor.Fragments.ArtistFragment;
import com.example.reproductor.Fragments.LibreriaFragment;
import com.example.reproductor.Fragments.HomeFragment;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.example.reproductor.databinding.NavHeaderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static ActivityMainBinding binding;
    private ActionBarDrawerToggle mToggle;
    private ServicesFirebase services;
    private Usuario user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new HomeFragment());
        services = new ServicesFirebase();
        user = new Usuario();

        UserLogin();

        binding.buttonNav.setOnNavigationItemSelectedListener(this);
        binding.nav.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close);
        mToggle.syncState();

        binding.searchCloseBtn.setOnClickListener(this::openNav);
        }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home){
            changeFragment(new HomeFragment());
        }
        if (item.getItemId() == R.id.Library){
            changeFragment(new LibreriaFragment());
        }
        if (item.getItemId() == R.id.artist){
            changeFragment(new ArtistFragment());
        }
        if (item.getItemId() == R.id.setting){
            if (services.getFirebaseAuth().getCurrentUser() != null){
                Intent load = new Intent(this, ConfigurationActivity.class);
                load.putExtra("email",services.getFirebaseAuth().getCurrentUser().getEmail());
                this.startActivity(load);
                overridePendingTransition(R.anim.slide_from_bottom,R.anim.slide_to_bottom);
            }else {
                Toast.makeText(this,"Necesita iniciar sesion.",Toast.LENGTH_SHORT).show();
            }

        }
        if (item.getItemId() == R.id.login){
            Intent load = new Intent(this, LoginActivity.class);
            this.startActivity(load);
            overridePendingTransition(R.anim.slide_from_bottom,R.anim.slide_to_bottom);
        }
        if (item.getItemId() == R.id.log_out){
            logOut();
            this.recreate();
        }
        return true;
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }
    private void openNav(View view){
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
    private void logOut(){
        services.getFirebaseAuth().signOut();
    }

    private void getUser(){
       String email = services.getFirebaseAuth().getCurrentUser().getEmail();
       services.getFirebaseFirestore().collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()){
                   DocumentSnapshot document = task.getResult();
                   if (document.exists()){
                       user = document.toObject(Usuario.class);

                       final TextView text = (TextView) findViewById(R.id.nav_user);
                       final TextView emailNav = (TextView) findViewById(R.id.nav_user_email);
                       final ImageView imgProfile = (ImageView) findViewById(R.id.profile_image);
                       final ImageView imgCover = (ImageView) findViewById(R.id.cover_user);
                       text.setText(user.getUserName());
                       emailNav.setText(user.getEmail());
                       Glide.with(getApplicationContext()).load(user.getImgPerfil()).into(imgProfile);
                       Glide.with(getApplicationContext()).load(user.getImgFondo()).into(imgCover);
                   }
               }
           }
       });
    }
    private void UserLogin(){
        if(services.getFirebaseAuth().getCurrentUser() != null){
            binding.nav.getMenu().findItem(R.id.login).setVisible(false);
            binding.nav.getMenu().findItem(R.id.log_out).setVisible(true);
            getUser();
        }else{
            binding.nav.getMenu().findItem(R.id.login).setVisible(true);
            binding.nav.getMenu().findItem(R.id.log_out).setVisible(false);
        }
    }
}