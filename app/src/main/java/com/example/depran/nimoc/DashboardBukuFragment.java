package com.example.depran.nimoc;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.buku.TambahPemasukanActivity;
import com.example.depran.nimoc.buku.TambahPengeluaranActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardBukuFragment extends Fragment {
    public static DashboardBukuActivity dashboardBukuActivity;
    PieChart pieChart;

    ImageButton pemasukkanBtn, pengeluaranBtn;

    public static DashboardBukuFragment newInstance(DashboardBukuActivity activity) {
        dashboardBukuActivity = activity;
        return new DashboardBukuFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(dashboardBukuActivity).inflate(R.layout.fragment_dashboard_buku, container, false);
        pieChart = (PieChart) view.findViewById(R.id.piechart);

        setDataKeuangan(1000_000,200_000);

        pengeluaranBtn = (ImageButton) view.findViewById(R.id.pengeluaran_btn);
        pemasukkanBtn= (ImageButton) view.findViewById(R.id.pemasukkan_btn);

        pengeluaranBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahPengeluaranActivity.class);
                getActivity().startActivity(intent);
            }
        });
        pemasukkanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahPemasukanActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    private void setDataKeuangan(int pemasukkan, int pengeluaran){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(pemasukkan, "Pemasukkan"));
        yValues.add(new PieEntry(pengeluaran, "Pengeluaran"));

        PieDataSet dataSet = new PieDataSet(yValues, "data_keuangan");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        /*
        CUSTOM COLOR jika mau

        final int[] MY_COLORS = {Color.rgb(192,0,0), Color.rgb(255,0,0), Color.rgb(255,192,0),
                Color.rgb(127,127,127), Color.rgb(146,208,80), Color.rgb(0,176,80), Color.rgb(79,129,189)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        dataSet.setColors(colors);

         */
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }

}
