package com.example.reproductor.Views.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Views.Activities.MainActivity;
import com.example.reproductor.Models.Adapters.ListSearchAdapter;
import com.example.reproductor.Models.Clases.SongsList;
import com.example.reproductor.Interfaces.ListSearch;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;

public class SearchFragment extends Fragment implements ListSearch, OnNoteListener {
    private ListSearchAdapter listSearchAdapter;
    private RecyclerView recyclerView;
    private SendData sendData;
    private SongsList songsSingleton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler);
        songsSingleton = SongsList.getInstance();
        ((MainActivity)getActivity()).setListSearch(this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayout);
        listSearchAdapter = new ListSearchAdapter(songsSingleton.getAllSongDB(),this);

        recyclerView.setAdapter(listSearchAdapter);

        return view;
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
        sendData.getData(songsSingleton.getAllSongDB().get(position), null);
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