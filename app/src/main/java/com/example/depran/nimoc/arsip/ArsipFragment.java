package com.example.depran.nimoc.arsip;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;

public class ArsipFragment extends Fragment {
    public static BerandaActivity berandaActivity;

    public static ArsipFragment newInstance(BerandaActivity activity) {
        berandaActivity = activity;
        return new ArsipFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(berandaActivity).inflate(R.layout.fragment_arsip_empty, container, false);
        return view;
    }
}
