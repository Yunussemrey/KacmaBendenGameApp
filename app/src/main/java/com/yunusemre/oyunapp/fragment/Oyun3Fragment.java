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
import com.yunusemre.oyunapp.databinding.FragmentOyun3Binding;
import com.yunusemre.oyunapp.entity.Action;
import com.yunusemre.oyunapp.entity.Music2Service;

import java.util.Random;


public class Oyun3Fragment extends Fragment {
    private FragmentOyun3Binding binding;
    int skor;
    ImageView[] imageArray;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerFruit;
    MediaPlayer mediaPlayerZombi;
    CountDownTimer countDownTimer;
    boolean ispaused = true;
    private long sure = 45000; // 45 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    private AdView bannerOyun3;
    int gelenPuan;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences preferences2;

    Action action = new Action();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun3Binding.inflate(inflater,container,false);

        preferences2 = PreferenceManager.getDefaultSharedPreferences(getContext());
        float volume = preferences2.getFloat("touch_sound_volume", 0.5f);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();


        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
         gelenPuan = sp.getInt("enYüksekPuan",0);


        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerOyun3 = binding.adViewOyun3;
        adRequest = new AdRequest.Builder().build();
        bannerOyun3.loadAd(adRequest);

        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/1871473841", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });

        imageArray = new ImageView[]{binding.imageView,binding.imageView0, binding.imageViewZombi4, binding.imageViewelma1,
                binding.imageView4, binding.imageViewZombi1, binding.imageView6, binding.imageView7, binding.imageViewelma3,binding.imageView10
                ,binding.imageViewelma2,binding.imageViewZombi3,binding.imageView14,binding.imageViewZombi5,binding.imageView16,binding.imageView17
        ,binding.imageView19,binding.imageViewZombi2,binding.imageViewelma,binding.imageView22};

        skor = gelenPuan;
        binding.puan3.setText(gelenPuan+"");

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


        binding.stop3.setOnClickListener(v -> {
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
                action.karakterGizle(imageArray,570,20);
                alertDialog.dismiss();
            });
            btnNegatif.setOnClickListener(v1 -> {
                anaSayfa();
                alertDialog.dismiss();
            });
            alertDialog.setCancelable(false);
            alertDialog.show();


        });

        mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.zombie);
        mediaPlayerCharacter = MediaPlayer.create(requireContext(),R.raw.sound_toch);
        mediaPlayerFruit = MediaPlayer.create(requireContext(),R.raw.sound_toch_two);

        mediaPlayerCharacter.setVolume(volume,volume);
        mediaPlayerZombi.setVolume(volume,volume);
        mediaPlayerFruit.setVolume(volume,volume);

        // karaktere basmalar ..
        binding.imageView.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        binding.imageView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan3.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewZombi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan3.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });
        binding.imageViewelma1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 2;
                binding.puan3.setText(String.valueOf(skor));
                mediaPlayerFruit.start();
            }
        });
        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan3.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewZombi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan3.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });
        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan3.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan3.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewelma3.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageView10.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewelma2.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageViewZombi3.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });

        binding.imageView14.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewZombi5.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageView16.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView17.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView19.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewZombi2.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageViewelma.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageView22.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan3.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });


        linearLayout =(LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog_oyun3,null);


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
        binding.textViewZaman3.setText("Kalan Süre:");
        binding.zaman3.setText(" "+seconds);
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
                binding.textViewZaman3.setText("Süre Bitti !");
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
        int sonPuan = Integer.parseInt(binding.puan3.getText().toString());
        editor = sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("enYüksekPuan",sonPuan);
        if (sonPuan>=60){
            editor.putBoolean("kilit4",true);
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
                Oyun4Fragment oyun4Fragment = new Oyun4Fragment();
                fragmentTransaction.replace(R.id.fragmentContainerView, oyun4Fragment);
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
    public void anaSayfa(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GirisFragment girisFragment = new GirisFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, girisFragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onPause() {
        super.onPause();
        action.stopRunnable();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.remove(Oyun3Fragment.this).commit();
        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().stopService(musicIntent);
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
        TextView oyna = linearLayout.findViewById(R.id.btn3Oyna);
        AlertDialog alertDialog = alert.create();
        oyna.setOnClickListener(v -> {
            action.karakterGizle(imageArray,570,20);
            startTimer();
            alertDialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().startService(musicIntent);
    }



}