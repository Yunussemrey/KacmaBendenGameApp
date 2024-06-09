package com.yunusemre.oyunapp.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.yunusemre.oyunapp.MusicService;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentAyarlarBinding;


public class AyarlarFragment extends Fragment {

    private FragmentAyarlarBinding binding;
    PackageInfo pInfo; // versiyon
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private MusicService musicService;
    private SeekBar volumeSeekBar;
    private boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
                isBound = false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.putExtra("track",1); // müzik 1 çal
        requireActivity().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBound){
            requireActivity().unbindService(serviceConnection);
            isBound = false;
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAyarlarBinding.inflate(inflater,container,false);

        sp = requireContext().getSharedPreferences("Müzik", Context.MODE_PRIVATE);
        editor = sp.edit();

        // versiyon
        try {
            pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String versionName = pInfo.versionName;
        int versionCode = pInfo.versionCode;

        binding.textViewVersion.setText("Version "+versionName);


        // ses yükleme
        int savedMusic = loadVolume();
        binding.volumeSeekBar.setProgress(savedMusic);
        binding.textViewSesSeviyesi.setText("Ses Seviyesi: "+savedMusic);


       binding.volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if (isBound){
                   float volume = progress / 100.0f;
                   musicService.setVolume(volume);
                   binding.textViewSesSeviyesi.setText("Ses Seviyesi: "+progress);
                   saveMusic(progress); // yeni ses seviyesini kaydet

               }
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {}

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {}

       });

       // müzik 2 için ses ayarlama






        return binding.getRoot();
    }
    private void saveMusic(int volume){ // sesi kaydetme
        editor.putInt("volume",volume);
        editor.apply();
    }
    private int loadVolume(){
        return sp.getInt("volume",30); // varsayılan ses değeri
    }
}