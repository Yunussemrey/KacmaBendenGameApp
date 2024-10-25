package com.yunusemre.oyunapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunusemre.oyunapp.R;
import com.yunusemre.oyunapp.databinding.FragmentOyun11Binding;


public class Oyun11Fragment extends Fragment {
    private FragmentOyun11Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyun11Binding.inflate(inflater,container,false);
















        return binding.getRoot();
    }
}