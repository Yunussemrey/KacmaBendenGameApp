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
import com.yunusemre.oyunapp.databinding.FragmentOyunBinding;
import com.yunusemre.oyunapp.entity.Action;
import com.yunusemre.oyunapp.entity.Music2Service;

import java.util.Random;


public class OyunFragment extends Fragment {
    private FragmentOyunBinding binding;
    int skor;
    ImageView[] imageArray;
    MediaPlayer mediaPlayerCharacter;
    CountDownTimer countDownTimer;

    boolean ispaused = true;
    private long sure = 25000; // 25 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    LinearLayout linearLayout;
    SharedPreferences.Editor editor;
    private AdView bannerOyun1;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences preferences2;


    Action action = new Action();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyunBinding.inflate(inflater, container, false);

        preferences2 = PreferenceManager.getDefaultSharedPreferences(getContext());
        float volume = preferences2.getFloat("touch_sound_volume", 0.5f);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
        editor = sp.edit();
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerOyun1 = binding.adViewOyun1;
         adRequest = new AdRequest.Builder().build();
        bannerOyun1.loadAd(adRequest);

        // geçiş reklamı yükle !
        InterstitialAd.load(requireContext(),"ca-app-pub-3475820063501035/5858433273", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                });



        imageArray = new ImageView[]{binding.imageView0, binding.imageView1, binding.imageView2, binding.imageView3, binding.imageView4, binding.imageView5, binding.imageView6, binding.imageView7, binding.imageView8};

      //  hideImages();


        skor = 0;
        binding.puan.setText("0");





       // startTimer();

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


        binding.imageViewPause.setOnClickListener(v -> {

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
                action.karakterGizle(imageArray,650,9);
                alertDialog.dismiss();
            });
            btnNegatif.setOnClickListener(v1 -> {
                anaSayfa();
                alertDialog.dismiss();
            });
            alertDialog.setCancelable(false);
            alertDialog.show();


        });
        mediaPlayerCharacter = MediaPlayer.create(getContext(), R.raw.sound_toch);
        mediaPlayerCharacter.setVolume(volume,volume);

        // karaktere basmalar ..

            binding.imageView0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skor = skor + 1;
                    binding.puan.setText(String.valueOf(skor));
                    mediaPlayerCharacter.start();
                }
            });


           binding.imageView1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   skor = skor + 1;
                   binding.puan.setText(String.valueOf(skor));
                   mediaPlayerCharacter.start();
               }
           });

           binding.imageView2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   skor = skor + 1;
                   binding.puan.setText(String.valueOf(skor));
                   mediaPlayerCharacter.start();
               }
           });

           binding.imageView3.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   skor = skor + 1;
                   binding.puan.setText(String.valueOf(skor));
                   mediaPlayerCharacter.start();
               }
           });
            binding.imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skor = skor + 1;
                    binding.puan.setText(String.valueOf(skor));
                    mediaPlayerCharacter.start();
                }
            });
            binding.imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skor = skor + 1;
                    binding.puan.setText(String.valueOf(skor));
                    mediaPlayerCharacter.start();
                }
            });
            binding.imageView6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skor = skor + 1;
                    binding.puan.setText(String.valueOf(skor));
                    mediaPlayerCharacter.start();
                }
            });
           binding.imageView7.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   skor = skor + 1;
                   binding.puan.setText(String.valueOf(skor));
                   mediaPlayerCharacter.start();
               }
           });
           binding.imageView8.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   skor = skor + 1;
                   binding.puan.setText(String.valueOf(skor));
                   mediaPlayerCharacter.start();
               }
           });

        // alert dialog da foto gösterme
        linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog_oyun1,null);



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
        binding.textViewZaman.setText("Kalan Süre:");
        binding.textViewsure.setText(" "+seconds);
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
                binding.textViewZaman.setText("Süre Bitti !");
                action.stopRunnable();
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                sonScore();

            }



        }.start();
        ispaused = true;
    }

    public void anaSayfa(){


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GirisFragment girisFragment = new GirisFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, girisFragment);
        fragmentTransaction.commit();
    }



        public void sonScore(){
        int sonPuan = Integer.parseInt(binding.puan.getText().toString());

        editor.putInt("enYüksekPuan",sonPuan);
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        if (sonPuan>=25){
            editor.putBoolean("kilit2",true);
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
                //Sonra ki bölüme git!
                if (mInterstitialAd != null){
                    mInterstitialAd.show(requireActivity());
                }
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Oyun2Fragment oyun2Fragment = new Oyun2Fragment();
                fragmentTransaction.replace(R.id.fragmentContainerView, oyun2Fragment);
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
            AlertDialog.Builder alertd = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_sonskor,null);
            alertd.setView(alertView);
            TextView baslik = alertView.findViewById(R.id.sonSkorBaslik);
            TextView mesaj = alertView.findViewById(R.id.sonSkorMesaj);
            TextView btnPozitif = alertView.findViewById(R.id.sonSkorPozitif);
            TextView btnNegatif = alertView.findViewById(R.id.sonSkorNegatif);
            AlertDialog alertDialog = alertd.create();

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
        FragmentTransaction ft = getParentFragmentManager().beginTransaction(); // fragmentı gizliyor
        ft.remove(OyunFragment.this).commit();

        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().stopService(musicIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setView(linearLayout);
        TextView btnOyna = linearLayout.findViewById(R.id.btnPlay);
        AlertDialog alertDialog1 = alertDialog.create();
        btnOyna.setOnClickListener(v -> {
            action.karakterGizle(imageArray,650,9);
            startTimer();
            alertDialog1.dismiss();
        });
        alertDialog1.setCancelable(false); // butonlara basmadan alert dialog kapanmaz !!!
        alertDialog1.show();

        Intent musicIntent = new Intent(getActivity(), Music2Service.class);
        getActivity().startService(musicIntent);
    }


}





