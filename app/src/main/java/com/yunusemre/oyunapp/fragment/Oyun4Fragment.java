package com.yunusemre.oyunapp.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentOyun4Binding;
import com.yunusemre.oyunapp.entity.Action;
import com.yunusemre.oyunapp.entity.Music2Service;

import java.util.Random;


public class Oyun4Fragment extends Fragment {
    private FragmentOyun4Binding binding;
    int skor;
    ImageView[] imageArray;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerZombi;
    MediaPlayer mediaPlayerFruit;
    CountDownTimer countDownTimer;

    boolean ispaused = true;
    private long sure = 35000; // 35 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    private AdView bannerOyun4;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences preferences2;
    int gelenPuan;

    Action action = new Action();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun4Binding.inflate(inflater,container,false);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        preferences2 = PreferenceManager.getDefaultSharedPreferences(getContext());
        float volume = preferences2.getFloat("touch_sound_volume", 0.5f);

            // veri al !
        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
         gelenPuan = sp.getInt("enYüksekPuan",0);

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerOyun4 = binding.adViewOyun4;
        adRequest = new AdRequest.Builder().build();
        bannerOyun4.loadAd(adRequest);

        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/3555503870", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });


        imageArray = new ImageView[]{binding.imageView,binding.imageView0, binding.imageView1, binding.imageViewzombi1,binding.imageView3,
                binding.imageView4, binding.imageViewelma1, binding.imageView6, binding.imageViewmuz1,binding.imageView8 ,binding.imageViewzombi2,binding.imageView10
                ,binding.imageView11,binding.imageView12,binding.imageViewzombi3 ,binding.imageView14,binding.imageView15,binding.imageViewzombi4,binding.imageViewzombi5,binding.imageView18
                ,binding.imageView19,binding.imageViewelma2,binding.imageView21,binding.imageView22,binding.imageView23,binding.imageView24,binding.imageViewmuz2,binding.imageViewzombi6,
        binding.imageView27,binding.imageView28,binding.imageView29,binding.imageViewzombi7,binding.imageView31,binding.imageView32,binding.imageViewmuz3,binding.imageView34};



        skor = gelenPuan;
        binding.puan4.setText(gelenPuan+"");

        //startTimer();

