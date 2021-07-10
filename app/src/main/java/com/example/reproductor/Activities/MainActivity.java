package com.example.reproductor.Activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.Target;
import com.example.reproductor.Adapters.ArtistRecommendedAdapter;
import com.example.reproductor.Adapters.firstRowAdapter;
import com.example.reproductor.Clases.CustomBottomSheet;
import com.example.reproductor.Clases.Usuario;
import com.example.reproductor.Fragments.ArtistFragment;
import com.example.reproductor.Fragments.LibreriaFragment;
import com.example.reproductor.Fragments.HomeFragment;
import com.example.reproductor.Fragments.SearchFragment;
import com.example.reproductor.Interfaces.ListSearch;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;
import com.example.reproductor.Services.ServicesFirebase;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.example.reproductor.databinding.NavHeaderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
                                                                NavigationView.OnNavigationItemSelectedListener,
                                                                SendData,
                                                                MediaPlayer.OnPreparedListener,
                                                                MediaPlayer.OnBufferingUpdateListener,
                                                                SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "Main Activity";
    public static ActivityMainBinding binding;
    private ServicesFirebase services;
    private Usuario user;
    private View bottom, viewBottomListSongs;
    private CustomBottomSheet bottomMediaPlayer,bottomInnerMediaPlayer;
    private ViewGroup.LayoutParams params;
    private int sizeImg;
    private float getTransX, getTransY;
    private MediaPlayer mediaPlayer;
    private ArrayList<HashMap<String, Object>> cancionesRecientes;
    private ArrayList<HashMap<String, Object>> songLikes;
    private String carpeta, cancion;
    private RecyclerView recycler;
    ListSearch listSearch;
    private final Handler handler = new Handler();
    private int navigationHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_baseline_play_circle_outline_24);
        changeFragment(new HomeFragment());
        services = new ServicesFirebase();
        user = new Usuario();
        cancionesRecientes = new ArrayList<>();
        songLikes = new ArrayList<>();
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) binding.mainCoordinator;
        CoordinatorLayout coordinatorLayout1 = (CoordinatorLayout) findViewById(R.id.coordinator_list_songs);
        viewBottomListSongs = coordinatorLayout1.findViewById(R.id.layout_bottom_list_songs);
        bottom = coordinatorLayout.findViewById(R.id.bottomsheet_player);
        bottomMediaPlayer = (CustomBottomSheet) BottomSheetBehavior.from(bottom);
        bottomInnerMediaPlayer = (CustomBottomSheet) BottomSheetBehavior.from(viewBottomListSongs);
        params = (ViewGroup.LayoutParams) binding.bottomsheetPlayer.imgReproductor.getLayoutParams();
        sizeImg = binding.bottomsheetPlayer.imgReproductor.getLayoutParams().height;
        recycler = (RecyclerView) binding.bottomsheetPlayer.layoutBottomListSongs.recyclerBottomSheetList;
        RecyclerView.LayoutManager linear = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(linear);
        ArtistRecommendedAdapter artistaR = new ArtistRecommendedAdapter();
        recycler.setAdapter(artistaR);
        ControlBottomSheet();

        UserLogin();

        binding.bottomsheetPlayer.nameReproductorCollapse.setSelected(true);
        binding.bottomsheetPlayer.nameReproductor.setSelected(true);

        binding.buttonNav.setOnNavigationItemSelectedListener(this);
        binding.nav.setNavigationItemSelectedListener(this);
        binding.bottomsheetPlayer.seekBarReproductor.setOnSeekBarChangeListener(this);
        bottomInnerMediaPlayer.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING||newState==BottomSheetBehavior.STATE_EXPANDED) {
                    binding.bottomsheetPlayer.controlsMediaPlayerCollapse.setVisibility(View.VISIBLE);
                   bottomMediaPlayer.setEnableCollapse(false);

                }else if(newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomMediaPlayer.setEnableCollapse(true);
                    binding.bottomsheetPlayer.controlsMediaPlayerCollapse.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                int minSizeImg = binding.bottomsheetPlayer.imgReproductor.getMinimumWidth();
                int sizeLayoutCollapse = binding.bottomsheetPlayer.layoutCollapse.getHeight();
                if(slideOffset >= 0){
                    int translateCollapsed = (sizeLayoutCollapse - minSizeImg) / 2;
                    float translateImgX = translateCollapsed + ( (getTransX - translateCollapsed) * (1 - slideOffset));
                    float translateImgY = translateCollapsed + ((getTransY - translateCollapsed) * (1 - slideOffset));
                    float alpha = (float) (sizeLayoutCollapse - binding.bottomsheetPlayer.imgReproductor.getTranslationY()) / 100;
                    int resizeImg = (int) (minSizeImg + ((sizeImg - minSizeImg) * (1 - slideOffset)));
                    params.height = resizeImg;
                    params.width = resizeImg;
                    binding.bottomsheetPlayer.imgReproductor.setTranslationX(translateImgX);
                    binding.bottomsheetPlayer.imgReproductor.setTranslationY(translateImgY);
                    binding.bottomsheetPlayer.imgReproductor.setLayoutParams(params);
                    binding.bottomsheetPlayer.layoutCollapse.setAlpha(alpha);
                }
            }
        });
        viewBottomListSongs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        bottomMediaPlayer.setEnableCollapse(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        bottomMediaPlayer.setEnableCollapse(true);
                        break;
                }
                return false;
            }
        });
    }



    public void setListSearch(ListSearch listSearch){
        this.listSearch = listSearch;
        }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_music);
        MenuItem menuProfile = menu.findItem(R.id.profile);
        SearchView search = (SearchView) menuItem.getActionView();
        search.setQueryHint("Search your music...");
        search.setBackgroundResource(R.drawable.bg_search_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            search.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.white));
        }

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menuProfile.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                menuProfile.setVisible(true);
                getSupportFragmentManager().popBackStack();//go back to the previous fragment
                return true;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listSearch != null){
                    listSearch.searchInList(newText);
                }
