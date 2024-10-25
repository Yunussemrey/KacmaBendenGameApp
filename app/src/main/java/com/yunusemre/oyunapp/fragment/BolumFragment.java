package com.yunusemre.oyunapp.fragment;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentBolumBinding;
import com.yunusemre.oyunapp.entity.MusicService;


public class BolumFragment extends Fragment {

  private FragmentBolumBinding binding;

  SharedPreferences sp;
  SharedPreferences.Editor editor;
  TextView baslik;
  TextView mesaj;
  TextView btnOyna;
  TextView btnKapat;
  ImageView kilit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBolumBinding.inflate(inflater,container,false);




        sp = getContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
        editor = sp.edit();



        // bölümlerin kilit durumunu kabul et ve güncelle !!

        kontrolBolumKilidi(requireContext(),binding.lockTwo,"enYüksekPuan","kilit2",40);
        kontrolBolumKilidi(requireContext(),binding.lockThree,"enYüksekPuan","kilit3",60);
        kontrolBolumKilidi(requireContext(),binding.lockFour,"enYüksekPuan","kilit4",75);
        kontrolBolumKilidi(requireContext(),binding.lockFive,"enYüksekPuan","kilit5",85);
        kontrolBolumKilidi(requireContext(),binding.lockSix,"enYüksekPuan","kilit6",100);
        kontrolBolumKilidi(requireContext(),binding.lockSeven,"enYüksekPuan","kilit7",115);
        kontrolBolumKilidi(requireContext(),binding.lockEight,"enYüksekPuan","kilit8",125);
        kontrolBolumKilidi(requireContext(),binding.lockNine,"enYüksekPuan","kilit9",140);
        kontrolBolumKilidi(requireContext(),binding.lockTen,"enYüksekPuan","kilit10",150);

        // kartlara basma

        binding.numberOne.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm 1");
            mesaj.setText("30 saniye içinde 20 puana ulaşmalısın");
            btnOyna.setOnClickListener(v1 -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OyunFragment oyunFragment = new OyunFragment();
                fragmentTransaction.replace(R.id.fragmentContainerView, oyunFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                alertDialog.dismiss();
            });
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567); // kilidi beyaz yaptı
            kilit.setImageResource(R.drawable.lock_open); // renk kapalı gözükmüyor
            alertDialog.setCancelable(false);
            alertDialog.show();
        });

        binding.numberTwo.setOnClickListener(v -> {
            AlertDialog.Builder alert1 = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert1.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert1.create();

            baslik.setText("Bölüm 2");

            kilit.setColorFilter(124567); // kilidi beyaz yaptı

            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit2",40);
            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 40){
                mesaj.setText("30 saniye içinde 40 puana ulaşmalısın");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun2Fragment oyun2Fragment = new Oyun2Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun2Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidini açamadın");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        });

        binding.numberThree.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();
            baslik.setText("Bölüm 3");

            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 60) {
                mesaj.setText("Sonraki bölüm için 75 puana ulaşman gerek");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun3Fragment oyun3Fragment = new Oyun3Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun3Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidini açamadın");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit3",60);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
        binding.numberFour.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();
            baslik.setText("Bölüm 4");;
            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 75){
                mesaj.setText("Sonraki bölüm için 85 puana ulaşman gerekiyor");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun3Fragment oyun3Fragment = new Oyun3Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun3Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidini açamadın");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit4",75);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
        binding.numberFive.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm 5");

            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 85){
                mesaj.setText("Sonraki bölüm için 100 puana ulaşman gerekiyor");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun5Fragment oyun5Fragment = new Oyun5Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun5Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölüm kilidini açamadınız");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit5",85);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
        binding.numberSix.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm 6");
            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 100){
                mesaj.setText("Sonraki bölüme geçmek için 115 puan gerekli");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun6Fragment oyun6Fragment = new Oyun6Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun6Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidini açamadınız");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit6",100);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
        binding.numberSeven.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm 7");
            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 115){
                mesaj.setText("Sonraki bölüme geçmek için 125 puan gerekli");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun7Fragment oyun7Fragment = new Oyun7Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun7Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidi açılmadı");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit7",115);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
        binding.numberEight.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm 8");
            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 125){
                mesaj.setText("Sonraki bölüme geçmek için 140 puana ulaşmalısın");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun8Fragment oyun8Fragment = new Oyun8Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun8Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidini açamadınız");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit8",125);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
        binding.numberNine.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm 9");
            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 140){
                mesaj.setText("Son Bölüme geçmek için 150 puana ulaşman gerek");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun9Fragment oyun9Fragment = new Oyun9Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun9Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidini açamadın");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit9",140);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });
        binding.numberTen.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_bolum,null);
            alert.setView(alertView);
            baslik = alertView.findViewById(R.id.bolumBaslik);
            mesaj = alertView.findViewById(R.id.bolumMesaj);
            btnOyna = alertView.findViewById(R.id.bolumOyna);
            btnKapat = alertView.findViewById(R.id.bolumKapat);
            kilit = alertView.findViewById(R.id.bolumKilit);
            AlertDialog alertDialog = alert.create();

            baslik.setText("Bölüm 10");
            int puan = sp.getInt("enYüksekPuan",0);
            if (puan >= 150){
                mesaj.setText("1. Levelin son bölümü! 150 puan ve üzeri yaparak ilk Level'i bitirin");
                btnOyna.setVisibility(View.VISIBLE);
                btnOyna.setOnClickListener(v1 -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Oyun10Fragment oyun10Fragment = new Oyun10Fragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, oyun10Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    alertDialog.dismiss();
                });
            }else {
                mesaj.setText("Henüz bölümün kilidini açamadınız");
                btnOyna.setVisibility(View.INVISIBLE);
                kilit.setImageResource(R.drawable.lock_close);
            }
            btnKapat.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            kilit.setColorFilter(124567);
            kontrolBolumKilidi(getContext(),kilit,"enYüksekPuan","kilit10",150);
            alertDialog.setCancelable(false);
            alertDialog.show();
        });









        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Müzik servisini başlat
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(musicIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        getActivity().stopService(musicIntent);
    }

    private void kontrolBolumKilidi(Context context, ImageView kilitIcon, String puanAnahtari, String kilitAnahtari, int gerekliPuan){
        int mevcutPuan = sp.getInt(puanAnahtari,0);
        boolean kilitDurumu = sp.getBoolean(kilitAnahtari,false); // kilit durumunu alıyor
        if (mevcutPuan >= gerekliPuan){
            kilitIcon.setImageResource(R.drawable.lock_open);
            sp.edit().putBoolean(kilitAnahtari,true).apply(); //kilit açıldı olarak kaydet!!
        }else if (!kilitDurumu){
            kilitIcon.setImageResource(R.drawable.lock_close);
        }
    }



}