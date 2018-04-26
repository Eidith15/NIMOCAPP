package com.example.depran.nimoc;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.function.Riwayat;
import com.example.depran.nimoc.function.RiwayatAdapter;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatFragment extends Fragment {
    public static DashboardBukuActivity dashboardBukuActivity;
    ListView listView;

    public static RiwayatFragment newInstance(DashboardBukuActivity activity) {
        dashboardBukuActivity = activity;
        return new RiwayatFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(dashboardBukuActivity).inflate(R.layout.fragment_riwayat, container, false);
        ArrayList<Riwayat> riwayats = new ArrayList<Riwayat>();
        riwayats.add(new Riwayat("Syiar & Media", "300.000","Hari ini"));
        riwayats.add(new Riwayat("Syiar & Media", "200.000","Hari ini"));

        RiwayatAdapter adapter = new RiwayatAdapter(getActivity(), riwayats);
        listView = (ListView) view.findViewById(R.id.lsView);
        listView.setAdapter(adapter);
        return view;
    }

}
