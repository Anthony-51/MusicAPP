package com.example.reproductor.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reproductor.Activities.MainActivity;
import com.example.reproductor.Adapters.ListSearchAdapter;
import com.example.reproductor.Clases.Canciones;
import com.example.reproductor.Interfaces.ListSearch;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements ListSearch, OnNoteListener {
    private ListSearchAdapter listSearchAdapter;
    private ArrayList<Canciones> songsList;
    private ServicesFirebase servicesFirebase;
    private RecyclerView recyclerView;
    private SendData sendData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler);
        servicesFirebase = new ServicesFirebase();
        songsList = new ArrayList<>();
        ((MainActivity)getActivity()).setListSearch(this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayout);
        listSearchAdapter = new ListSearchAdapter(songsList,this);

        recyclerView.setAdapter(listSearchAdapter);
        fillArray();

        return view;
    }

    private void fillArray(){
        servicesFirebase.getFirebaseFirestore().collection("canciones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){
                        final Canciones cs = new Canciones(document.getId(),document.get("Artista").toString());
                        songsList.add(cs);
                    }
                    listSearchAdapter = new ListSearchAdapter(songsList, SearchFragment.this::onNoteClick);
                    recyclerView.setAdapter(listSearchAdapter);
                }
            }
        });
    }

    @Override
    public void searchInList(String textSearch) {
        if (textSearch.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            listSearchAdapter.getFilter().filter(textSearch);
        }

    }

    @Override
    public void onNoteClick(int position, String recycler) {
        final String artista = songsList.get(position).getArtista();
        final String cancion = songsList.get(position).getNombre();
        sendData.getData(artista,cancion);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            sendData = (SendData) activity;
        }catch(RuntimeException e){
            throw new RuntimeException(activity.toString()+"Must implement method");
        }
    }
}