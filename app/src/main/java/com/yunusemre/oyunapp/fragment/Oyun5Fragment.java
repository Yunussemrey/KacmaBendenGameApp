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
import com.yunusemre.oyunapp.databinding.FragmentOyun5Binding;

import java.util.Random;


public class Oyun5Fragment extends Fragment {
  private FragmentOyun5Binding binding;
    int skor;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerZombi;
    MediaPlayer mediaPlayerFruit;
    MediaPlayer mediaPlayerBomb;

    CountDownTimer countDownTimer;

    boolean ispaused = true;
    private long sure = 12000; // 12 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    private AdView bannerOyun5;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    int gelenPuan;
    private MusicService musicService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun5Binding.inflate(inflater,container,false);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();


          sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
         gelenPuan = sp.getInt("puan5",0);

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerOyun5 = binding.adViewOyun5;
        adRequest = new AdRequest.Builder().build();
        bannerOyun5.loadAd(adRequest);

        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/4701064585", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });

        imageArray = new ImageView[]{binding.imageView,binding.imageView0, binding.imageViewZombi4, binding.imageViewelma1,
                binding.imageView4, binding.imageViewZombi1, binding.imageViewbomba, binding.imageView7, binding.imageViewmuz,binding.imageView10
                ,binding.imageViewelma2,binding.imageViewZombi3,binding.imageView14,binding.imageViewbomba2,binding.imageView16,binding.imageView17
                ,binding.imageViewzombi5,binding.imageViewZombi2,binding.imageViewelma,binding.imageViewbomb3};

        hideImages();



        skor = gelenPuan;
        binding.puan5.setText(gelenPuan+"");


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


        binding.stop5.setOnClickListener(v -> {
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
        binding.imageView.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        binding.imageView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan5.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });


        binding.imageViewZombi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan5.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });

        binding.imageViewelma1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 2;
                binding.puan5.setText(String.valueOf(skor));
                mediaPlayerFruit.start();
            }
        });


        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan5.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewZombi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor - 2;
                binding.puan5.setText(String.valueOf(skor));
                mediaPlayerZombi.start();
            }
        });
        binding.imageViewbomba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oyunBitti();
                mediaPlayerBomb.start();
                //reklam ile yeniden yada devam olabilir !!
            }
        });
        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skor = skor + 1;
                binding.puan5.setText(String.valueOf(skor));
                mediaPlayerCharacter.start();
            }
        });
        binding.imageViewmuz.setOnClickListener(v -> {
            skor = skor + 3;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageView10.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });

        binding.imageViewelma2.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageViewZombi3.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });

        binding.imageView14.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewbomba2.setOnClickListener(v -> {
            oyunBitti();
            mediaPlayerBomb.start();
        });
        binding.imageView16.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageView17.setOnClickListener(v -> {
            skor = skor + 1;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerCharacter.start();
        });
        binding.imageViewzombi5.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageViewZombi2.setOnClickListener(v -> {
            skor = skor - 2;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerZombi.start();
        });
        binding.imageViewelma.setOnClickListener(v -> {
            skor = skor + 2;
            binding.puan5.setText(String.valueOf(skor));
            mediaPlayerFruit.start();
        });
        binding.imageViewbomb3.setOnClickListener(v -> {
            oyunBitti();
            mediaPlayerBomb.start();
        });


        linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog_oyun5,null);

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
        binding.textViewzaman5.setText("Kalan Süre:");
        binding.zaman5.setText(" "+seconds);
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
                binding.zaman5.setText("Süre Bitti !");
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
        int sonPuan = Integer.parseInt(binding.puan5.getText().toString());
        editor = sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("puan6",sonPuan);
        if (sonPuan>=80){
            editor.putBoolean("kilit6",true);
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
                    Oyun6Fragment oyun6Fragment = new Oyun6Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun6Fragment);
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
                    binding.puan5.setText(gelenPuan+"");
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
                // geçiş reklamı ile tekrar hak



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
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Bölüm 5");
        alert.setMessage("Görev: Bombalara basmadan 80 puana ulaş.");
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

                handler.postDelayed(this, 530);

            }
        };
        handler.post(runnable);
    }

}