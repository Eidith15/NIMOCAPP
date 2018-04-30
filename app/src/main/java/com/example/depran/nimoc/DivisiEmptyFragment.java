package com.example.depran.nimoc;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.divisi.TambahDivisiActivity;


public class DivisiEmptyFragment extends Fragment {
    public static DashboardBukuActivity dashboardBukuActivity;

    public static DivisiEmptyFragment newInstance(DashboardBukuActivity activity) {
        dashboardBukuActivity = activity;
        return new DivisiEmptyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(dashboardBukuActivity).inflate(R.layout.fragment_divisi_empty, container, false);
        view.findViewById(R.id.btnTambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahDivisiActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