        OnBackPressedCallback geriTusu = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Snackbar.make(requireView(),"Oyundan çıkmak istiyor musun?",Snackbar.LENGTH_LONG).setAction("Evet", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setEnabled(false);
                        requireActivity().finishAffinity();

                    }
                }).show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),geriTusu);


        binding.stop4.setOnClickListener(v -> {
            pauseTimer();
            action.stopRunnable();
            for (ImageView image : imageArray) {
                image.setVisibility(View.INVISIBLE);
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_stop,null);
            alert.setView(alertView);
            TextView btnPozitif = alertView.findViewById(R.id.stopPozitif);
            TextView btnNegatif = alertView.findViewById(R.id.stopNegatif);
            AlertDialog alertDialog = alert.create();

            btnPozitif.setOnClickListener(v1 -> {
                resumeTimer();
                action.karakterGizle(imageArray,550,36);
                alertDialog.dismiss();
            });
            btnNegatif.setOnClickListener(v1 -> {
                anaSayfa();
                alertDialog.dismiss();
            });
            alertDialog.setCancelable(false);
            alertDialog.show();


        });

            mediaPlayerFruit = MediaPlayer.create(requireContext(),R.raw.sound_toch_two);
            mediaPlayerCharacter = MediaPlayer.create(requireContext(),R.raw.sound_toch);
            mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.zombie);

            mediaPlayerCharacter.setVolume(volume,volume);
            mediaPlayerFruit.setVolume(volume,volume);
            mediaPlayerZombi.setVolume(volume,volume);

        // karaktere basmalar ..

        binding.imageView.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan4.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan4.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewzombi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan4.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });
        binding.imageView3.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan4.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewelma1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 2;
                binding.puan4.setText(String.valueOf(skor));
                mediaPlayerFruit.start();
            }
        });
        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan4.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();

            }
        });
        binding.imageViewmuz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 3;
                binding.puan4.setText(String.valueOf(skor));
                mediaPlayerFruit.start();
            }
        });
        binding.imageView8.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        binding.imageViewzombi2.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });

        binding.imageView10.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView11.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView12.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewzombi3.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageView14.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView15.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewzombi4.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageViewzombi5.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageView18.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView19.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewelma2.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageView21.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView22.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView23.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView24.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewmuz2.setOnClickListener(v -> {
            skor = skor + 3;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageViewzombi6.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageView27.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView28.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView29.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewzombi7.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageView31.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView32.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewmuz3.setOnClickListener(v -> {
            skor = skor + 3;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageView34.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan4.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog_oyun4,null);


        return binding.getRoot();
    }


    private void pauseTimer(){
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        ispaused = false;


    }

    private void resumeTimer(){
        startTimer();
    }

    private void updateCountdownText(){
        int seconds = (int) (sure / 1000) % 60;
        binding.textViewZaman4.setText("Kalan Süre:");
        binding.zaman4.setText(" "+seconds);
    }


    private void startTimer(){
        countDownTimer = new CountDownTimer(sure, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                sure = millisUntilFinished;
                updateCountdownText();


            }

            @Override
            public void onFinish() {


                ispaused = false;
                binding.textViewZaman4.setText("Süre Bitti !");
               action.stopRunnable();
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }


                sonSkor();


            }



        }.start();
        ispaused = true;
    }
    private void sonSkor(){
        int sonPuan = Integer.parseInt(binding.puan4.getText().toString());
        editor = sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("enYüksekPuan",sonPuan);
        if (sonPuan>=75){
            editor.putBoolean("kilit5",true);
            editor.apply();
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_sonskor,null);
            alert.setView(alertView);
            TextView baslik = alertView.findViewById(R.id.sonSkorBaslik);
            TextView mesaj = alertView.findViewById(R.id.sonSkorMesaj);
            TextView btnPozitif = alertView.findViewById(R.id.sonSkorPozitif);
            TextView btnNegatif = alertView.findViewById(R.id.sonSkorNegatif);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm Sonu ! Puanınız: "+sonPuan);
            mesaj.setText("Tebrikler! Sonraki bölüme geçmeye hak kazandınız");
            btnPozitif.setText("İleri");
            btnPozitif.setOnClickListener(v -> {
                if (mInterstitialAd != null){
                    mInterstitialAd.show(requireActivity());
                }

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Oyun5Fragment oyun5Fragment = new Oyun5Fragment();
                fragmentTransaction.replace(R.id.fragmentContainerView, oyun5Fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                alertDialog.dismiss();
            });
            btnNegatif.setText("Ana Sayfa");
            btnNegatif.setOnClickListener(v -> {
                anaSayfa();
                alertDialog.dismiss();
            });
            alertDialog.setCancelable(false);
            alertDialog.show();


        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_sonskor,null);
            alert.setView(alertView);
            TextView baslik = alertView.findViewById(R.id.sonSkorBaslik);
            TextView mesaj = alertView.findViewById(R.id.sonSkorMesaj);
            TextView btnPozitif = alertView.findViewById(R.id.sonSkorPozitif);
            TextView btnNegatif = alertView.findViewById(R.id.sonSkorNegatif);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm Sonu! Puanınız: "+sonPuan);
            mesaj.setText("Maalesef! Sonraki bölüme geçemediniz");
            btnPozitif.setText("Ana Sayfa");
            btnPozitif.setOnClickListener(v -> {
                anaSayfa();
                alertDialog.dismiss();
            });
            btnNegatif.setVisibility(View.INVISIBLE);
            alertDialog.setCancelable(false);
            alertDialog.show();

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        action.stopRunnable();
        pauseTimer();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.remove(Oyun4Fragment.this).commit();
        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().stopService(musicIntent);

    }
    public void anaSayfa(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GirisFragment girisFragment = new GirisFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, girisFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        action.stopRunnable();
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(linearLayout);
        TextView oyna = linearLayout.findViewById(R.id.btn4Oyna);
        AlertDialog alertDialog = alert.create();
        oyna.setOnClickListener(v -> {
            action.karakterGizle(imageArray,550,36);
            startTimer();
            alertDialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().startService(musicIntent);
    }


}