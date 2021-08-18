package com.example.reproductor.Views.Activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.example.reproductor.R;
import com.example.reproductor.databinding.ActivityUplMusicBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reproductor.Models.Adapters.SectionsPagerAdapter;

import java.util.Objects;

public class UplMusicActivity extends AppCompatActivity {

    private ActivityUplMusicBinding vbUpload;
    public static int width;
    public static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vbUpload = ActivityUplMusicBinding.inflate(getLayoutInflater());
        setContentView(vbUpload.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        vbUpload.viewPager.setAdapter(sectionsPagerAdapter);

        setSupportActionBar(vbUpload.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("UploadMusic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(vbUpload.viewPager);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels; // ancho absoluto en pixels
        height = metrics.heightPixels; // alto absoluto en pixels

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}