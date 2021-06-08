package com.example.reproductor;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reproductor.Fragments.ArtistFragment;
import com.example.reproductor.Fragments.LibreriaFragment;
import com.example.reproductor.Fragments.gridFragmentRecycler;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changeFragment(new gridFragmentRecycler());

        binding.buttonNav.setOnNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close);
        binding.drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            changeFragment(new gridFragmentRecycler());
        }
        if (item.getItemId() == R.id.Library){
            changeFragment(new LibreriaFragment());
        }
        if (item.getItemId() == R.id.artist){
            changeFragment(new ArtistFragment());
        }
        return true;
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }
}