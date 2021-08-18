package com.example.reproductor.Views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.Models.Adapters.ArtistRecommendedAdapter;
import com.example.reproductor.Models.Clases.ArtistsList;
import com.example.reproductor.Models.Clases.CustomBottomSheet;
import com.example.reproductor.Models.Clases.MytItemTouchHelper;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Models.Clases.User;
import com.example.reproductor.Controllers.MainActivityController;
import com.example.reproductor.Controllers.MusicController;
import com.example.reproductor.Views.Fragments.ArtistFragment;
import com.example.reproductor.Views.Fragments.LibreriaFragment;
import com.example.reproductor.Views.Fragments.HomeFragment;
import com.example.reproductor.Views.Fragments.SearchFragment;
import com.example.reproductor.Interfaces.ListSearch;
import com.example.reproductor.Interfaces.SendData;
import com.example.reproductor.R;
import com.example.reproductor.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
                                                                NavigationView.OnNavigationItemSelectedListener,
                                                                SendData {

    private static final String TAG = "Main Activity";
    public static Context context;
    private ActivityMainBinding vbMain;
    private CustomBottomSheet bottomMediaPlayer,bottomInnerMediaPlayer;
    ListSearch listSearch;
    private MainActivityController controller;
    private ImageButton buttonLike, buttonUnlike;
    private MusicController musicController;
    private ImageView imgMedia;
    private ArtistRecommendedAdapter artistaR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vbMain = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vbMain.getRoot());
        setSupportActionBar(vbMain.toolbar);
        context = getApplicationContext();
        vbMain.toolbar.setLogo(R.drawable.ic_baseline_play_circle_outline_24);
        ConstraintLayout controlsCollapse = vbMain.bottomsheetPlayer.controlsMediaPlayerCollapse;
        LinearLayout layoutCollapse = vbMain.bottomsheetPlayer.layoutCollapse;
        LinearLayout controlsExpand = vbMain.bottomsheetPlayer.controlsExpand;
        SeekBar seekBar = vbMain.bottomsheetPlayer.seekBarReproductor;
        buttonLike = vbMain.bottomsheetPlayer.likeSong;
        buttonUnlike = vbMain.bottomsheetPlayer.unlikeSong;
        ImageButton buttonPlay = vbMain.bottomsheetPlayer.play;
        ImageButton buttonPlayCollapse = vbMain.bottomsheetPlayer.playCollapse;
        ImageButton buttonReplay = vbMain.bottomsheetPlayer.replay;
        imgMedia = vbMain.bottomsheetPlayer.imgReproductor;
        TextView nameRepExpand = vbMain.bottomsheetPlayer.nameReproductor;
        TextView nameRepCollapse = vbMain.bottomsheetPlayer.nameReproductorCollapse;


        controller = new MainActivityController(this, vbMain);
        musicController = MusicController.getInstance();
        musicController.setSeekBar(seekBar);
        musicController.setComponents(buttonPlay, buttonPlayCollapse, buttonReplay, nameRepExpand,nameRepCollapse,imgMedia,this);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) vbMain.mainCoordinator;
        CoordinatorLayout coordinatorLayout1 = (CoordinatorLayout) findViewById(R.id.coordinator_list_songs);
        View viewBottomListSongs = coordinatorLayout1.findViewById(R.id.layout_bottom_list_songs);
        View bottom = coordinatorLayout.findViewById(R.id.bottomsheet_player);

        bottomMediaPlayer = (CustomBottomSheet) BottomSheetBehavior.from(bottom);
        bottomInnerMediaPlayer = (CustomBottomSheet) BottomSheetBehavior.from(viewBottomListSongs);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) vbMain.bottomsheetPlayer.imgReproductor.getLayoutParams();
        int sizeImg = vbMain.bottomsheetPlayer.imgReproductor.getLayoutParams().height;
        RecyclerView recycler = (RecyclerView) vbMain.bottomsheetPlayer.layoutBottomListSongs.recyclerBottomSheetList;
        RecyclerView.LayoutManager linear = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(linear);
        artistaR = new ArtistRecommendedAdapter(null);
        ItemTouchHelper.Callback itemCallback = new MytItemTouchHelper(artistaR);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemCallback);
        artistaR.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recycler);
        recycler.setAdapter(artistaR);

        musicController.setAdaptInner(artistaR);
        controller.controlsBottomsheet(bottomMediaPlayer,bottomInnerMediaPlayer,imgMedia,controlsCollapse, layoutCollapse, params,controlsExpand,vbMain.bottomNav, sizeImg);
        bottomMediaPlayer.setState(BottomSheetBehavior.STATE_HIDDEN);

        controller.loadData();


        nameRepCollapse.setSelected(true);
        nameRepExpand.setSelected(true);

        vbMain.bottomNav.setOnNavigationItemSelectedListener(this);
        vbMain.nav.setNavigationItemSelectedListener(this);
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
                controller.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listSearch != null){
                    listSearch.searchInList(newText);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            vbMain.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            vbMain.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        if(item.getItemId() == R.id.search_music){
            controller.ChangeFragment(new SearchFragment());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home){
            controller.ChangeFragment(new HomeFragment());
        }
        if (item.getItemId() == R.id.Library){
            User user = User.getInstance();
            if(user != null){
                controller.ChangeFragment(new LibreriaFragment());
            }
        }
        if (item.getItemId() == R.id.artist){
            ArtistsList arttist = ArtistsList.getInstance();
            if(arttist != null){
                controller.ChangeFragment(new ArtistFragment());
            }
        }
        if (item.getItemId() == R.id.setting){
            User user = User.getInstance();
            if(user != null){
                Intent configuration = new Intent(this, ConfigurationActivity.class);
                this.startActivity(configuration);
                overridePendingTransition(R.anim.slide_from_bottom,R.anim.slide_to_bottom);
            }
        }

        if(item.getItemId() == R.id.upload_song){
            Intent intent = new Intent(this, UplMusicActivity.class);
            this.startActivity(intent);
        }
        if (item.getItemId() == R.id.log_out){
            controller.logOut();
            Intent login = new Intent(this, LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(login);
            this.finish();
        }
        return true;
    }

    @Override
    public void getData(SongModel model, ArrayList<SongModel> songModels) {
        musicController.fetchMusicFromFirebase(model);
        vbMain.bottomsheetPlayer.nameReproductor.setText(model.getCancion());
        vbMain.bottomsheetPlayer.nameReproductorCollapse.setText(model.getCancion());
        controller.openBottomSheetMusic(model,imgMedia,buttonLike,buttonUnlike);
        controller.loadInnerRecycler(model,artistaR, songModels);
    }


    public void expandMediaPlayer(View view){
        if(bottomMediaPlayer.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            bottomMediaPlayer.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void musicControl(View view){
        musicController.playMusic();
    }
    public void LikeSong(View view){
        musicController.saveLikeSong(buttonLike,buttonUnlike);
    }
    public void UnlikeSong(View view){
        buttonUnlike.setSelected(!vbMain.bottomsheetPlayer.unlikeSong.isSelected());
        buttonLike.setSelected(false);
    }
    public void replaySong(View view){
        musicController.replaySong();
    }

    public void nextSong(View view){
        musicController.nextSong();
    }
    public void previousSong(View view){
        musicController.previousSong();
    }
}