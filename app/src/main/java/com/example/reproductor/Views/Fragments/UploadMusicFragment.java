package com.example.reproductor.Views.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reproductor.Models.Clases.DialogSearchable;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Models.Clases.SongsList;
import com.example.reproductor.Models.Clases.UploadInBackground;
import com.example.reproductor.R;
import com.example.reproductor.databinding.FragmentUploadMusicBinding;
import static android.app.Activity.RESULT_OK;

public class UploadMusicFragment extends Fragment {
    private FragmentUploadMusicBinding vbUpl;
    public static final String TAG = "UploadMusicFragment";
    public static final int GALLERY_INTENT = 1;
    public static final int GALLERY_MUSIC = 2;
    private Uri [] url;
    private SongsList songsList;
    private boolean exist = true;
    private SongModel model;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vbUpl = FragmentUploadMusicBinding.inflate(inflater,container,false);
        url = new Uri[2];
        songsList = SongsList.getInstance();
        vbUpl.add.setOnClickListener(this::uplImgSong);
        vbUpl.uplSong.setOnClickListener(this::uplSong);
        vbUpl.submit.setOnClickListener(this::uplMusic);

        vbUpl.nameSong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String var = s.toString();
                exist = findSame(var);
                if(exist){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        vbUpl.nameSong.setBackgroundTintList(requireActivity().getColorStateList(R.color.red));
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        vbUpl.nameSong.setBackgroundTintList(requireActivity().getColorStateList(R.color.green_succes));
                    }
                }
                if(count == 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        vbUpl.nameSong.setBackgroundTintList(requireActivity().getColorStateList(R.color.white));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        vbUpl.nameArtist.setOnClickListener(v -> {
            DialogSearchable searchable = new DialogSearchable(requireContext(), vbUpl.nameArtist);
            searchable.show();
        });
        vbUpl.cancel.setOnClickListener(v -> {
            vbUpl.imgSong.setImageResource(R.drawable.img_default_song);
            vbUpl.cancel.setVisibility(View.GONE);
            vbUpl.add.setVisibility(View.VISIBLE);
            url[0]= null;
        });


        return vbUpl.getRoot();
    }

    public void uplImgSong(View view){
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, GALLERY_INTENT);
    }

    public void uplSong(View view){
        Intent gallery = new Intent();
        gallery.setType("audio/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery,GALLERY_MUSIC);
    }

    public void uplMusic(View view){
        if(vbUpl.nameSong.getText().toString().isEmpty()){
            Toast.makeText(requireContext(),"El nombre es requerido y no debe estar vacio.", Toast.LENGTH_SHORT).show();
        }else{
            if(exist){
                Toast.makeText(requireContext(),"Nombre existente, escriba otro Nombre.",Toast.LENGTH_SHORT).show();
            }else {
                if (model ==null){
                    model = new SongModel();
                }
                model.setCancion(vbUpl.nameSong.getText().toString());
                if(vbUpl.nameArtist.getText().toString().isEmpty()){
                    model.setArtista("Unknown");
                }else{
                    model.setArtista(vbUpl.nameArtist.getText().toString());
                }
                model.setSearch(vbUpl.nameSong.getText().toString().toLowerCase());
                new UploadInBackground(url,model).execute();
                resetView();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                if(requestCode == GALLERY_INTENT){
                    url[0] = uri;
                    vbUpl.imgSong.setImageURI(uri);
                    vbUpl.add.setVisibility(View.GONE);
                    vbUpl.cancel.setVisibility(View.VISIBLE);
                }
                if(requestCode == GALLERY_MUSIC){
                    url[1] = uri;
                    vbUpl.urlSong.setText(getFileName(requireContext(),uri));
                }
            }
        }
    }

    private void resetView(){
        url[0] = null;
        url[1] = null;
        vbUpl.nameSong.setText("");
        vbUpl.nameArtist.setText("");
        vbUpl.imgSong.setImageResource(R.drawable.img_default_song);
        vbUpl.cancel.setVisibility(View.GONE);
        vbUpl.urlSong.setText("");
        vbUpl.add.setVisibility(View.VISIBLE);
        model = null;
    }

    private boolean findSame(String s ){
        for(int i = 0; i < songsList.getAllSongDB().size(); i++){
            String val = songsList.getAllSongDB().get(i).getCancion();
            if(s.equals(val)){
                return true;
            }
        }
        return false;
    }
    public String getFileName(Context context,Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vbUpl = null;
    }
}