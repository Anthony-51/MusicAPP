package com.example.reproductor.Models.Clases;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.reproductor.Views.Activities.UplMusicActivity;
import com.example.reproductor.R;
import com.example.reproductor.databinding.SpinnerSearchableBinding;

import java.util.ArrayList;

public class DialogSearchable extends Dialog {
    private SpinnerSearchableBinding vbSearch;
    private Context context;
    private ArtistsList artistsList;
    private ArrayList<String> artist;
    private TextView txt;

    public DialogSearchable(@NonNull Context context, TextView txt) {
        super(context);
        this.context = context;
        this.txt = txt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.spinner_searchable);
        this.getWindow().setLayout((int)(UplMusicActivity.width/1.1),(int)(UplMusicActivity.height/1.1));

        artistsList = ArtistsList.getInstance();
        fillArrayUser();
        LinearLayout linera;
        linera = (LinearLayout) findViewById(R.id.searchable);
        View view = View.inflate(context,R.layout.spinner_searchable,linera);
        vbSearch = SpinnerSearchableBinding.bind(view);

        if(!txt.getText().toString().isEmpty()){
            vbSearch.editTxt.setText(txt.getText());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,artist);
        vbSearch.listArtist.setAdapter(adapter);

        vbSearch.editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    txt.setText(s.toString());
                    adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vbSearch.listArtist.setOnItemClickListener((parent, view1, position, id) -> {
            txt.setText(adapter.getItem(position));
            this.dismiss();
        });

        vbSearch.close.setOnClickListener((v) -> {
            this.dismiss();
        });
    }

    private void fillArrayUser(){
        artist = new ArrayList<>();
        for(int i = 0; i< artistsList.getArtists().size(); i++){
            String nameArtist = artistsList.getArtists().get(i).getNombre();
            artist.add(nameArtist);
        }
    }
}
