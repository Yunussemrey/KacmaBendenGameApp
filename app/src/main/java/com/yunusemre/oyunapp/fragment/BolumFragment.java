package com.yunusemre.oyunapp.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.yunusemre.oyunapp.MusicService;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentBolumBinding;




public class BolumFragment extends Fragment {

  private FragmentBolumBinding binding;

  SharedPreferences sp;
  SharedPreferences.Editor editor;
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBolumBinding.inflate(inflater,container,false);


        sp = getContext().getSharedPreferences("Veriler", Context.MODE_PRIVATE);
        editor = sp.edit();



        // bölümlerin kilit durumunu kabul et ve güncelle !!

        kontrolBolumKilidi(requireContext(),binding.lockTwo,"puan2","kilit2",30);
        kontrolBolumKilidi(requireContext(),binding.lockThree,"puan3","kilit3",45);
        kontrolBolumKilidi(requireContext(),binding.lockFour,"puan4","kilit4",60);
        kontrolBolumKilidi(requireContext(),binding.lockFive,"puan5","kilit5",70);
        kontrolBolumKilidi(requireContext(),binding.lockSix,"puan6","kilit6",80);
        kontrolBolumKilidi(requireContext(),binding.lockSeven,"puan7","kilit7",85);
        kontrolBolumKilidi(requireContext(),binding.lockEight,"puan8","kilit8",90);
        kontrolBolumKilidi(requireContext(),binding.lockNine,"puan9","kilit9",100);
        kontrolBolumKilidi(requireContext(),binding.lockTen,"puan10","kilit10",120);









        return binding.getRoot();
    }

    private void kontrolBolumKilidi(Context context, ImageView kilitIcon, String puanAnahtari,String kilitAnahtari, int gerekliPuan){
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