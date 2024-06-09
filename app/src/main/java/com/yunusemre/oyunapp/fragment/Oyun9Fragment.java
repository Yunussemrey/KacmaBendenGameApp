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
import com.yunusemre.oyunapp.databinding.FragmentOyun9Binding;

import java.util.Random;


public class Oyun9Fragment extends Fragment {
   private FragmentOyun9Binding binding;
    int skor;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerFruit;
    MediaPlayer mediaPlayerBomb;
    MediaPlayer mediaPlayerZombi;

    CountDownTimer countDownTimer;

    boolean ispaused = true;
    private long sure = 10000; // 10 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    private AdView bannerOyun9;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    int gelenPuan;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun9Binding.inflate(inflater,container,false);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();


        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
        gelenPuan = sp.getInt("puan9",0);

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerOyun9 = binding.adViewOyun9;
        adRequest = new AdRequest.Builder().build();
        bannerOyun9.loadAd(adRequest);

        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/9414921946", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });

        imageArray = new ImageView[]{binding.bomba1,binding.bomba2,binding.bomba3,binding.bomba4,binding.kid1,binding.kid2
                ,binding.elma1,binding.zombi1,binding.zombi2,binding.zombi3,binding.zombi4,binding.zombi5};

        hideImages();



        skor = gelenPuan;
        binding.puan9.setText(gelenPuan+"");






        //startTimer();


        OnBackPressedCallback geriTusu = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Snackbar.make(getView(),"Oyundan çıkmak istiyor musun?",Snackbar.LENGTH_INDEFINITE).setAction("Evet", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setEnabled(false);
                        requireActivity().finishAffinity();

                    }
                }).show();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),geriTusu);


        binding.stop9.setOnClickListener(v -> {
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
                    musicService.startMusic();
                    hideImages();



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
        mediaPlayerBomb = MediaPlayer.create(requireContext(),R.raw.music_bomb);
        mediaPlayerFruit = MediaPlayer.create(requireContext(),R.raw.sound_toch_two);
        mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.sound_touch_three);

        // karaktere basmalar ..
        binding.kid1.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan9.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        binding.bomba1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oyunBitti();
                mediaPlayerBomb.start();
                //reklam ile yeniden yada devam olabilir !!
            }
        });
        binding.kid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan9.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.elma1.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan9.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.zombi3.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan9.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.bomba2.setOnClickListener(v -> {
            oyunBitti();
            mediaPlayerBomb.start();
        });
        binding.bomba3.setOnClickListener(v -> {
            oyunBitti();
            mediaPlayerBomb.start();
        });
        binding.zombi2.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan9.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.zombi1.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan9.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.zombi4.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan9.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.zombi5.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan9.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.bomba4.setOnClickListener(v -> {
            oyunBitti();
            mediaPlayerBomb.start();
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
        binding.textViewzaman9.setText("Kalan Süre:");
        binding.zaman9.setText(" "+seconds);
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
                binding.textViewzaman9.setText("Süre Bitti !");
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
        int sonPuan = Integer.parseInt(binding.puan9.getText().toString());
        editor = sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("puan10",sonPuan);
        if (sonPuan>=120){
            editor.putBoolean("kilit10",true);
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
                    Oyun10Fragment oyun10Fragment = new Oyun10Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun10Fragment);
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
                    binding.puan9.setText(gelenPuan+"");
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

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Kaybettiniz !");
        alert.setMessage("Ana Sayfaya Dönmek İster misiniz ?");

        alert.setPositiveButton("Tekrar Dene", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              // Reklam


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
        handler.removeCallbacks(runnable);
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setTitle("Bölüm 9");
        alert.setMessage("Görev: Finale son Bölüm. Artık Hızlı olmalısın. Süre bitmeden 120 puanı geç. ");
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
                int i = random.nextInt(12);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 400);

            }
        };
        handler.post(runnable);
    }
}