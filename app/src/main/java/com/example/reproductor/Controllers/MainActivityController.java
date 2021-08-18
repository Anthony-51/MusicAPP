package com.example.reproductor.Controllers;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.Target;
import com.example.reproductor.Views.Activities.MainActivity;
import com.example.reproductor.Models.Adapters.ArtistRecommendedAdapter;
import com.example.reproductor.Models.Clases.ArtistModel;
import com.example.reproductor.Models.Clases.ArtistsList;
import com.example.reproductor.Models.Clases.CustomBottomSheet;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Models.Clases.SongsList;
import com.example.reproductor.Models.Clases.User;
import com.example.reproductor.Views.Fragments.HomeFragment;
import com.example.reproductor.R;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivityController{
    private final MainActivity activity;
    private final ActivityMainBinding vbMain;
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;
    private final User user;
    private final ArtistsList artistsList;
    private final SongsList songsList;
    private int navigationHeight;
    private float getTransY, getTransX;
    private final MusicController musicController;
    private CustomBottomSheet openMediaPlayer, innerMediaPlayer;
    private ArrayList<SongModel> songsDB;
    private ArrayList<ArtistModel> loadArtist;
    private final Handler handler2 = new Handler();
    private final Runnable fillClass = new Runnable() {
        @Override
        public void run() {
            ClassNotNull();
        }
    };
    private void ClassNotNull(){//Comprobar que los datos esten llenos y cargar fragment principal
        if(user != null && songsDB != null && loadArtist != null){
                if(!songsDB.isEmpty() && !loadArtist.isEmpty()){
                    handler2.removeCallbacks(fillClass);
                    vbMain.progressMain.setVisibility(View.GONE);
                    ChangeFragment(null);
                    return;
            }
        }
        handler2.postDelayed(fillClass,1000);
    }
    //construct
    public MainActivityController(MainActivity activity, ActivityMainBinding vbMain) {
        this.activity = activity;
        this.vbMain = vbMain;
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.user = User.getInstance();
        this.artistsList = ArtistsList.getInstance();
        this.songsList = SongsList.getInstance();
        this.musicController = MusicController.getInstance();
    }

    public void ChangeFragment(Fragment fragment){
        if(fragment == null) {
            fragment = new HomeFragment();
        }
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    public void loadData(){
        ClassNotNull();
        if(auth.getCurrentUser() != null){
            String email = auth.getCurrentUser().getEmail();
            if(email != null){
                firestore.collection("users").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.w("TAG", "Listen failed.", error);
                            return;
                        }
                        if(value != null){
                            if(value.exists()){
                                User us = value.toObject(User.class);
                                if(us != null){
                                    user.setUser(us);
                                    loadProfile();
                                }
                            }
                        }
                    }
                });
            }
        }

        firestore.collection("canciones").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(songsDB == null){
                    songsDB = new ArrayList<>();
                }
                songsDB.clear();
                if(error != null){
                    Log.w("TAG", "Listen failed.", error);
                    return;
                }
                if(value != null){
                    for(DocumentSnapshot document : value.getDocuments()){
                        SongModel model = document.toObject(SongModel.class);
                        songsDB.add(model);
                    }
                    songsList.setAllSongDB(songsDB);
                }
            }
        });

        firestore.collection("artistas").addSnapshotListener((value, error) -> {
            if(loadArtist == null){
                loadArtist = new ArrayList<>();
            }
            loadArtist.clear();
            if( error !=null){
                Log.w("TAG", "Listen failed.", error);
                return;
            }
            if(value != null){
                for(DocumentSnapshot document : value.getDocuments()){
                    ArtistModel am = document.toObject(ArtistModel.class);
                    loadArtist.add(am);
                }
                artistsList.setArtists(loadArtist);
            }
        });
    }

    public void search(@NonNull String text){
        firestore.collection("canciones").whereEqualTo("search",text.toLowerCase().trim()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(Objects.requireNonNull(task.getResult()).isEmpty()){
                                Log.e("TAG", "onComplete: no se encontro " );
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.e("TAG", "documentSnapshot: entro" );
                                    Log.e("TAG", document.getId() + " => " + document.getData());
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void openBottomSheetMusic(SongModel model, ImageView image, ImageButton buttonLike, ImageButton buttonUnlike){
        MusicController.index = 0;
        openMediaPlayer.setState(BottomSheetBehavior.STATE_EXPANDED);
        if(user.getSongLikes() != null){
            final int existSongLike = findElement(user.getSongLikes(),model);
            if(existSongLike == 1){
                buttonLike.setSelected(true);
            }else{
                buttonLike.setSelected(false);
                buttonUnlike.setSelected(false);
            }
        }
        String urlImg = model.getUrlImg();
        Glide.with(activity.getApplicationContext()).load(urlImg)
                .centerCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL)
                .into(image);
    }

    public void controlsBottomsheet(@NonNull CustomBottomSheet mediaPlayer, @NonNull CustomBottomSheet innerMedia
                                    , ImageView imgMedia, ConstraintLayout controlsCollapse
                                    , LinearLayout layoutCollapse, ViewGroup.LayoutParams params
                                    , LinearLayout controlsExpand, BottomNavigationView navigationView, int sizeImg){
        this.openMediaPlayer = mediaPlayer;
        this.innerMediaPlayer = innerMedia;
        openMediaPlayer.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        int widthImg = imgMedia.getLayoutParams().width;
                        int translateImg = bottomSheet.getHeight() / 6;
                        int centerImg = (bottomSheet.getWidth() - widthImg) / 2;
                        imgMedia.setTranslationX(centerImg);
                        imgMedia.setTranslationY(translateImg);
                        controlsCollapse.setVisibility(View.GONE);
                        ;break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        controlsCollapse.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        musicController.resetMusic();
                        ;break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                int minSizeImg = imgMedia.getMinimumWidth();
                if(slideOffset >= 0){
                    int widthImg = imgMedia.getLayoutParams().width;
                    int translateImg = bottomSheet.getHeight() / 6;
                    int centerImg = (bottomSheet.getWidth() - widthImg) / 2;
                    int translateCollapsed = (layoutCollapse.getHeight() - minSizeImg) / 2;

                    float translateImgX = translateCollapsed + ( (centerImg - translateCollapsed) * slideOffset);
                    float translateImgY = translateCollapsed + ((translateImg - translateCollapsed) * slideOffset);
                    float alpha = (float) (bottomSheet.getTop() - (bottomSheet.getHeight() * 0.8)) / 100;
                    int resizeImg = (int) (minSizeImg + ((sizeImg - minSizeImg) * slideOffset));
                    params.height = resizeImg;
                    params.width = resizeImg;
                    imgMedia.setTranslationX(translateImgX);
                    imgMedia.setTranslationY(translateImgY);
                    controlsExpand.setTranslationY((float) (bottomSheet.getHeight() / 2));
                    imgMedia.setLayoutParams(params);
                    layoutCollapse.setAlpha(alpha);
                    getTransY = translateImgY;
                    getTransX = translateImgX;
                }
                if (navigationHeight == 0){
                    navigationHeight = navigationView.getHeight(); //the height of the navigationView
                }
                float slideY = navigationHeight - navigationHeight * (1 - slideOffset);
                if(slideY >= 0){
                    navigationView.setTranslationY(slideY);//Translate the navigatinView
                }
            }
        });
        innerMedia.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING||newState==BottomSheetBehavior.STATE_EXPANDED) {
                    controlsCollapse.setVisibility(View.VISIBLE);
                    mediaPlayer.setEnableCollapse(false);

                }else if(newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mediaPlayer.setEnableCollapse(true);
                    controlsCollapse.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                int minSizeImg = imgMedia.getMinimumWidth();
                int sizeLayoutCollapse = layoutCollapse.getHeight();
                if(slideOffset >= 0){
                    int translateCollapsed = (sizeLayoutCollapse - minSizeImg) / 2;
                    float translateImgX = translateCollapsed + ( (getTransX - translateCollapsed) * (1 - slideOffset));
                    float translateImgY = translateCollapsed + ((getTransY - translateCollapsed) * (1 - slideOffset));
                    float alpha = (float) (sizeLayoutCollapse - imgMedia.getTranslationY()) / 100;
                    int resizeImg = (int) (minSizeImg + ((sizeImg - minSizeImg) * (1 - slideOffset)));
                    params.height = resizeImg;
                    params.width = resizeImg;
                    imgMedia.setTranslationX(translateImgX);
                    imgMedia.setTranslationY(translateImgY);
                    imgMedia.setLayoutParams(params);
                    layoutCollapse.setAlpha(alpha);
                }
            }
        });
    }

    public void loadProfile(){
        final TextView userName = (TextView) activity.findViewById(R.id.nav_user);
        final TextView userEmail = (TextView) activity.findViewById(R.id.nav_user_email);
        final ImageView imgProfile = (ImageView) activity.findViewById(R.id.profile_image);
        final ImageView imgCover = (ImageView) activity.findViewById(R.id.cover_user);
        if(user.getUserName() != null && !user.getUserName().isEmpty()){
                userName.setText(user.getUserName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()){
            userEmail.setText(user.getEmail());
        }
        if (user.getImgProfile() != null && !user.getImgProfile().isEmpty()){
            Glide.with(activity).load(user.getImgProfile()).into(imgProfile);
        }
        if (user.getImgCover() != null && !user.getImgCover().isEmpty()){
            Glide.with(activity).load(user.getImgCover()).into(imgCover);
        }
    }

    public void loadInnerRecycler(SongModel model, ArtistRecommendedAdapter adapter, ArrayList<SongModel> arrayList){
        if(arrayList == null || arrayList.isEmpty()){
            for(ArtistModel artist : artistsList.getArtists()){
                if(artist.getNombre().equalsIgnoreCase(model.getArtista())){
                    ArrayList<SongModel> songModels = new ArrayList<>(artist.getCanciones());
                    orderArray(songModels,model.getCancion());
                    songsList.setSongsPlaying(songModels);
                    adapter.setSongModels(songModels);
                    return;
                }
            }
        }else{
            orderArray(arrayList,model.getCancion());
            songsList.setSongsPlaying(arrayList);
            adapter.setSongModels(arrayList);
        }

    }
    public void orderArray(ArrayList<SongModel> songModels, String song){
        int cont = 0;
        for(SongModel model : songModels){
            if(model.getCancion().equalsIgnoreCase(song)){
                SongModel temp = songModels.get(cont);
                songModels.set(cont,songModels.get(0));
                songModels.set(0,temp);
                return;
            }
            cont++;
        }
    }

    public void logOut(){
        auth.signOut();
    }

    private int findElement(@NonNull ArrayList<SongModel> model, SongModel songModel){
        for (SongModel song : model) {
            if(song.getCancion().equalsIgnoreCase(songModel.getCancion()) && song.getArtista().equalsIgnoreCase(songModel.getArtista())){
                return 1;
            }
        }
        return -1;
    }

}
