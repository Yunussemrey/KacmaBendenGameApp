package com.yunusemre.oyunapp.fragment;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.yunusemre.oyunapp.databinding.FragmentOyunBinding;

import java.util.Map;
import java.util.Random;
import java.util.Set;


public class OyunFragment extends Fragment {
    private FragmentOyunBinding binding;
    int skor;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayerCharacter;
    CountDownTimer countDownTimer;

    boolean ispaused = true;
    private long sure = 35000; // 35 saniye
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor2;
    LinearLayout linearLayout;
    SharedPreferences.Editor editor;
    private AdView bannerOyun1;
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyunBinding.inflate(inflater, container, false);


        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);

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

        hideImages();


        skor = 0;
        binding.puan.setText("0");





       // startTimer();

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


        binding.imageViewPause.setOnClickListener(v -> {
          musicService.stopMusic();
             pauseTimer();


            handler.removeCallbacks(runnable);
            for (ImageView image : imageArray) {
                image.setVisibility(View.INVISIBLE);
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());

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
                    //getActivity().finishAffinity(); // uygulamadan çıkış yaptırır!!
                    requireActivity().getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE); // ana ekrana dönüş


                }
            });
            alert.setCancelable(false);
            alert.show();


        });
        mediaPlayerCharacter = MediaPlayer.create(getContext(), R.raw.sound_toch);

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

                musicService.stopMusic();
                ispaused = false;
                binding.textViewZaman.setText("Süre Bitti !");
                handler.removeCallbacks(runnable);
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                sonScore();

            }



        }.start();
        ispaused = true;
    }
        public void sonScore(){
        int sonPuan = Integer.parseInt(binding.puan.getText().toString());
        editor = sp.edit();
        editor.putInt("puan2",sonPuan);
        editor2.putInt("sonPuan",sonPuan);
        editor2.apply();
        if (sonPuan>=30){
            editor.putBoolean("kilit2",true);
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
                        if (isBound && musicService != null){
                            musicService.stopMusic();
                        }
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun2Fragment oyun2Fragment = new Oyun2Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun2Fragment);
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
            AlertDialog.Builder alertd = new AlertDialog.Builder(requireContext());
            alertd.setTitle("Bölüm Sonu! Puanınız: "+sonPuan);
            alertd.setMessage("Maalesef! Sonraki bölüme geçemediniz");
            alertd.setPositiveButton("Tekrar et", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   //fragment tekrar et!!
                    skor =0;
                    binding.puan.setText("0");
                   hideImages();

                   countDownTimer.start();
                }
            });
            alertd.setNegativeButton("Ana Sayfa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requireActivity().getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });
            alertd.setCancelable(false);
            alertd.show();

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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Bölüm 1 ");
        alertDialog.setMessage("Görev: "+"Kısıtlı süre içerisinde 30 puana ulaşman gerek");
        alertDialog.setView(linearLayout);
        alertDialog.setPositiveButton("Oyna", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideImages();
                startTimer();

            }
        });
        alertDialog.setCancelable(false); // butonlara basmadan alert dialog kapanmaz !!!
        alertDialog.show();
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
                int i = random.nextInt(9);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 630);

            }
        };
        handler.post(runnable);
    }
}





