package com.yunusemre.oyunapp.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentGirisBinding;
import com.yunusemre.oyunapp.entity.MusicService;
import com.yunusemre.oyunapp.entity.NetWork;


public class GirisFragment extends Fragment {
    private FragmentGirisBinding binding;

    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    private AdView bannerGiris;
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

        int oyunPuan = sp.getInt("enYüksekPuan",0);
       /* int oyun2Puan = sp.getInt("enYüksekPuan",0);
        int oyun3Puan = sp.getInt("enYüksekPuan",0);
        int oyun4Puan = sp.getInt("enYüksekPuan",0);
        int oyun5Puan = sp.getInt("enYüksekPuan",0);
        int oyun6Puan = sp.getInt("enYüksekPuan",0);
        int oyun7Puan = sp.getInt("enYüksekPuan",0);
        int oyun8Puan = sp.getInt("enYüksekPuan",0);
        int oyun9Puan = sp.getInt("enYüksekPuan",0);
        int oyun10Puan = sp.getInt("enYüksekPuan",0);*/

        if ( oyunPuan== 0){
           binding.textViewSkor.setText(oyunPuan+"-0");
        }else if (oyunPuan >= 25){
            binding.textViewSkor.setText(oyunPuan+"-1");
        }else if (oyunPuan >= 40){
            binding.textViewSkor.setText(oyunPuan+"-2");
        }else if (oyunPuan >= 60){
            binding.textViewSkor.setText(oyunPuan+"-3");
        }else if (oyunPuan >= 75){
            binding.textViewSkor.setText(oyunPuan+"-4");
        }else if (oyunPuan >= 85){
            binding.textViewSkor.setText(oyunPuan+"-5");
        }else if (oyunPuan >= 100){
            binding.textViewSkor.setText(oyunPuan+"-6");
        }else if (oyunPuan >= 115){
            binding.textViewSkor.setText(oyunPuan+"-7");
        }else if (oyunPuan >= 125){
            binding.textViewSkor.setText(oyunPuan+"-8");
        }else if (oyunPuan >= 140){
            binding.textViewSkor.setText(oyunPuan+"-9");
        } else if (oyunPuan >= 150) {
            binding.textViewSkor.setText(oyunPuan+" - 10");

        }


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


            binding.buttonOyna.setOnClickListener(v -> {
               saveGame();
                Intent musicIntent = new Intent(getActivity(), MusicService.class);
                getActivity().stopService(musicIntent);

            });

            binding.returnn.setOnClickListener(v -> { // skor sıfırla ve oyna yı 1 den başla
                editor = sp.edit();
                editor.putInt("enYüksekPuan",0);
                editor.apply();
                binding.textViewSkor.setText("0");
                Toast.makeText(getActivity(), "Skor sıfırlandı", Toast.LENGTH_SHORT).show();

            });

            binding.buttonAyarlar.setOnClickListener(v -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AyarlarFragment ayarlarFragment = new AyarlarFragment();
                fragmentTransaction.replace(R.id.fragmentContainerView, ayarlarFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            binding.buttonLevel.setOnClickListener(v -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                BolumFragment bolumFragment = new BolumFragment();
                fragmentTransaction.replace(R.id.fragmentContainerView, bolumFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });

        return binding.getRoot();
    }
    @Override
    public void onPause() {
        super.onPause();
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().stopService(musicIntent);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!NetWork.isNetworkAvailable(getContext())) {
            showInternetDialog();
        }

        int highScore = sp.getInt("enYüksekPuan",0);
        binding.textViewSkor.setText(" " +highScore);

        // Müzik servisini başlat
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(musicIntent);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().stopService(musicIntent);
    }

    private void showInternetDialog(){ // internet var mı yok mu?
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_internet,null);
        alert.setView(alertView);
        TextView btnAyar = alertView.findViewById(R.id.btnSettings);
        TextView btnCik = alertView.findViewById(R.id.btnExit);
        AlertDialog alertDialog = alert.create();
        btnAyar.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            alertDialog.dismiss();
        });
        btnCik.setOnClickListener(v -> {
           // requireActivity().finishAffinity();
            alertDialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void saveGame(){
        int sonBolumPuan = sp.getInt("enYüksekPuan",0);
        if (sonBolumPuan >= 0 && sonBolumPuan < 40){
            // oyun fragment dan başla ..
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            OyunFragment oyunFragment = new OyunFragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyunFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 40 && sonBolumPuan <60){
            // Oyun2
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun2Fragment oyun2Fragment = new Oyun2Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun2Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 60 && sonBolumPuan < 75){
            // Oyun3
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun3Fragment oyun3Fragment = new Oyun3Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun3Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 75 && sonBolumPuan < 85) {
            //Oyun4
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun4Fragment oyun4Fragment = new Oyun4Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun4Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 85 && sonBolumPuan < 100){
            // Oyun5
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun5Fragment oyun5Fragment = new Oyun5Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun5Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 100 && sonBolumPuan < 115) {
            //Oyun6
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun6Fragment oyun6Fragment = new Oyun6Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun6Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 115 && sonBolumPuan < 125) {
            //Oyun7
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun7Fragment oyun7Fragment = new Oyun7Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun7Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 125 && sonBolumPuan < 140){
            // Oyun8
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun8Fragment oyun8Fragment = new Oyun8Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun8Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 140 && sonBolumPuan < 150){
            // Oyun9
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Oyun9Fragment oyun9Fragment = new Oyun9Fragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyun9Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (sonBolumPuan >= 150 && sonBolumPuan < 200){
            // Oyun10
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            OyunFragment oyunFragment = new OyunFragment();
            fragmentTransaction.replace(R.id.fragmentContainerView, oyunFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }



}