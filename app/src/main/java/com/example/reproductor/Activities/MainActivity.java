package com.example.reproductor;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reproductor.Fragments.ArtistFragment;
import com.example.reproductor.Fragments.LibreriaFragment;
import com.example.reproductor.Fragments.HomeFragment;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static ActivityMainBinding binding;
    private ActionBarDrawerToggle mToggle;
    private ServicesFirebase services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new HomeFragment());
        services = new ServicesFirebase();

        if(services.getFirebaseAuth().getCurrentUser() != null){
            binding.nav.getMenu().findItem(R.id.login).setVisible(false);
            binding.nav.getMenu().findItem(R.id.log_out).setVisible(true);
        }else{
            binding.nav.getMenu().findItem(R.id.login).setVisible(true);
            binding.nav.getMenu().findItem(R.id.log_out).setVisible(false);
        }

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
}