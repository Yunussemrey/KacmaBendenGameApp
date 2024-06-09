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
import com.yunusemre.oyunapp.databinding.FragmentOyun2Binding;

import java.util.Random;


public class Oyun2Fragment extends Fragment {
    private FragmentOyun2Binding binding;
    int skor;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerZombi;
    CountDownTimer countDownTimer;
    boolean ispaused = true;
    private long sure = 25000; // 25 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    private AdView bannerOyun2;
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
        }
    };

    int gelenPuan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun2Binding.inflate(inflater,container,false);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();


        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
         gelenPuan = sp.getInt("puan2",0);




        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerOyun2 = binding.adViewOyun2;
        adRequest = new AdRequest.Builder().build();
        bannerOyun2.loadAd(adRequest);

        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/9007141108", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });






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



        imageArray = new ImageView[]{binding.imageView0, binding.imageView1, binding.imageView2, binding.imageView3,
                binding.imageView4, binding.imageView5, binding.imageViewzombi1, binding.imageView7, binding.imageViewzombi2, binding.imageView9,binding.imageView10
        ,binding.imageView11,binding.imageView12,binding.imageView13,binding.imageViewzombi3,binding.imageView15};

        hideImages();



        skor = gelenPuan;
        binding.puan2.setText(gelenPuan+"");




        //startTimer();


        binding.oyun2Stop.setOnClickListener(v -> {
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

            mediaPlayerCharacter = MediaPlayer.create(requireContext(),R.raw.sound_toch);
            mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.sound_touch_three);
        // karaktere basmalar ..

        binding.imageView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });


        binding.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });

        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });

        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewzombi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });
        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewzombi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan2.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });

        binding.imageViewzombi3.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan2.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });

        binding.imageView9.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan2.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView10.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan2.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView11.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan2.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView12.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan2.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView13.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan2.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView15.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan2.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });



            linearLayout =(LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog_oyun2,null);



        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(requireActivity(), MusicService.class);
        intent.putExtra("track",2);
        requireActivity().startService(intent);
        requireActivity().bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
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
        binding.textViewzaman2.setText("Kalan Süre:");
        binding.textViewsure2.setText(" "+seconds);
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
                binding.textViewzaman2.setText("Süre Bitti !");
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
        int sonPuan = Integer.parseInt(binding.puan2.getText().toString());
        editor =sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("puan3",sonPuan);
        if (sonPuan>=45){
            editor.putBoolean("kilit3",true);
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
                    Oyun3Fragment oyun3Fragment = new Oyun3Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun3Fragment);
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
                    binding.puan2.setText(gelenPuan+"");
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
        alert.setTitle("Bölüm 2");
        alert.setMessage("Görev: Eyvah Zombiler! Kısıtlı süre içerisinde zombilere yakalanmadan 45 puan yapman gerek!");
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
                int i = random.nextInt(16);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 600);

            }
        };
        handler.post(runnable);
    }
}