//                if( newText.length() > 0){
//
//                }
                return false;
            }
        });
        return true;
    }

    private void search(String text){
        services.getFirebaseFirestore().collection("canciones").whereEqualTo("search",text.toLowerCase().trim()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(Objects.requireNonNull(task.getResult()).isEmpty()){
                        Log.e(TAG, "onComplete: no se encontro " );
                    }else{
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.e(TAG, "documentSnapshot: entro" );
                            Log.e(TAG, document.getId() + " => " + document.getData());
//                        MainActivity.this.getData(document.get("Artista").toString(),document.getId());
                        }
                    }
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        if(item.getItemId() == R.id.search_music){
            changeFragment(new SearchFragment());
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

    private void ControlBottomSheet(){
        binding.bottomsheetPlayer.layoutCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomMediaPlayer.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomMediaPlayer.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        bottomMediaPlayer.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        int widthImg = binding.bottomsheetPlayer.imgReproductor.getLayoutParams().width;
                        int translateImg = bottomSheet.getHeight() / 6;
                        int centerImg = (bottomSheet.getWidth() - widthImg) / 2;
                        binding.bottomsheetPlayer.imgReproductor.setTranslationX(centerImg);
                        binding.bottomsheetPlayer.imgReproductor.setTranslationY(translateImg);
                        binding.bottomsheetPlayer.controlsMediaPlayerCollapse.setVisibility(View.GONE);
                        ;break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        binding.bottomsheetPlayer.controlsMediaPlayerCollapse.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mediaPlayer.reset();
                        ;break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                int minSizeImg = binding.bottomsheetPlayer.imgReproductor.getMinimumWidth();
                    if(slideOffset >= 0){
                    int widthImg = binding.bottomsheetPlayer.imgReproductor.getLayoutParams().width;
                    int translateImg = bottomSheet.getHeight() / 6;
                    int centerImg = (bottomSheet.getWidth() - widthImg) / 2;
                    int translateCollapsed = (binding.bottomsheetPlayer.layoutCollapse.getHeight() - minSizeImg) / 2;

                    float translateImgX = translateCollapsed + ( (centerImg - translateCollapsed) * slideOffset);
                    float translateImgY = translateCollapsed + ((translateImg - translateCollapsed) * slideOffset);
                    float alpha = (float) (bottomSheet.getTop() - (bottomSheet.getHeight() * 0.8)) / 100;
                    int resizeImg = (int) (minSizeImg + ((sizeImg - minSizeImg) * slideOffset));
                    params.height = resizeImg;
                    params.width = resizeImg;
                    binding.bottomsheetPlayer.imgReproductor.setTranslationX(translateImgX);
                    binding.bottomsheetPlayer.imgReproductor.setTranslationY(translateImgY);
                    binding.bottomsheetPlayer.layoutExpand.setTranslationY((float) (bottomSheet.getHeight() / 2));
                    binding.bottomsheetPlayer.imgReproductor.setLayoutParams(params);
                    binding.bottomsheetPlayer.layoutCollapse.setAlpha(alpha);
                    getTransY = translateImgY;
                    getTransX = translateImgX;
                }
                if (navigationHeight == 0){
                    navigationHeight = binding.buttonNav.getHeight(); //the height of the navigationView
                }
                    float slideY = navigationHeight - navigationHeight * (1 - slideOffset);
                if(slideY >= 0){
                    binding.buttonNav.setTranslationY(slideY);//Translate the navigatinView
                }
            }
        });
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();

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
                       if(user.getCancionesRecientes() != null){
                           cancionesRecientes = user.getCancionesRecientes();
                       }
                       if(user.getSongLikes() != null){
                           songLikes = user.getSongLikes();
                       }
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

    @Override
    public void getData(String carpeta, String cancion) {
        this.carpeta = carpeta;
        this.cancion = cancion;
        fetchMusicFromFirebase(carpeta,cancion);
        bottom.setVisibility(View.VISIBLE);
        bottomMediaPlayer.setState(BottomSheetBehavior.STATE_EXPANDED);
        Object img = services.getImage(carpeta,cancion);
        Glide.with(this).load(img)
                .centerCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL)
                .into(binding.bottomsheetPlayer.imgReproductor);
        binding.bottomsheetPlayer.nameReproductor.setText(cancion);
        binding.bottomsheetPlayer.nameReproductorCollapse.setText(cancion);
        binding.bottomsheetPlayer.seekBarReproductor.setProgress(0);
        binding.bottomsheetPlayer.seekBarReproductor.setSecondaryProgress(0);
        binding.bottomsheetPlayer.play.setImageResource(R.drawable.ic_pause);
        mediaPlayer.setOnBufferingUpdateListener(this);
        saveRecentActivities(carpeta, cancion);
    }

    private void saveRecentActivities(String carpeta, String cancion) {
        if (services.getFirebaseAuth().getCurrentUser() != null) {
            HashMap<String, Object> elemento = new HashMap<>();
            elemento.put("Cancion", cancion);
            elemento.put("Artista", carpeta);
            if (songLikes.contains(elemento)) {
                binding.bottomsheetPlayer.likeSong.setSelected(true);
            } else {
                binding.bottomsheetPlayer.likeSong.setSelected(false);
                binding.bottomsheetPlayer.unlikeSong.setSelected(false);
            }
            if (cancionesRecientes.size() == 8) {
                if (!cancionesRecientes.contains(elemento)) {
                    cancionesRecientes.remove(0);
                    cancionesRecientes.add(elemento);
                    user.setCancionesRecientes(cancionesRecientes);
                    services.getFirebaseFirestore().collection("users").document(user.getEmail()).set(user);
                }
            } else {
                if (!cancionesRecientes.contains(elemento)) {
                    cancionesRecientes.add(elemento);
                    user.setCancionesRecientes(cancionesRecientes);
                    services.getFirebaseFirestore().collection("users").document(user.getEmail()).set(user);
                }
            }
        }
    }

    private void fetchMusicFromFirebase(String artista, String cancion){
        if(mediaPlayer != null){
            mediaPlayer.reset();
            StorageReference ref = services.getStorageReference().child(artista+" Music/"+cancion+".mp3");
            if(services.getStorageReference() == null){
                Toast.makeText(this,"Error al encontrar databse",Toast.LENGTH_SHORT).show();
            }
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try{
                        final String url = uri.toString();
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.setOnPreparedListener(MainActivity.this);
                        mediaPlayer.prepareAsync();
                        onPrepared(mediaPlayer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("TAG", e.getMessage());
                }
            });
        }
        else{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            StorageReference ref = services.getStorageReference().child(artista+" Music/"+cancion+".mp3");
            if(services.getStorageReference() == null){
                Toast.makeText(this,"Error al encontrar databse",Toast.LENGTH_SHORT).show();
            }
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        //dowload url or file
                        final String url = uri.toString();
                        mediaPlayer.setDataSource(url);
                        // wait for mediaplayer to get prepare
                        mediaPlayer.setOnPreparedListener(MainActivity.this);
                        mediaPlayer.prepareAsync();
                        onPrepared(mediaPlayer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("TAG", e.getMessage());
                }
            });
        }

    }

    private Runnable updater = new Runnable(){
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if(mp.isPlaying()){
            Log.e(TAG, "onPrepared: Comienza la cancion." );
            updateSeekBar();
        }
    }

    private void updateSeekBar(){
        binding.bottomsheetPlayer.seekBarReproductor.setProgress((int)((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration() * 100));
        handler.postDelayed(updater,1000);
    }


    /*
     * Controles del reproductor ( Play - Pause - Repeat - Next - Previous)
     */

    public void musicControl(View view){
        if(mediaPlayer.isPlaying()){
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
//            binding.bottomsheetPlayer.play.startAnimation(animFadeOut);
            binding.bottomsheetPlayer.play.setImageResource(R.drawable.ic_play);
            binding.bottomsheetPlayer.playCollapse.setImageResource(R.drawable.ic_play);
//            binding.bottomsheetPlayer.play.startAnimation(animFadeIn);
        }else{
            mediaPlayer.start();
//            binding.bottomsheetPlayer.play.startAnimation(animFadeOut);
            binding.bottomsheetPlayer.play.setImageResource(R.drawable.ic_pause);
            binding.bottomsheetPlayer.playCollapse.setImageResource(R.drawable.ic_pause);
//            binding.bottomsheetPlayer.play.startAnimation(animFadeIn);
            updateSeekBar();
        }
    }
    public void LikeSong(View view){
        binding.bottomsheetPlayer.likeSong.setSelected(!binding.bottomsheetPlayer.likeSong.isSelected());
        binding.bottomsheetPlayer.unlikeSong.setSelected(false);
        HashMap<String, Object> elemento = new HashMap<>();
        elemento.put("Cancion",cancion);
        elemento.put("Artista",carpeta);
        if (binding.bottomsheetPlayer.likeSong.isSelected()){
            if(!songLikes.contains(elemento)){
                songLikes.add(elemento);
                if(user.getSongLikes() == null){
                    user.setSongLikes(songLikes);
                    services.getFirebaseFirestore().collection("users").document(user.getEmail()).set(user);
                }
                else{
                    services.getFirebaseFirestore().collection("users").document(user.getEmail()).update("songLikes",songLikes);
                }
            }
        }else{
            if(songLikes.contains(elemento)){
                songLikes.remove(elemento);
                services.getFirebaseFirestore().collection("users").document(user.getEmail()).update("songLikes",songLikes);
            }
        }
    }
    public void UnlikeSong(View view){
        binding.bottomsheetPlayer.unlikeSong.setSelected(!binding.bottomsheetPlayer.unlikeSong.isSelected());
        binding.bottomsheetPlayer.likeSong.setSelected(false);
    }

//    public void nextSong(View view){
//        index++;
//        if(index > canciones.size()-1){
//            Toast.makeText(getContext(),"No hay mas canciones",Toast.LENGTH_SHORT).show();
//            index--;
//        }else{
//            String carpeta = canciones.get(index).getArtista();
//            String cancion = canciones.get(index).getNombre();
//            Object img = services.getImage(canciones.get(index).getArtista(),canciones.get(index).getNombre());
//
//            Glide.with(this).load(img).into(binding.bottomsheet.imgReproductor);
//            binding.bottomsheet.nameReproductor.setText(cancion);
//            binding.bottomsheet.nameReproductorCollapse.setText(cancion);
//            fetchMusicFromFirebase(carpeta,cancion);
//        }
//    }
//    public void previousSong(View view){
//        index--;
//        if(index < 0){
//            Toast.makeText(getContext(),"No hay mas canciones",Toast.LENGTH_SHORT).show();
//            index++;
//        }else{
//            String carpeta = canciones.get(index).getArtista();
//            String cancion = canciones.get(index).getNombre();
//            Object img = services.getImage(canciones.get(index).getArtista(),canciones.get(index).getNombre());
//
//            Glide.with(this).load(img).into(binding.bottomsheet.imgReproductor);
//            binding.bottomsheet.nameReproductor.setText(cancion);
//            binding.bottomsheet.nameReproductorCollapse.setText(cancion);
//            fetchMusicFromFirebase(carpeta,cancion);
//        }
//    }
//
    public void replaySong(View view){
        if(binding.bottomsheetPlayer.replay.isSelected()){
            binding.bottomsheetPlayer.replay.setSelected(false);
            mediaPlayer.setLooping(false);
        }else{
            binding.bottomsheetPlayer.replay.setSelected(true);
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "onStartTrackingTouch: " + seekBar.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
        mediaPlayer.seekTo(playPosition);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        binding.bottomsheetPlayer.seekBarReproductor.setSecondaryProgress(percent);
    }
}