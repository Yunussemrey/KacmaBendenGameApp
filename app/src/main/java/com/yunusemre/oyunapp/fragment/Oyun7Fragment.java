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
import com.yunusemre.oyunapp.databinding.FragmentOyun7Binding;

import java.util.Random;


public class Oyun7Fragment extends Fragment {
  private FragmentOyun7Binding binding;
    int skor;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerFruit;
    MediaPlayer mediaPlayerZombi;
    MediaPlayer mediaPlayerBomb;
    CountDownTimer countDownTimer;
    boolean ispaused = true;
    private long sure = 10000; // 10 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    private AdView bannerOyun7;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    int gelenPuan;
    private MusicService musicService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun7Binding.inflate(inflater,container,false);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
         gelenPuan = sp.getInt("puan7",0);

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerOyun7 = binding.adViewOyun7;
        adRequest = new AdRequest.Builder().build();
        bannerOyun7.loadAd(adRequest);

        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/3703877371", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });


        imageArray = new ImageView[]{binding.bomba1,binding.bomba2, binding.bomba3,binding.kid1,binding.kid2,binding.kid3,binding.kid4,binding.kid5,binding.kid6,binding.kid7
                ,binding.kid8,binding.kid9,binding.kid10,binding.elma1,binding.elma2,binding.elma3,binding.elma4,binding.elma5,binding.muz1,binding.muz2,binding.muz3};

        hideImages();



        skor = gelenPuan;
        binding.puan7.setText(gelenPuan+"");

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


        binding.stop7.setOnClickListener(v -> {
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

        mediaPlayerBomb = MediaPlayer.create(requireContext(),R.raw.music_bomb);
        mediaPlayerCharacter = MediaPlayer.create(requireContext(),R.raw.sound_toch);
        mediaPlayerFruit = MediaPlayer.create(requireContext(),R.raw.sound_toch_two);
        mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.sound_touch_three);


        // karaktere basmalar ..
        binding.kid1.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        binding.kid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan7.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.zombi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan7.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });

        binding.elma1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 2;
                binding.puan7.setText(String.valueOf(skor));
                mediaPlayerFruit.start();
            }
        });


        binding.kid3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan7.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.zombi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan7.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });
        binding.bomba1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oyunBitti();
                mediaPlayerBomb.start();
                //reklam ile yeniden yada devam olabilir !!
            }
        });
        binding.kid4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan7.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.muz1.setOnClickListener(v -> {
            skor = skor + 3;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.kid5.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        binding.elma2.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.zombi3.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });

        binding.kid6.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.bomba2.setOnClickListener(v -> {
            oyunBitti();
            mediaPlayerBomb.start();
        });
        binding.bomba3.setOnClickListener(v -> {
            oyunBitti();
            mediaPlayerBomb.start();
        });
        binding.zombi4.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.zombi5.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan7.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });



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
        binding.textViewZaman7.setText("Kalan Süre:");
        binding.zaman7.setText(" "+seconds);
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
                binding.zaman7.setText("Süre Bitti !");
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
        int sonPuan = Integer.parseInt(binding.puan7.getText().toString());
        editor = sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("puan8",sonPuan);
        if (sonPuan>=90){
            editor.putBoolean("kilit8",true);
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
                    Oyun8Fragment oyun8Fragment = new Oyun8Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun8Fragment);
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
                    binding.puan7.setText(gelenPuan+"");
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

    private void oyunBitti(){
        pauseTimer();
        musicService.stopMusic();

        handler.removeCallbacks(runnable);
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());

        alert.setTitle("Kaybettiniz !");
        alert.setMessage("Ana Sayfaya Dönmek İster misiniz ?");

        alert.setPositiveButton("Tekrar Dene", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // reklam


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






    @Override
    public void onPause() {
        super.onPause();
        musicService.stopMusic();

    }

    @Override
    public void onResume() {
        super.onResume();
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setTitle("Bölüm 7");
        alert.setMessage("Görev: Süre bitene kadar 90 puanı geçmen gerekiyor.");
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
                int i = random.nextInt(16);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 470);

            }
        };
        handler.post(runnable);
    }
}