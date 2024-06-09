package com.yunusemre.oyunapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.yunusemre.oyunapp.MusicService;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentOyun4Binding;

import java.util.Random;


public class Oyun4Fragment extends Fragment {
    private FragmentOyun4Binding binding;
    int skor;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerZombi;
    MediaPlayer mediaPlayerFruit;
    CountDownTimer countDownTimer;

    boolean ispaused = true;
    private long sure = 15000; // 15 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    private AdView bannerOyun4;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    int gelenPuan;
    private MusicService musicService;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun4Binding.inflate(inflater,container,false);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();

            // veri al !
        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
         gelenPuan = sp.getInt("puan4",0);

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

        hideImages();

        skor = gelenPuan;
        binding.puan4.setText(gelenPuan+"");

        //startTimer();

        OnBackPressedCallback geriTusu = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Snackbar.make(requireView(),"Oyundan çıkmak istiyor musun?",Snackbar.LENGTH_INDEFINITE).setAction("Evet", new View.OnClickListener() {
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
           musicService.stopMusic();

            handler.removeCallbacks(runnable);
            for (ImageView image : imageArray) {
                image.setVisibility(View.INVISIBLE);
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

            alert.setTitle("Oyun Durduruldu !");
            alert.setMessage("Devam etmek ister misiniz ?");

            alert.setPositiveButton("Devam et", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    resumeTimer();

                    hideImages();
                    musicService.startMusic();


                }
            });
            alert.setNegativeButton("Ana Sayfa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requireActivity().getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });
            alert.setCancelable(false);
            alert.show();


        });

            mediaPlayerFruit = MediaPlayer.create(requireContext(),R.raw.sound_toch_two);
            mediaPlayerCharacter = MediaPlayer.create(requireContext(),R.raw.sound_toch);
            mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.sound_touch_three);

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
    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(requireActivity(), MusicService.class);
        intent.putExtra("track",2);
        requireActivity().startService(intent);
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

                musicService.stopMusic();
                ispaused = false;
                binding.textViewZaman4.setText("Süre Bitti !");
                handler.removeCallbacks(runnable);
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
        editor.putInt("puan5",sonPuan);
        if (sonPuan>=70){
            editor.putBoolean("kilit5",true);
            editor.apply();
            AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
            alert.setTitle("Bölüm Sonu ! Puanınız: "+sonPuan);
            alert.setMessage("Tebrikler! Sonraki bölüme geçmeye hak kazandınız.");
            alert.setPositiveButton("Sonraki Bölüm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Sonra ki bölüme git!
                    if (mInterstitialAd != null){
                        mInterstitialAd.show(requireActivity());
                    }
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun5Fragment oyun5Fragment = new Oyun5Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun5Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
            alert.setNegativeButton("Ana Sayfa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requireActivity().getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });
            alert.setCancelable(false);
            alert.show();


        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
            alert.setTitle("Bölüm Sonu! Puanınız: "+sonPuan);
            alert.setMessage("Maalesef! Sonraki bölüme geçemediniz");
            alert.setPositiveButton("Tekrar et", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //fragment tekrar et!!
                    skor = gelenPuan;
                    binding.puan4.setText(gelenPuan+"");
                    hideImages();
                    musicService.startMusic();
                    countDownTimer.start();
                }
            });
            alert.setNegativeButton("Ana Sayfa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requireActivity().getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });
            alert.setCancelable(false);
            alert.show();

        }

    }

    @Override
    public void onPause() {
        super.onPause();
       musicService.startMusic();
        pauseTimer();

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Bölüm 4");
        alert.setMessage("Görev: Ortalık iyice karıştı. Muzlarda geldi. Sonraki bölüm için 70 puana ulaşman gerek.");
        alert.setView(linearLayout);
        alert.setPositiveButton("Oyna", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideImages();
                startTimer();

            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    public void hideImages() {

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {

                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                Random random = new Random();
                int i = random.nextInt(36);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 550);

            }
        };
        handler.post(runnable);
    }
}