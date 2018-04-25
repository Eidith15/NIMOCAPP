package com.example.depran.nimoc;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.depran.nimoc.buku.BerandaActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment {
    public static BerandaActivity berandaActivity;


    public static BerandaFragment newInstance(BerandaActivity activity) {
        berandaActivity = activity;
        return new BerandaFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(berandaActivity).inflate(R.layout.fragment_sign_in, container, false);

        return view;
    }

}
