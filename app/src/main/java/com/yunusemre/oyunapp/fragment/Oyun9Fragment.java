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

import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentOyun9Binding;
import com.yunusemre.oyunapp.entity.Action;
import com.yunusemre.oyunapp.entity.Music2Service;

import java.util.Random;


public class Oyun9Fragment extends Fragment {
   private FragmentOyun9Binding binding;
    int skor;
    ImageView[] imageArray;
    MediaPlayer mediaPlayerCharacter;
    MediaPlayer mediaPlayerFruit;
    MediaPlayer mediaPlayerBomb;
    MediaPlayer mediaPlayerZombi;

    CountDownTimer countDownTimer;

    boolean ispaused = true;
    private long sure = 20000; // 20 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor;
    private AdView bannerOyun9;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    private SharedPreferences preferences2;
    int gelenPuan;
    Action action = new Action();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun9Binding.inflate(inflater,container,false);

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
        bannerOyun9 = binding.adViewOyun9;
        adRequest = new AdRequest.Builder().build();
        bannerOyun9.loadAd(adRequest);

        RewardedAd.load(requireActivity(), "ca-app-pub-3475820063501035/8717885540",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.

                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;

                    }
                });

        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/9414921946", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });

        imageArray = new ImageView[]{binding.bomba1,binding.bomba2,binding.bomba3,binding.bomba4,binding.kid1,binding.kid2
                ,binding.elma1,binding.zombi1,binding.zombi2,binding.zombi3,binding.zombi4,binding.zombi5};





        skor = gelenPuan;
        binding.puan9.setText(gelenPuan+"");






        //startTimer();


        OnBackPressedCallback geriTusu = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Snackbar.make(getView(),"Oyundan çıkmak istiyor musun?",Snackbar.LENGTH_LONG).setAction("Evet", new View.OnClickListener() {
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
                action.karakterGizle(imageArray,460,12);
                alertDialog.dismiss();
            });
            btnNegatif.setOnClickListener(v1 -> {
                anaSayfa();
                alertDialog.dismiss();
            });
            alertDialog.setCancelable(false);
            alertDialog.show();


        });
        mediaPlayerCharacter = MediaPlayer.create(requireContext(),R.raw.sound_toch);
        mediaPlayerBomb = MediaPlayer.create(requireContext(),R.raw.music_bomb);
        mediaPlayerFruit = MediaPlayer.create(requireContext(),R.raw.sound_toch_two);
        mediaPlayerZombi = MediaPlayer.create(requireContext(),R.raw.zombie);

        mediaPlayerBomb.setVolume(volume,volume);
        mediaPlayerZombi.setVolume(volume,volume);
        mediaPlayerFruit.setVolume(volume,volume);
        mediaPlayerCharacter.setVolume(volume,volume);

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

                ispaused = false;
                binding.textViewzaman9.setText("Süre Bitti !");
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
        int sonPuan = Integer.parseInt(binding.puan9.getText().toString());
        editor = sp.edit();
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        editor.putInt("enYüksekPuan",sonPuan);
        if (sonPuan>=140){
            editor.putBoolean("kilit10",true);
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
                Oyun10Fragment oyun10Fragment = new Oyun10Fragment();
                fragmentTransaction.replace(R.id.fragmentContainerView, oyun10Fragment);
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

    private void oyunBitti(){
        pauseTimer();
       action.stopRunnable();
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }
        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().stopService(musicIntent);

        AlertDialog.Builder alert1 = new AlertDialog.Builder(getContext());

        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_oyun_bitti,null);
        alert1.setView(alertView);
        TextView btnAdd = alertView.findViewById(R.id.btnReklam);
        TextView btnBack = alertView.findViewById(R.id.btnBack);
        AlertDialog alertDialog = alert1.create();

        btnAdd.setOnClickListener(v -> {
            yenidenOyna();
            alertDialog.dismiss();
        });
        btnBack.setOnClickListener(v -> {
            anaSayfa();
            alertDialog.dismiss();
        });
        alert1.setCancelable(false);
        alertDialog.show();
    }
    public void yenidenOyna() {
        // geçiş reklamı ile tekrar hak
        if (rewardedAd != null) {
            rewardedAd.show(requireActivity(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    Log.e("hataMesajı", "bölüm 6 Ödül reklamı gösterildi");
                    FragmentTransaction ft = getParentFragmentManager().beginTransaction(); // fragmentı yeniden başlat !!!
                    ft.replace(R.id.fragmentContainerView, new Oyun9Fragment()).commit();
                }
            });
        }else { // ödül reklamı gösterilmezse
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_no_reklam,null);
            alert.setView(alertView);
            TextView btnAnaSayfa = alertView.findViewById(R.id.btnAnaMenu);
            AlertDialog alertDialog = alert.create();
            btnAnaSayfa.setOnClickListener(v -> {
               anaSayfa();
                alertDialog.dismiss();
            });
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

        editor = sp.edit();
        editor.putInt("enYüksekPuan",0);
        editor.apply();
        binding.puan9.setText("0");
    }


    @Override
    public void onPause() {
        super.onPause();
        action.stopRunnable();
        pauseTimer();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.remove(Oyun9Fragment.this).commit();
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
        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_oyun6,null);
        alert.setView(alertView);
        TextView baslik = alertView.findViewById(R.id.textViewBaslik);
        TextView mesaj = alertView.findViewById(R.id.textViewMessage);
        TextView oyna = alertView.findViewById(R.id.btnOyna);
        AlertDialog alertDialog = alert.create();

        baslik.setText("Bölüm 9");
        mesaj.setText("Level 2'ye son Bölüm. Artık Hızlı olmalısın. Süre bitmeden 140 puanı geç");
        oyna.setText("Oyna");
        oyna.setOnClickListener(v -> {
            action.karakterGizle(imageArray,460,12);
            startTimer();
            alertDialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().startService(musicIntent);
    }

}