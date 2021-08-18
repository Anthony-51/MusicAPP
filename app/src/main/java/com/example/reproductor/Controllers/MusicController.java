package com.example.reproductor.Controllers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.Target;
import com.example.reproductor.Views.Activities.MainActivity;
import com.example.reproductor.Models.Adapters.ArtistRecommendedAdapter;
import com.example.reproductor.Models.Clases.SongModel;
import com.example.reproductor.Models.Clases.SongsList;
import com.example.reproductor.Models.Clases.User;
import com.example.reproductor.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MusicController implements MediaPlayer.OnPreparedListener,
        SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnErrorListener {
    private static MusicController instance;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageButton play, playCollapse, replay;
    private TextView nameRepExpand, nameRepCollapse;
    private ImageView imgRep;
    private MainActivity activity;
    private final StorageReference storage;
    private final User user;
    private final SongsList songsList;
    private final FirebaseFirestore firestore;
    private SongModel sModel;
    public static int index;
    private ArtistRecommendedAdapter adapt;
    private final Handler handler = new Handler();
    private final Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    public void setAdaptInner(ArtistRecommendedAdapter adap){
        this.adapt = adap;
    }


    private MusicController(){
        this.storage = FirebaseStorage.getInstance().getReference();
        this.firestore = FirebaseFirestore.getInstance();
        this.user = User.getInstance();
        this.songsList = SongsList.getInstance();
    }
    public void setIndex(ArrayList<SongModel> Model){
        String can = sModel.getCancion();
        for(int i = 0 ; i< Model.size(); i++){
            String findCan = Model.get(i).getCancion();
            if(can.equalsIgnoreCase(findCan)){
                index = i;
                return;
            }
        }
    }
    public int getIndex(){
        return index;
    }

    public static MusicController getInstance(){
        if(instance == null){
            instance = new MusicController();
        }
        return instance;
    }

    public void setSeekBar(SeekBar seekBar){
        this.seekBar = seekBar;
        this.seekBar.setOnSeekBarChangeListener(this);
    }

    public void setComponents(ImageButton play, ImageButton playCollapse, ImageButton replay, TextView nameRepExpand, TextView nameRepCollapse
                                , ImageView imgRep, MainActivity activity){
        this.play = play;
        this.playCollapse = playCollapse;
        this.replay = replay;
        this.nameRepExpand = nameRepExpand;
        this.nameRepCollapse = nameRepCollapse;
        this.imgRep = imgRep;
        this.activity = activity;
    }

    public void playMusic(){
        if(mediaPlayer.isPlaying()){
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            play.setImageResource(R.drawable.ic_play);
            playCollapse.setImageResource(R.drawable.ic_play);
        }else{
            mediaPlayer.start();
            play.setImageResource(R.drawable.ic_pause);
            playCollapse.setImageResource(R.drawable.ic_pause);
            updateSeekBar();
        }
    }

    public void replaySong(){
        if(replay.isSelected()){
            replay.setSelected(false);
            mediaPlayer.setLooping(false);
        }else{
            replay.setSelected(true);
            mediaPlayer.setLooping(true);
        }
    }
    public void resetMusic(){
        handler.removeCallbacks(updater);
        mediaPlayer.release();
        mediaPlayer = null;
    }


    public void fetchMusicFromFirebase(SongModel model){
        this.sModel = model;
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
        }
        mediaPlayer.reset();
        String urlSong = model.getUrlSong();
        try{
            mediaPlayer.setDataSource(urlSong);
            mediaPlayer.setOnPreparedListener(MusicController.this);
            mediaPlayer.setOnCompletionListener(MusicController.this);
            mediaPlayer.setOnErrorListener(MusicController.this);
            mediaPlayer.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void saveLikeSong(ImageButton buttonLikeSong, ImageButton buttonUnlikeSong){

        buttonLikeSong.setSelected(!buttonLikeSong.isSelected());
        buttonUnlikeSong.setSelected(false);
        final int elementExist = findElement(user.getSongLikes(),sModel);
        if(user.getSongLikes() != null){
            if (buttonLikeSong.isSelected()){
                if(elementExist != 1){
                    user.getSongLikes().add(sModel);
                    firestore.collection("users").document(user.getEmail()).update("songLikes",user.getSongLikes());
                }
            }else{
                if(elementExist == 1){
                    removeElement(user.getSongLikes(),sModel);
                    firestore.collection("users").document(user.getEmail()).update("songLikes",user.getSongLikes());
                }
            }
        }else{
            ArrayList<SongModel> temp = new ArrayList<>();
            temp.add(sModel);
            firestore.collection("users").document(user.getEmail()).update("songLikes", temp);
        }
    }

    public void SaveRecentActivities(SongModel model){
        if (user != null) {
            final int existSongRecent = findElement(user.getCancionesRecientes(),model);
            if(user.getCancionesRecientes() != null){
                if (user.getCancionesRecientes().size() == 8) {
                    if (existSongRecent == -1) {
                        user.getCancionesRecientes().remove(0);
                        user.getCancionesRecientes().add(model);
                        firestore.collection("users").document(user.getEmail()).update("cancionesRecientes",user.getCancionesRecientes());
                    }
                } else {
                    if (existSongRecent == -1) {
                        user.getCancionesRecientes().add(model);
                        firestore.collection("users").document(user.getEmail()).update("cancionesRecientes",user.getCancionesRecientes());
                    }
                }
            }else{
                ArrayList<SongModel> temp = new ArrayList<>();
                temp.add(model);
                firestore.collection("users").document(user.getEmail()).update("cancionesRecientes",temp);
            }
        }
    }
    private void updateSeekBar(){
        seekBar.setProgress((int)((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration() * 100));
        handler.postDelayed(updater,1000);
    }

    public void nextSong(){
        index++; //0
        if(index > songsList.getSongsPlaying().size()-1){
            Toast.makeText(activity.getApplicationContext(),"No hay mas canciones", Toast.LENGTH_SHORT).show();
            index--;
        }else{
            adapt.notifyDataSetChanged();
            String song = songsList.getSongsPlaying().get(index).getCancion();
            String urlImg = songsList.getSongsPlaying().get(index).getUrlImg();
            Glide.with(activity.getApplicationContext()).load(urlImg)
                    .centerCrop()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL)
                    .into(imgRep);
            nameRepExpand.setText(song);
            nameRepCollapse.setText(song);
            fetchMusicFromFirebase(songsList.getSongsPlaying().get(index));
        }
    }
    public void previousSong(){
        index--;
        if(index < 0){
            Toast.makeText(activity.getApplicationContext(),"No hay mas canciones",Toast.LENGTH_SHORT).show();
            index++;
        }else{
            adapt.notifyDataSetChanged();
            String song = songsList.getSongsPlaying().get(index).getCancion();
            String urlImg = songsList.getSongsPlaying().get(index).getUrlImg();
            Glide.with(activity.getApplicationContext()).load(urlImg).centerCrop()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL)
                    .into(imgRep);
            nameRepExpand.setText(song);
            nameRepCollapse.setText(song);
            fetchMusicFromFirebase(songsList.getSongsPlaying().get(index));
        }
    }

    private int findElement(ArrayList<SongModel> model, SongModel songModel){
        if(model != null){
            for (SongModel song : model) {
                if(song.getCancion().equalsIgnoreCase(songModel.getCancion()) && song.getArtista().equalsIgnoreCase(songModel.getArtista())){
                    return 1;
                }
            }
        }
        return -1;
    }

    private void removeElement(ArrayList<SongModel> canciones, SongModel remove){
        int index = 0;
        for(SongModel model : canciones ){
            if(model.getCancion().equalsIgnoreCase(remove.getCancion()) && model.getArtista().equalsIgnoreCase(remove.getArtista())){
                user.getSongLikes().remove(index);
                return;
            }
            index++;
        }
    }

    /*
    * Override
    * */

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        updateSeekBar();
        play.setImageResource(R.drawable.ic_pause);
        playCollapse.setImageResource(R.drawable.ic_pause);
        if(mp.isPlaying()){
            Log.e("TAG", "onPrepared: " );
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
        mediaPlayer.seekTo(playPosition);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(!mp.isLooping()){
            handler.removeCallbacks(updater);
            seekBar.setProgress(0);
            seekBar.setSecondaryProgress(0);
            play.setImageResource(R.drawable.ic_play);
            playCollapse.setImageResource(R.drawable.ic_play);
            SaveRecentActivities(sModel);
            nextSong();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

}

