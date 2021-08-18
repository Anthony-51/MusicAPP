package com.example.reproductor.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reproductor.Models.Adapters.SongsUploadAdapter;
import com.example.reproductor.Models.Clases.User;
import com.example.reproductor.Interfaces.OnNoteListener;
import com.example.reproductor.databinding.FragmentSongsUploadedBinding;

public class SongsUploadedFragment extends Fragment implements OnNoteListener {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSongsUploadedBinding vbinding = FragmentSongsUploadedBinding.inflate(inflater,container,false);
        User user = User.getInstance();
        SongsUploadAdapter adapter = new SongsUploadAdapter(user.getSongsUploaded());
        LinearLayoutManager linear = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        vbinding.songsUpl.setLayoutManager(linear);
        vbinding.songsUpl.setAdapter(adapter);
        if(user.getSongsUploaded() != null){
            if(user.getSongsUploaded().isEmpty()){
                vbinding.songsUpl.setVisibility(View.GONE);
            }else{
                vbinding.songsUpl.setVisibility(View.VISIBLE);
            }
        }else{
            vbinding.songsUpl.setVisibility(View.GONE);
        }

        return vbinding.getRoot();

    }

    @Override
    public void onNoteClick(int position, String recycler) {

    }
}