package com.yunusemre.oyunapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private int currentTrack = 1;
    private boolean isPlaying = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    public int onStartCommand(Intent intent,int flags,int startId) {
        if (intent != null && intent.hasExtra("track")) {
            currentTrack = intent.getIntExtra("track",1);
        }
        if (mediaPlayer1 == null){
            mediaPlayer1 = MediaPlayer.create(this,R.raw.music_giris);
            mediaPlayer1.setLooping(true); // müzik tekrar etsin
            // kaydedilen sesi başlangıçta uygula
            SharedPreferences sp = getSharedPreferences("Müzik",MODE_PRIVATE);
            int savedMusic = sp.getInt("volume",30);
            float volume = savedMusic / 100.0f;
            mediaPlayer1.setVolume(volume,volume);
        }
        if (mediaPlayer2 == null){
            mediaPlayer2 = MediaPlayer.create(this,R.raw.music_game);
            mediaPlayer2.setLooping(true);
        }
        if (currentTrack == 1) {
            if (mediaPlayer2.isPlaying()) {
                mediaPlayer2.pause();
            }
            mediaPlayer1.start();
        }else {
            if (mediaPlayer1.isPlaying()) {
                mediaPlayer1.pause();
            }
            mediaPlayer2.start();
        }
        return START_STICKY;
    }
    public void onDestroy(){
        super.onDestroy();
        if (mediaPlayer1 != null){
            mediaPlayer1.stop();
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
    }

    public void setVolume(float volume){
       if (currentTrack == 1 && mediaPlayer1 != null) {
           mediaPlayer1.setVolume(volume,volume);
       } else if (currentTrack == 2 && mediaPlayer2 != null) {
           mediaPlayer2.setVolume(volume,volume);
       }
    }

    public void stopMusic() {
        if (currentTrack == 1 && mediaPlayer1 != null) {
            mediaPlayer1.pause();
        } else if (currentTrack == 2 && mediaPlayer2 != null) {
            mediaPlayer2.pause();
        }
    }

        public void startMusic(){
            if (currentTrack == 1 && mediaPlayer1 != null) {
                mediaPlayer1.start();
            } else if (currentTrack == 2 && mediaPlayer2 != null) {
                mediaPlayer2.start();
            }


        }
        public boolean isMusicPlaying() {
        return isPlaying;
        }

        public void toggleMusic() {
        if (isPlaying) {
            stopMusic();
        }else {
            startMusic();
        }
        }



}
