package com.yunusemre.oyunapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentAyarlarBinding;
import com.yunusemre.oyunapp.entity.Music2Service;
import com.yunusemre.oyunapp.entity.MusicService;


public class AyarlarFragment extends Fragment {

    private FragmentAyarlarBinding binding;
    PackageInfo pInfo; // versiyon
    private SeekBar volumeSeekBar;
    private TextView volumeTextView;
    private SharedPreferences preferences;
    private SharedPreferences preferences1;
    private SharedPreferences preferences2;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor1;
    private SharedPreferences.Editor editor2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAyarlarBinding.inflate(inflater, container, false);



        // versiyon
        try {
            pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String versionName = pInfo.versionName;
        int versionCode = pInfo.versionCode;

        binding.textViewVersion.setText("Version " + versionName);

        // SESLER

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        preferences1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor1 = preferences1.edit();
        preferences2 = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor2 = preferences2.edit();

        binding.btnArkaPlanMusic.setOnClickListener(v -> {
            @SuppressLint("ResourceType") LinearLayout linearLayout = requireActivity().findViewById(R.layout.alert_dialog_ses);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_ses,linearLayout);
             SeekBar seekBar = view.findViewById(R.id.seekBar);
            TextView textViewSeek = view.findViewById(R.id.volumeTextView);

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setView(view);
            final AlertDialog alertDialog = alert.create();

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }


            // Önceden kaydedilmiş ses seviyesini yükle
            float savedVolume = preferences.getFloat("music_volume", 0.3f);
            int progress = (int) (savedVolume * 100);
            seekBar.setProgress(progress);
            textViewSeek.setText("Ses Seviyesi: " + progress);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float volume = progress / 100f;
                    editor.putFloat("music_volume", volume);
                    editor.apply();

                    // Servisteki ses seviyesini güncelle
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    getActivity().startService(intent); // Servisi başlat

                    textViewSeek.setText("Ses Seviyesi: " + progress); // güncelleme

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

            });
            alertDialog.show();
        });
        binding.btnOyunMusic.setOnClickListener(v -> {
            LinearLayout linearLayout = requireActivity().findViewById(R.layout.alert_dialog_ses_2);
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_ses_2,linearLayout);
            alert.setView(alertView);
            SeekBar seekBar = alertView.findViewById(R.id.seekBar2);
            TextView text = alertView.findViewById(R.id.volumeTextView2);
            AlertDialog alertDialog = alert.create();

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            // Önceden kaydedilmiş ses seviyesini yükle
            float savedVolume = preferences1.getFloat("music_volume2", 0.3f);
            int progress = (int) (savedVolume * 100);
            seekBar.setProgress(progress);
            text.setText("Ses Seviyesi: " + progress);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float volume = progress / 100f;
                    editor1.putFloat("music_volume2", volume);
                    editor1.apply();

                    // Servisteki ses seviyesini güncelle
                    Intent intent = new Intent(getActivity(), Music2Service.class);
                   // getActivity().startService(intent); // Servisi başlat

                    text.setText("Ses Seviyesi: " + progress); // güncelleme
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

            });
            alertDialog.show();
        });
        binding.btnDokunmaSes.setOnClickListener(v -> {
            LinearLayout linearLayout = requireActivity().findViewById(R.layout.alert_dialog_ses_3);
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_ses_3,linearLayout);
            alert.setView(alertView);
            SeekBar seekBar = alertView.findViewById(R.id.seekBar3);
            TextView textView = alertView.findViewById(R.id.volumeTextView3);
            AlertDialog alertDialog = alert.create();

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            float savedVolume = preferences2.getFloat("touch_sound_volume", 0.5f);
            int progress = (int) (savedVolume * 100);
            seekBar.setProgress(progress);
            textView.setText("Ses Seviyesi: " + progress);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float volume = progress / 100f;
                    editor2.putFloat("touch_sound_volume", volume);
                    editor2.apply();
                    textView.setText("Ses Seviyesi: " + progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            alertDialog.show();
        });
        binding.btnPuanlama.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_puanlama,null);
            alert.setView(alertView);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        });









        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Müzik servisini başlat
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(musicIntent);
    }

}
