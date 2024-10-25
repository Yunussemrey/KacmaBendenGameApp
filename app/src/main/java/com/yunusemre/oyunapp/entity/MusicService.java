package com.yunusemre.oyunapp.entity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.yunusemre.oyunapp.R;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.music_giris);
        mediaPlayer.setLooping(true);

        // SharedPreferences'dan ses seviyesini al
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        float volume = preferences.getFloat("music_volume", 0.3f); // Varsayılan değer 1.0f (maksimum ses)
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        float volume = preferences.getFloat("music_volume", 0.3f);
        setVolume(volume);

       // Toast.makeText(this, "Ses seviyesi: "+volume, Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        //Toast.makeText(this, "Music Service Stopped", Toast.LENGTH_SHORT).show();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }
}
