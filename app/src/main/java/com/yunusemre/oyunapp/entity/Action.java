package com.yunusemre.oyunapp.entity;

import static android.app.PendingIntent.getActivity;
import static android.view.View.inflate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentOyun3Binding;
import com.yunusemre.oyunapp.fragment.GirisFragment;
import com.yunusemre.oyunapp.fragment.Oyun5Fragment;

import java.util.Random;

public class Action {
    Handler handler;
    Runnable runnable;



    public void karakterGizle(ImageView[] imageArray,int zaman,int karakterSayi){

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                Random random = new Random();
                int i = random.nextInt(karakterSayi);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, zaman);//630

            }
        };
        handler.post(runnable);
    }
    public void stopRunnable(){
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }


    }


    public void oyunBitti(CountDownTimer countDownTimer, RewardedAd rewardedAd, boolean isPaused, Context mContext, FragmentActivity fragmentActivity,ImageView[] imageViews,int süre,int sayi){
        stopRunnable();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        isPaused = false;

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.alert_dialog_oyun_bitti, null);
        alert.setView(dialogView);
        TextView btnAdd = dialogView.findViewById(R.id.btnReklam);
        TextView btnBack = dialogView.findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(v -> {
            if (rewardedAd != null) {
                rewardedAd.show(fragmentActivity, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                       countDownTimer.start();
                       karakterGizle(imageViews,süre,sayi);

                    }
                });
            }else {
                fragmentActivity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        btnBack.setOnClickListener(v -> {
            fragmentActivity.getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
        alert.setCancelable(false);
        alert.show();


    }
}

