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
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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
import com.yunusemre.oyunapp.databinding.FragmentOyun3Binding;

import java.util.Random;


public class Oyun3Fragment extends Fragment {
    private FragmentOyun3Binding binding;
    int skor;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerFruit;
    MediaPlayer mediaPlayerZombi;
    CountDownTimer countDownTimer;
    boolean ispaused = true;
    private long sure = 20000; // 20 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    private AdView bannerOyun3;
    int gelenPuan;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private MusicService musicService;
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
            musicService = null;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun3Binding.inflate(inflater,container,false);

        Intent musicIntent = new Intent(requireActivity(), MusicService.class);
        requireActivity().bindService(musicIntent,serviceConnection,Context.BIND_AUTO_CREATE);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();


        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
         gelenPuan = sp.getInt("puan3",0);


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

        hideImages();

        skor = gelenPuan;
        binding.puan3.setText(gelenPuan+"");


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


        binding.stop3.setOnClickListener(v -> {
            pauseTimer();
            if (musicService != null){
                musicService.stopMusic();
            }else {

            }


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

        mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.sound_touch_three);
        mediaPlayerCharacter = MediaPlayer.create(requireContext(),R.raw.sound_toch);
        mediaPlayerFruit = MediaPlayer.create(requireContext(),R.raw.sound_toch_two);

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
                if (isBound && musicService != null) {
                    musicService.stopMusic();
                }
                ispaused = false;
                binding.textViewZaman3.setText("Süre Bitti !");
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
        int sonPuan = Integer.parseInt(binding.puan3.getText().toString());
        editor = sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("puan4",sonPuan);
        if (sonPuan>=60){
            editor.putBoolean("kilit4",true);
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
                    Oyun4Fragment oyun4Fragment = new Oyun4Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun4Fragment);
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
                    binding.puan3.setText(gelenPuan+"");
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
      musicService.stopMusic();

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Bölüm 3");
        alert.setMessage("Görev: Elma geldi! Bir elma +2 puan değerinde. Zamanında 60 puana ulaşman gerek.");
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
                int i = random.nextInt(20);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 580);

            }
        };
        handler.post(runnable);
    }

}