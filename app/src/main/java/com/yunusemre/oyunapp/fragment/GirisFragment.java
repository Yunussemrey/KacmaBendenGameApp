package com.yunusemre.oyunapp.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.yunusemre.oyunapp.MusicService;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentGirisBinding;


public class GirisFragment extends Fragment {
    private FragmentGirisBinding binding;

    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;
    Intent intent;
    private AdView bannerGiris;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.putExtra("track",1); // müzik 1 çal
        requireActivity().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBound){
            requireActivity().unbindService(serviceConnection);
            isBound = false;
        }
        intent = new Intent(getActivity(), MusicService.class);
        requireActivity().stopService(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGirisBinding.inflate(inflater,container,false);

        sp2 = requireActivity().getSharedPreferences("OyunSonuPuan",Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        // reklam

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        bannerGiris = binding.adViewGiris;
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerGiris.loadAd(adRequest);


        sp = requireContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
        editor = sp.edit();

        int oyun1Puan = sp.getInt("puan2",0);
        int oyun2Puan = sp.getInt("puan3",0);
        int oyun3Puan = sp.getInt("puan4",0);
        int oyun4Puan = sp.getInt("puan5",0);
        int oyun5Puan = sp.getInt("puan6",0);
        int oyun6Puan = sp.getInt("puan7",0);
        int oyun7Puan = sp.getInt("puan8",0);
        int oyun8Puan = sp.getInt("puan9",0);
        int oyun9Puan = sp.getInt("puan10",0);
        int oyun10Puan = sp.getInt("puanSon",0);

        if (oyun1Puan == 0){
            binding.textViewSkor.setVisibility(View.INVISIBLE);
            binding.textView2.setVisibility(View.INVISIBLE);
        }else if (oyun1Puan > oyun2Puan){
            binding.textViewSkor.setText(oyun1Puan+"");
        }else if (oyun2Puan > oyun1Puan){
            binding.textViewSkor.setText(oyun2Puan+"");
        }else if (oyun3Puan > oyun2Puan){
            binding.textViewSkor.setText(oyun3Puan+"");
        }else if (oyun4Puan > oyun3Puan){
            binding.textViewSkor.setText(oyun4Puan+"");
        }else if (oyun5Puan > oyun4Puan){
            binding.textViewSkor.setText(oyun5Puan+"");
        }else if (oyun6Puan > oyun5Puan){
            binding.textViewSkor.setText(oyun6Puan+"");
        }else if (oyun7Puan > oyun6Puan){
            binding.textViewSkor.setText(oyun7Puan+"");
        }else if (oyun8Puan > oyun7Puan){
            binding.textViewSkor.setText(oyun8Puan+"");
        }else if (oyun9Puan > oyun8Puan){
            binding.textViewSkor.setText(oyun9Puan+"");
        }else if (oyun10Puan > oyun9Puan){
            binding.textViewSkor.setText(oyun10Puan+"");
        }


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


                // Son bölüm bilgisi kontrol et!!


                int sonBolumPuan = sp2.getInt("sonPuan",0);




            binding.buttonOyna.setOnClickListener(v -> {
                if (sonBolumPuan < 30){
                    // oyun fragment dan başla ..
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    OyunFragment oyunFragment = new OyunFragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyunFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 30 && sonBolumPuan <45){
                    // Oyun2
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun2Fragment oyun2Fragment = new Oyun2Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun2Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 45 && sonBolumPuan < 60){
                    // Oyun3
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun3Fragment oyun3Fragment = new Oyun3Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun3Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 60 && sonBolumPuan < 70) {
                    //Oyun4
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun4Fragment oyun4Fragment = new Oyun4Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun4Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 70 && sonBolumPuan < 80){
                    // Oyun5
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun5Fragment oyun5Fragment = new Oyun5Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun5Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 80 && sonBolumPuan < 85) {
                    //Oyun6
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun6Fragment oyun6Fragment = new Oyun6Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun6Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 85 && sonBolumPuan < 90) {
                    //Oyun7
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun7Fragment oyun7Fragment = new Oyun7Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun7Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 90 && sonBolumPuan < 100){
                    // Oyun8
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun8Fragment oyun8Fragment = new Oyun8Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun8Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 100 && sonBolumPuan < 120){
                    // Oyun9
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun9Fragment oyun9Fragment = new Oyun9Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun9Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if (sonBolumPuan >= 120 && sonBolumPuan < 150){
                    // Oyun10
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    OyunFragment oyunFragment = new OyunFragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyunFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }


            });

            binding.buttonAyarlar.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.ayarlarGidis);
            });
            binding.buttonLevel.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.bolumFragmentGit);
            });



         // Music Service başlat

         intent = new Intent(getActivity(), MusicService.class);
        requireActivity().startService(intent);






        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        intent = new Intent(getActivity(), MusicService.class);
        requireActivity().stopService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(requireActivity(), MusicService.class);
        intent.putExtra("track",1);
        requireActivity().startService(intent);
    }


}